package model.simulator;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Simulator.
 */
public class SJYHRSimulator implements Simulator {

    /**
     * People will be divided in five age category, each with its own
     * characteristics.
     */
    @Getter
    @Setter
    private class AgeCategory {
        /**
         * Susceptible.
         */
        List<Double> si;
        /**
         * Light infected.
         */
        List<Double> ji;
        /**
         * Heavy infected.
         */
        List<List<Double>> yi;
        /**
         * Hospitalized.
         */
        List<List<Double>> hi;
        /**
         * Recovered.
         */
        List<Double> ri;
        /**
         * Dead.
         */
        List<Double> di;

        /**
         * Light infection rate.
         */
        double lambdai;
        /**
         * Heavy infection rate.
         */
        double thetai;
        /**
         * Hospitalized death rate.
         */
        double mui;

        /**
         * Constructor of the i-th age category.
         * @param initialState initial rates of every person category.
         * @param i index of the age category.
         * @param lambda light infection rate.
         * @param theta heavy infection rate.
         * @param mu hospitalized death rate.
         */
        AgeCategory(final List<Double> initialState, final int i,
                    final double lambda, final double theta, final double mu) {
            int nbParam = 4 + h + u;
            si = new ArrayList<>();
            si.add(initialState.get(i * nbParam));

            ji = new ArrayList<>();
            ji.add(initialState.get(i * nbParam + 1));

            yi = new ArrayList<>(h);
            for (int j = 0; j < h; ++j) {
                yi.add(new ArrayList<>());
                yi.get(j).add(initialState.get(i * nbParam + 2 + j));
            }

            hi = new ArrayList<>(u);
            for (int j = 0; j < u; ++j) {
                hi.add(new ArrayList<>());
                hi.get(j).add(initialState.get(i * nbParam + 2 + h + j));
            }

            ri = new ArrayList<>();
            ri.add(initialState.get(i * nbParam + 2 + h + u));

            di = new ArrayList<>();
            di.add(initialState.get(i * nbParam + 2 + h + u + 1));

            lambdai = lambda;
            thetai = theta;
            mui = mu;
        }
    }

    // parameters common to all age categories
    /**
     * Number of age categories.
     */
    private final int n;
    /**
     * Max time before going to hospital.
     */
    private final int h;
    /**
     * Max time before leaving hospital.
     */
    private final int u;
    /**
     * Light infection recovery rate.
     */
    double gamma;
    /**
     * Hospitalization rate for the heavily infected.
     */
    List<Double> eta;
    /**
     * Hospitalized recovery rate.
     */
    List<Double> nu;

    /**
     * The n age categories.
     */
    private List<AgeCategory> ageCategories;
    /**
     * The differential equations that describes the model.
     */
    private CauchyProblem model;
    /**
     * The solver method.
     */
    private final DifferentialSolver solver;
    /**
     * The precision for the solver.
     */
    private final int nbIterations;

    /**
     * Constructor
     * @param initS list of size n, initial rates of susceptible.
     * @param initJ list of size n, initial rante of light infected.
     * @param initY list of size n, initial rante of heavy infected.
     */
    public SJYHRSimulator(final List<Double> initS,
                          final List<Double> initJ,
                          final List<Double> initY) {
        n = 5;
        h = 7;
        u = 30;
        gamma = 1. / 15.;
        eta = new ArrayList<>(h);
        eta.add(0.019800996674983);
        eta.add(0.038431577566093);
        eta.add(0.054835871116274);
        eta.add(0.068171503117297);
        eta.add(0.077880078307141);
        eta.add(0.083721159128524);
        eta.add(0.085767695185818);
        nu = new ArrayList<>(u);
        nu.add(0.279529548717468);
        nu.add(0.232378190716258);
        nu.add(0.209836368913548);
        nu.add(0.190466877227672);
        nu.add(0.169403073518076);
        nu.add(0.146313694656761);
        nu.add(0.122408679009779);
        nu.add(0.099222391436451);
        nu.add(0.07804904312467);
        nu.add(0.05972102303667);
        nu.add(0.044588146023765);
        nu.add(0.03260490588277);
        nu.add(0.023459478889861);
        nu.add(0.016700896161408);
        nu.add(0.011840776117942);
        nu.add(0.008421561632826);
        nu.add(0.006053358910595);
        nu.add(0.004426574270714);
        nu.add(0.003308830028639);
        nu.add(0.002533619814707);
        nu.add(0.001986151513213);
        nu.add(0.001589727084837);
        nu.add(0.001294302511991);
        nu.add(0.001067710486963);
        nu.add(0.000889368688435);
        nu.add(0.00074600811335);
        nu.add(0.000628898963787);
        nu.add(0.000532112830217);
        nu.add(0.000451463616165);
        nu.add(0.000383873453542);

        int nbParam = 4 + h + u;
        List<Double> initialState = new ArrayList<>(5 * nbParam);
        for (int i = 0; i < 5; ++i) {
            initialState.add(initS.get(i));
            initialState.add(initJ.get(i));
            initialState.add(initY.get(i));
            for (int j = 0; j < nbParam - 3; ++j) {
                initialState.add(0.);
            }
        }

        ageCategories = new ArrayList<>(n);
        ageCategories.add(new AgeCategory(initialState, 0,
                0.00075, 0.0072, 0.003));
        ageCategories.add(new AgeCategory(initialState, 1,
                0.0005, 0.144, 0.01));
        ageCategories.add(new AgeCategory(initialState, 2,
                0.0004, 1.77, 0.08));
        ageCategories.add(new AgeCategory(initialState, 3,
                0.0003, 6.57, 0.22));
        ageCategories.add(new AgeCategory(initialState, 4,
                0.00007, 12.96, 0.44));

        model = makeModel(initialState);
        solver = new RK4Solver();
        nbIterations = 50;
    }

