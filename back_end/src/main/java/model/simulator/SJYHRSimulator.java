package model.simulator;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SJYHRSimulator implements Simulator {

    @Getter
    @Setter
    private class AgeCategory {
        List<Double> s; // susceptible
        List<Double> j; // light infected
        List<List<Double>> y; // heavy infected
        List<List<Double>> h; // hospitalized
        List<Double> r; // recovered
        List<Double> d; // dead

        double lambda; // light infection rate
        double gamma; // light infection recovery rate
        double theta; // heavy infection rate
        List<Double> eta; // hospitalization rate for the heavily infected
        List<Double> nu; // hospitalized recovery rate
        double mu; // hospitalized death rate
    }

    private int n; // number of age category
    private int h; // max time before going to hospital
    private int u; // max time before leaving hospital

    private List<AgeCategory> ageCategories;
    private CauchyProblem model;
    private DifferentialSolver solver;
    private int nbIterations;

    @Override
    public void step() {
        List<Double> nextValues = solver.next(model, nbIterations);

        int nbParam = 4 + h + u;
        for(int i = 0; i < n; ++i){
            ageCategories.get(i).getS()
                    .add(nextValues.get(i * nbParam));
            ageCategories.get(i).getJ()
                    .add(nextValues.get(i * nbParam + 1));
            for(int j = 0; j < h; ++j){
                ageCategories.get(i).getY().get(j)
                        .add(nextValues.get(i * nbParam + 2 + j));
            }
            for(int j = 0; j < u; ++j){
                ageCategories.get(i).getH().get(j)
                        .add(nextValues.get(i * nbParam + 2 + h + j));
            }
            ageCategories.get(i).getR()
                    .add(nextValues.get(i * nbParam + 2 + h + u));
            ageCategories.get(i).getD()
                    .add(nextValues.get(i * nbParam + 2 + h + u + 1));
        }

        model = updateModel(nextValues);
    }

    private CauchyProblem updateModel(final List<Double> state) {
        int nbParam = 4 + h + u;
        CauchyProblem.Builder builder = new CauchyProblem.Builder();
        for (int i = 0; i < n; ++i) {
            final int i1 = i;
            double lambda = ageCategories.get(i).getLambda();
            double gamma = ageCategories.get(i).getGamma();
            double theta = ageCategories.get(i).getTheta();
            List<Double> eta = ageCategories.get(i).getEta();
            List<Double> nu = ageCategories.get(i).getNu();
            double mu = ageCategories.get(i).getMu();

            // S_i
            builder.addParameter(state.get(i * nbParam),
                    // S_i -> J_i and Y_i1
                    ty -> -(lambda + theta)
                            * ty.getYi(i1 * nbParam)
                            * totalInfected(ty.getY()));

            // J_i
            builder.addParameter(state.get(i * nbParam + 1),
                    // S_i -> J_i
                    ty -> lambda
                            * ty.getYi(i1 * nbParam)
                            * totalInfected(ty.getY())
                            // J_i -> R_i
                            - gamma
                            * ty.getYi(i1 * nbParam + 1));

            // Y_i1
            builder.addParameter(state.get(i * nbParam + 2),
                    // S_i -> Y_i1
                    ty -> theta
                            * ty.getYi(i1 * nbParam)
                            * totalInfected(ty.getY())
                            // Y_i1 -> Y_i2 and H_i1
                            - ty.getYi(i1 * nbParam + 2));

            // Y_i2 to Y_ih
            for (int j = 1; j < h; ++j) {
                final int j1 = j;
                builder.addParameter(state.get(i * nbParam + 2 + j),
                        // Y_ij-1 -> Y_ij
                        ty -> (1. - eta.get(j1 - 1))
                                * ty.getYi(i1 * nbParam + 2 + j1 - 1)
                                // Y_ij -> Y_ij+1 and H_i1
                                - ty.getYi(i1 * nbParam + 2 + j1));
            }

            // H_i1
            builder.addParameter(state.get(i * nbParam + 2 + h),
                    // Y_i1 to Y_ih -> H_i1
                    ty -> heavyInfectedToHospitalized(ty.getY(), i1)
                            // H_i1 -> H_i2, R_i and D_i
                            - ty.getYi(i1 * nbParam + 2 + h));

            // H_i2 to H_iu
            for (int j = 1; j < u; ++j) {
                int j1 = j;
                builder.addParameter(state.get(i * nbParam + 2 + h + j),
                        // H_ij-1 -> H_ij
                        ty -> (1. - nu.get(j1 - 1))
                                * ty.getYi(i1 * nbParam + 2 + h + j1 - 1)
                                // H_ij -> H_ij+1, R_i and D_i
                                - ty.getYi(i1 * nbParam + 2 + h + j1));
            }

            // R_i
            builder.addParameter(state.get(i * nbParam + 2 + h + u + 1),
                    // H_i1 to H_iu -> R_i
                    ty -> hospitalizedToRecovered(ty.getY(), i1)
                            // J_i to R_i
                            + gamma
                            * ty.getYi(i1 * nbParam + 1));

            // D_i
            builder.addParameter(state.get(i * nbParam + 2 + h + u + 2),
                    // H_i1 to H_iu -> D_i
                    ty -> hospitalizedToDead(ty.getY(), i1));
        }

        return builder.build();
    }

    private double totalInfected(final List<Double> state) {
        double res = 0.;
        int nbParam = 4 + h + u;
        for (int i = 0; i < n; ++i) {
            res += state.get(i * nbParam + 1);
            for (int j = 2; j < h + 2; ++j) {
                res += state.get(i * nbParam + j);
            }
        }
        return res;
    }

    private double heavyInfectedToHospitalized(final List<Double> state,
                                               int i) {
        double res = 0.;
        int nbParam = 4 + h + u;
        List<Double> eta = ageCategories.get(i).getEta();
        for (int j = 0; j < u; ++j) {
            res += eta.get(j) * state.get(i * nbParam + 2 + j);
        }
        return res;
    }

    private double hospitalizedToRecovered(final List<Double> state, int i) {
        double res = 0.;
        int nbParam = 4 + h + u;
        List<Double> nu = ageCategories.get(i).getNu();
        double mu = ageCategories.get(i).getMu();
        for (int j = 0; j < u; ++j) {
            res += nu.get(j) * state.get(i * nbParam + 2 + h + j) * (1. - mu);
        }
        return res;
    }

    private double hospitalizedToDead(final List<Double> state, int i) {
        double res = 0.;
        int nbParam = 4 + h + u;
        List<Double> nu = ageCategories.get(i).getNu();
        double mu = ageCategories.get(i).getMu();
        for (int j = 0; j < u; ++j) {
            res += nu.get(j) * state.get(i * nbParam + 2 + h + j) * mu;
        }
        return res;
    }
}
