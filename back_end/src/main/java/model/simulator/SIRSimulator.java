package model.simulator;

import lombok.Getter;
import lombok.Setter;
import model.service.AgeCategoryService;
import model.service.DayDataService;
import org.apache.commons.lang.Validate;

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
   * List containing susceptible ratio per age category.
   */
  private List<List<Double>> susceptible;
  /**
   * List containing infectious ratio per age category.
   */
  private List<List<Double>> infectious;
  /**
   * List containing recovered ratio per age category.
   */
  private List<List<Double>> recovered;
  /**
   * List containing dead ratio per age category. en chantier.
   */
  private List<List<Double>> dead;

  /**
   * R0 / POP_FRA.
   */
  private double r0Pop = 0.5 / DayDataService.POPULATION_FRA;
  /**
   * Beta parameter in SIR for each age category (=transmission).
   */
  @Setter
  private List<Double> beta =
    Arrays.asList(r0Pop * 0.8, r0Pop * 0.9, r0Pop * 0.7, r0Pop * 0.6,
      r0Pop * 0.5);
  /**
   * Gamma parameter in sir, recovery Rate.
   */
  @Setter
  private double gamma = 1 / 15.;
  /**
   * Mu parameter in SIR for each age category (=lethality).
   */
  @Setter
  private List<Double> mu = Arrays.asList(AgeCategoryService.MU_0_15,
    AgeCategoryService.MU_15_44,
    AgeCategoryService.MU_44_64, AgeCategoryService.MU_64_75,
    AgeCategoryService.MU_75_INF);

  /**
   * Cauchy Problem used to represent our SIRD model.
   */
  private CauchyProblem model;
  /**
   * Differential Equation solver used to simulate our model.
   */
  private DifferentialSolver solver = new RK4Solver();
  /**
   * Granularity of our solver differential equation solver.
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
   * Simulate a day.
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
   * @param measures measures to apply
   */
  public void applyMeasures(final List<List<Integer>> measures) {
    Validate.notNull(measures, "measures Null");
    // Get measures
    final int confinedCategoriesIndex = 0;
    final int maskedCategoriesIndex = 1;
    final int confinementRespectIndex = 2;
    final List<Integer> confinedCategories =
      measures.get(confinedCategoriesIndex);
    final List<Integer> maskedCategories = measures.get(maskedCategoriesIndex);
    final double confinementRespect =
      measures.get(confinementRespectIndex).get(0);
    for (int confinedCat : confinedCategories) {
      final double betaI = beta.get(confinedCat);
      beta.set(confinedCat,
        (((0.5 / 3.3) - 1) * confinementRespect + 1) * betaI);
    }

    for (int maskCat : maskedCategories) {
      final double betaI = beta.get(maskCat);
      beta.set(maskCat, 0.32 * betaI);
    }
  }
}
