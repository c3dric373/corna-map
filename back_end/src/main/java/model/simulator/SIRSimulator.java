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
    /**
     * La classe est encore en chantier.
     */
    private List<Double> susceptible;
    /**
     * La classe est encore en chantier.
     */
    private List<Double> infectious;
    /**
     * La classe est encore en chantier.
     */
    private List<Double> recovered;
    /**
     * La classe est encore en chantier.
     */
    private List<Double> dead;

    /**
     * La classe est encore en chantier.
     */
    private Double beta = 0.3;
    /**
     * La classe est encore en chantier.
     */
    private Double lambda = 0.04;
    /**
     * La classe est encore en chantier.
     */
    private Double mu = 0.07;

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
    private int nbIterations = 500;

    /**
     * La classe est encore en chantier.
     *
     * @param iSuceptible La classe est encore en chantier.
     * @param iInfectious La classe est encore en chantier.
     * @param iRecovered  La classe est encore en chantier.
     * @param iDead       La classe est encore en chantier.
     */
    public SIRSimulator(final double iSuceptible,
                        final double iInfectious,
                        final double iRecovered,
                        final double iDead) {
        susceptible = new ArrayList<>();
        infectious = new ArrayList<>();
        recovered = new ArrayList<>();
        dead = new ArrayList<>();
        susceptible.add(iSuceptible);
        infectious.add(iInfectious);
        recovered.add(iRecovered);
        dead.add(iDead);

        model = CauchyProblem.builder()
                .addParameter(iSuceptible,
                        T -> -beta * T.getYi(0) * T.getYi(1))
                .addParameter(iInfectious,
                        T -> beta * T.getYi(0) * T.getYi(1)
                                - lambda * T.getYi(1)
                                - mu * T.getYi(1))
                .addParameter(iRecovered,
                        T -> lambda * T.getYi(1))
                .addParameter(iDead,
                        T -> mu * T.getYi(1))
                .build();
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
                        T -> -beta * T.getYi(0) * T.getYi(1))
                .addParameter(nextValues.get(1),
                        T -> beta * T.getYi(0) * T.getYi(1)
                                - lambda * T.getYi(1)
                                - mu * T.getYi(1))
                .addParameter(nextValues.get(2),
                        T -> lambda * T.getYi(1))
                .addParameter(nextValues.get(3),
                        T -> mu * T.getYi(1))
                .build();
    }
}
