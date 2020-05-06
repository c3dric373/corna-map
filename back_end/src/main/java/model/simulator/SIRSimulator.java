package model.simulator;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe est encore en chantier.
 */
@Getter
@Setter
public class SIRSimulator implements Simulator {

    int nbAgeCategory;
    /**
     * La classe est encore en chantier.
     */
    private List<List<Double>> susceptible;
    /**
     * La classe est encore en chantier.
     */
    private List<List<Double>> infectious;
    /**
     * La classe est encore en chantier.
     */
    private List<List<Double>> recovered;
    /**
     * La classe est encore en chantier.
     */
    private List<List<Double>> dead;

    /**
     * La classe est encore en chantier.
     */
    @Setter
    private List<Double> beta;
    /**
     * La classe est encore en chantier.
     */
    @Setter
    private double gamma;
    /**
     * La classe est encore en chantier.
     */
    @Setter
    private List<Double> mu;

    /**
     * La classe est encore en chantier.
     */
    private CauchyProblem model;
    /**
     * La classe est encore en chantier.
     */
    private DifferentialSolver solver = new RK4Solver();
    /**
     * La classe est encore en chantier.
     */
    private int nbIterations = 100;

    /**
     * La classe est encore en chantier.
     *
     * @param iSusceptible La classe est encore en chantier.
     * @param iInfectious  La classe est encore en chantier.
     * @param iRecovered   La classe est encore en chantier.
     * @param iDead        La classe est encore en chantier.
     */
    public SIRSimulator(final List<Double> iSusceptible,
                        final List<Double> iInfectious,
                        final List<Double> iRecovered,
                        final List<Double> iDead) {
        susceptible = new ArrayList<>(nbAgeCategory);
        infectious = new ArrayList<>(nbAgeCategory);
        recovered = new ArrayList<>(nbAgeCategory);
        dead = new ArrayList<>(nbAgeCategory);

        for (int k = 0; k < nbAgeCategory; ++k) {
            susceptible.add(new ArrayList<>());
            infectious.add(new ArrayList<>());
            recovered.add(new ArrayList<>());
            dead.add(new ArrayList<>());
            susceptible.get(k).add(iSusceptible.get(k));
            infectious.get(k).add(iInfectious.get(k));
            recovered.get(k).add(iRecovered.get(k));
            dead.get(k).add(iDead.get(k));
        }

        CauchyProblem.Builder builder = CauchyProblem.builder();
        for (int k = 0; k < nbAgeCategory; ++k) {
            int k1 = k;
            builder.addParameter(iSusceptible.get(k),
                    ty -> -beta.get(k1)
                            * ty.getY().get(4 * k1)
                            * makeTotalInfected(ty.getY()));
            builder.addParameter(iInfectious.get(k),
                    ty -> beta.get(k1)
                            * ty.getY().get(4 * k1)
                            * makeTotalInfected(ty.getY())
                            - gamma * ty.getY().get(4 * k1 + 1)
                            - mu.get(k1) * ty.getY().get(4 * k1 + 1));
            builder.addParameter(iRecovered.get(k),
                    ty -> gamma * ty.getY().get(4 * k1 + 1));
            builder.addParameter(iDead.get(k),
                    ty -> mu.get(k1) * ty.getY().get(4 * k1 + 1));
        }

        model = builder.build();
    }

    private double makeTotalInfected(List<Double> y) {
        double totalInfected = 0.;
        for (int k = 0; k < nbAgeCategory; ++k) {
            totalInfected += y.get(4 * k + 1);
        }
        return totalInfected;
    }

    /**
     * Empty Constructor.
     */
    public SIRSimulator() {
    }

    /**
     * La classe est encore en chantier.
     */
    public void step() {
        List<Double> nextValues = solver.next(model, nbIterations);
        susceptible.add(nextValues.get(0));
        infectious.add(nextValues.get(1));
        recovered.add(nextValues.get(2));
        dead.add(nextValues.get(3));

        model = CauchyProblem.builder()
                .addParameter(nextValues.get(0),
                        ty -> -beta * ty.getYi(0) * ty.getYi(1))
                .addParameter(nextValues.get(1),
                        ty -> beta * ty.getYi(0) * ty.getYi(1)
                                - gamma * ty.getYi(1)
                                - mu * ty.getYi(1))
                .addParameter(nextValues.get(2),
                        ty -> gamma * ty.getYi(1))
                .addParameter(nextValues.get(3),
                        ty -> mu * ty.getYi(1))
                .build();
    }

}
