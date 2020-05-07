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
  private double beta = 0.095;
  /**
   * La classe est encore en chantier.
   */
  private double lambda = 0.05;
  /**
   * La classe est encore en chantier.
   */
  private double mu = 0.009;

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
        ty -> -beta * ty.getYi(0) * ty.getYi(1))
      .addParameter(iInfectious,
        ty -> beta * ty.getYi(0) * ty.getYi(1)
          - lambda * ty.getYi(1)
          - mu * ty.getYi(1))
      .addParameter(iRecovered,
        ty -> lambda * ty.getYi(1))
      .addParameter(iDead,
        ty -> mu * ty.getYi(1))
      .build();
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
          - lambda * ty.getYi(1)
          - mu * ty.getYi(1))
      .addParameter(nextValues.get(2),
        ty -> lambda * ty.getYi(1))
      .addParameter(nextValues.get(3),
        ty -> mu * ty.getYi(1))
      .build();
  }

  /**
   * Gouvernemental measures to apply.
   *
   * @param confinedCategories age categories to be confined.
   * @param maskCategories     masked categories to be confined.
   * @param respectConf        percentage of confinement respect.
   */
  public void applyMeasures(final List<Integer> confinedCategories,
                            final List<Integer> maskCategories,
                            final Integer respectConf) {
    for (int confinedCat : confinedCategories) {
      final double betaI = beta.get(confinedCat);
      beta.set(confinedCat, (((0.5 / 3.3) - 1) * respectConf + 1) * betaI);
    }

    for (int maskCat : maskCategories) {
      final double betaI = beta.get(maskCat);
      beta.set(maskCat, 0.32 * betaI);
    }
  }
}