    /**
     * Update the values for the next day.
     */
    @Override
    public void step() {
        List<Double> nextValues = solver.next(model, nbIterations);

        int nbParam = 4 + h + u;
        for (int i = 0; i < n; ++i) {
            ageCategories.get(i).getSi()
                    .add(nextValues.get(i * nbParam));
            ageCategories.get(i).getJi()
                    .add(nextValues.get(i * nbParam + 1));
            for (int j = 0; j < h; ++j) {
                ageCategories.get(i).getYi().get(j)
                        .add(nextValues.get(i * nbParam + 2 + j));
            }
            for (int j = 0; j < u; ++j) {
                ageCategories.get(i).getHi().get(j)
                        .add(nextValues.get(i * nbParam + 2 + h + j));
            }
            ageCategories.get(i).getRi()
                    .add(nextValues.get(i * nbParam + 2 + h + u));
            ageCategories.get(i).getDi()
                    .add(nextValues.get(i * nbParam + 2 + h + u + 1));
        }

        model = makeModel(nextValues);
    }

    /**
     * Makes the model according to the initial condition.
     * @param state the initial conditions.
     * @return the differential equation system, together with the initial
     * condition
     */
    private CauchyProblem makeModel(final List<Double> state) {
        int nbParam = 4 + h + u;
        CauchyProblem.Builder builder = new CauchyProblem.Builder();
        for (int i = 0; i < n; ++i) {
            final int i1 = i;
            double lambdai = ageCategories.get(i).getLambdai();
            double thetai = ageCategories.get(i).getThetai();

            // S_i
            builder.addParameter(state.get(i * nbParam),
                    // S_i -> J_i and Y_i1
                    ty -> -(lambdai + thetai)
                            * ty.getYi(i1 * nbParam)
                            * totalInfected(ty.getY()));

            // J_i
            builder.addParameter(state.get(i * nbParam + 1),
                    // S_i -> J_i
                    ty -> lambdai
                            * ty.getYi(i1 * nbParam)
                            * totalInfected(ty.getY())
                            // J_i -> R_i
                            - gamma
                            * ty.getYi(i1 * nbParam + 1));

            // Y_i1
            builder.addParameter(state.get(i * nbParam + 2),
                    // S_i -> Y_i1
                    ty -> thetai
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

    /**
     * @param state the state of the system.
     * @return the total amount of infected people.
     */
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

    /**
     * @param state the state of the system.
     * @param i the age category index.
     * @return the part of the derivative related with the heavily infected
     * going to hospital.
     */
    private double heavyInfectedToHospitalized(final List<Double> state,
                                               final int i) {
        double res = 0.;
        int nbParam = 4 + h + u;
        for (int j = 0; j < u; ++j) {
            res += eta.get(j) * state.get(i * nbParam + 2 + j);
        }
        return res;
    }

    /**
     * @param state the state of the system.
     * @param i the age category index.
     * @return the part of the derivative related with people leaving the
     * hospital alive.
     */
    private double hospitalizedToRecovered(final List<Double> state,
                                           final int i) {
        double res = 0.;
        int nbParam = 4 + h + u;
        double mu = ageCategories.get(i).getMui();
        for (int j = 0; j < u; ++j) {
            res += nu.get(j) * state.get(i * nbParam + 2 + h + j) * (1. - mu);
        }
        return res;
    }

    /**
     * @param state the state of the system.
     * @param i the age category index.
     * @return the part of the derivative related with people leaving the
     * hospital dead.
     */
    private double hospitalizedToDead(final List<Double> state,
                                      final int i) {
        double res = 0.;
        int nbParam = 4 + h + u;
        double mu = ageCategories.get(i).getMui();
        for (int j = 0; j < u; ++j) {
            res += nu.get(j) * state.get(i * nbParam + 2 + h + j) * mu;
        }
        return res;
    }
}
