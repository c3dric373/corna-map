package model.simulator;

import lombok.Getter;
import lombok.Setter;
import model.service.AgeCategoryService;
import model.service.DayDataService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * La classe est encore en chantier.
 */
@Getter
@Setter
public class SIRSimulator implements Simulator {

  /**
   * Number of age categories.
   */
  int nbAgeCategory = 5;
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
   * R0 / POP_FRA.
   */
  private double r0Pop = 0.5 / DayDataService.POPULATION_FRA;
  /**
   * La classe est encore en chantier.
   */
  @Setter
  private List<Double> beta =
    Arrays.asList(r0Pop * 0.8, r0Pop * 0.9, r0Pop * 0.7, r0Pop * 0.6,
      r0Pop * 0.5);
  /**
   * La classe est encore en chantier.
   */
  @Setter
  private double gamma = 1 / 15.;
  /**
   * La classe est encore en chantier.
   */
  @Setter
  private List<Double> mu = Arrays.asList(AgeCategoryService.MU_0_15,
    AgeCategoryService.MU_15_44,
    AgeCategoryService.MU_44_64, AgeCategoryService.MU_64_75,
    AgeCategoryService.MU_75_INF);

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

  private double makeTotalInfected(final List<Double> y) {
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
    for (int k = 0; k < nbAgeCategory; ++k) {
      susceptible.get(k).add(nextValues.get(4 * k));
      infectious.get(k).add(nextValues.get(4 * k + 1));
      recovered.get(k).add(nextValues.get(4 * k + 2));
      dead.get(k).add(nextValues.get(4 * k + 3));
    }

    CauchyProblem.Builder builder = CauchyProblem.builder();
    for (int k = 0; k < nbAgeCategory; ++k) {
      int k1 = k;
      builder.addParameter(nextValues.get(4 * k),
        ty -> -beta.get(k1)
          * ty.getY().get(4 * k1)
          * makeTotalInfected(ty.getY()));
      builder.addParameter(nextValues.get(4 * k + 1),
        ty -> beta.get(k1)
          * ty.getY().get(4 * k1)
          * makeTotalInfected(ty.getY())
          - gamma * ty.getY().get(4 * k1 + 1)
          - mu.get(k1) * ty.getY().get(4 * k1 + 1));
      builder.addParameter(nextValues.get(4 * k + 2),
        ty -> gamma * ty.getY().get(4 * k1 + 1));
      builder.addParameter(nextValues.get(4 * k + 3),
        ty -> mu.get(k1) * ty.getY().get(4 * k1 + 1));
    }

    model = builder.build();
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
