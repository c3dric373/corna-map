package model.service;

import com.google.common.collect.Iterators;
import lombok.Getter;
import model.simulator.SJYHRSimulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgeCategoryService {

  // Stats about french pop
  // https://www.statista.com/statistics/464032/
  // distribution-population-age-group-france/

  /**
   * Percentage of people in the 0-14 age class in France.
   */
  public static final double FR_POP_0_14 = 0.178;

  /**
   * Percentage of people in the 15-44 age class in France.
   */
  public static final double FR_POP_15_44 = 0.36;

  /**
   * Percentage of people in the 45-64 age class in France.
   */
  public static final double FR_POP_45_64 = 0.261;

  /**
   * Percentage of people in the 0-14 age class in France.
   */
  public static final double FR_POP_64_75 = 0.108;

  /**
   * Percentage of people in the 0-14 age class in France.
   */
  public static final double FR_POP_75_INF = 0.093;

  /**
   * Death Rate 0_15. All the Following death rate are taken from:
   * https://www.santepubliquefrance.fr/content/download/249184/2589560
   */
  public static final double MU_0_15 = 0.00001 / 15;
  /**
   * Death Rate 15_44.
   */
  public static final double MU_15_44 = 0.0005 / 15;
  /**
   * Death Rate 44_64.
   */
  public static final double MU_44_64 = 0.002 / 15;
  /**
   * Death Rate 64_75.
   */
  public static final double MU_64_75 = 0.005 / 15;
  /**
   * Death Rate 75_INF.
   */
  public static final double MU_75_INF = 0.083 / 15;
  /**
   * Heavy Infection Rate 0_15.
   */
  public static final double THETA_0_15 = 0.000072;
  /**
   * Heavy Infection Rate 15_44.
   */
  public static final double THETA_15_44 = 0.00144;
  /**
   * Heavy Infection Rate 44_64.
   */
  public static final double THETA_44_64 = 0.0177;
  /**
   * Heavy Infection Rate 64_75.
   */
  public static final double THETA_64_75 = 0.0657;
  /**
   * Heavy Infection Rate 75_INF.
   */
  public static final double THETA_75_INF = 0.1296;
  /**
   * Heavy Infection Rate 0_15.
   */
  public static final double C_0_15 = 0.8;
  /**
   * Heavy Infection Rate 15_44.
   */
  public static final double C_15_44 = 0.9;
  /**
   * Heavy Infection Rate 44_64.
   */
  public static final double C_44_64 = 0.6;
  /**
   * Heavy Infection Rate 64_75.
   */
  public static final double C_64_75 = 0.3;
  /**
   * Heavy Infection Rate 75_INF.
   */
  public static final double C_75_INF = 0.1;
  /**
   * List of MU.
   */
  @Getter
  public final List<Double> mu = new ArrayList<>();
  /**
   * List of THETA_I.
   */
  @Getter
  public final List<Double> theta = new ArrayList<>();
  /**
   * List of C_I.
   */
  @Getter
  private final List<Double> c = new ArrayList<>();

  /**
   * Map to facilitate working with the above fields.
   */
  public Map<Integer, Double> indexToPopPercentage = new HashMap<>();

  /**
   * Empty Constructor.
   */
  public AgeCategoryService() {
    c.add(C_0_15);
    c.add(C_15_44);
    c.add(C_44_64);
    c.add(C_64_75);
    c.add(C_75_INF);
    theta.add(THETA_0_15);
    theta.add(THETA_15_44);
    theta.add(THETA_44_64);
    theta.add(THETA_64_75);
    theta.add(THETA_75_INF);
    mu.add(MU_0_15);
    mu.add(MU_15_44);
    mu.add(MU_44_64);
    mu.add(MU_64_75);
    mu.add(MU_75_INF);
    indexToPopPercentage.put(0, FR_POP_0_14);
    indexToPopPercentage.put(1, FR_POP_15_44);
    indexToPopPercentage.put(2, FR_POP_45_64);
    indexToPopPercentage.put(3, FR_POP_64_75);
    indexToPopPercentage.put(4, FR_POP_75_INF);

  }

  /**
   * Computes percentage of dead people in complete Population.
   *
   * @param ageCategories List of data of all ageCategories.
   * @return the percentage of dead people from covid-19.
   */
  public double getDead(final List<SJYHRSimulator.AgeCategory> ageCategories) {
    double sum = 0;
    for (final SJYHRSimulator.AgeCategory ageCategory : ageCategories) {
      sum += getDead(ageCategory);
    }
    return sum;
  }

  /**
   * Computes percentage of recovered people in complete Population.
   *
   * @param ageCategories List of data of all ageCategories.
   * @return the percentage of recovered people from covid-19.
   */
  public double getRecovered(final List<SJYHRSimulator.AgeCategory>
                               ageCategories) {
    double sum = 0;
    for (final SJYHRSimulator.AgeCategory ageCategory : ageCategories) {
      sum += getRecovered(ageCategory);
    }
    return sum;
  }

  /**
   * Computes percentage of hospitalized people in complete Population.
   *
   * @param ageCategories List of data of all ageCategories.
   * @return the percentage of hospitalized people from covid-19.
   */
  public double getHospitalized(final List<SJYHRSimulator.AgeCategory>
                                  ageCategories) {
    double sum = 0;
    for (final SJYHRSimulator.AgeCategory ageCategory : ageCategories) {
      sum += getHospitalized(ageCategory);
    }
    return sum;
  }

  /**
   * Computes percentage of heavy infected people in complete Population.
   *
   * @param ageCategories List of data of all ageCategories.
   * @return the percentage of heavy infected people from covid-19.
   */
  public double getHeavyInfected(final List<SJYHRSimulator.AgeCategory>
                                   ageCategories) {
    double sum = 0;
    for (final SJYHRSimulator.AgeCategory ageCategory : ageCategories) {
      sum += getHeavyInfected(ageCategory);
    }
    return sum;
  }

  /**
   * Computes percentage of light infected people in complete Population.
   *
   * @param ageCategories List of data of all ageCategories.
   * @return the percentage of light infected  people from covid-19.
   */
  public double getLightInfected(final List<SJYHRSimulator.AgeCategory>
                                   ageCategories) {
    double sum = 0;
    for (final SJYHRSimulator.AgeCategory ageCategory : ageCategories) {
      sum += getLightInfected(ageCategory);
    }
    return sum;
  }

  /**
   * Computes percentage of dead people in on age category.
   *
   * @param ageCategory specific ageCategories.
   * @return the percentage of dead people from covid-19 in on age category.
   */
  private double getDead(final SJYHRSimulator.AgeCategory ageCategory
  ) {
    List<Double> dead = ageCategory.getDi();
    return Iterators.getLast(dead.iterator());
  }

  /**
   * Computes percentage of recovered people in on age category.
   *
   * @param ageCategory specific ageCategories.
   * @return the percentage of recovered people from covid-19 in on age
   * category.
   */
  private double getRecovered(final SJYHRSimulator.AgeCategory ageCategory
  ) {
    List<Double> recovered = ageCategory.getRi();
    return Iterators.getLast(recovered.iterator());
  }

  /**
   * Computes percentage of hospitalized people in on age category.
   *
   * @param ageCategory specific ageCategories.
   * @return the percentage of hospitalized people from covid-19 in on age
   * category.
   */
  private double getHospitalized(final SJYHRSimulator.AgeCategory ageCategory
  ) {
    List<List<Double>> hospitalized = ageCategory.getHi();
    double sum = 0;
    for (List<Double> tmp : hospitalized) {
      sum += Iterators.getLast(tmp.iterator());
    }
    return sum;
  }

  /**
   * Computes percentage of light infected people in on age category.
   *
   * @param ageCategory specific ageCategories.
   * @return the percentage of light infected people from covid-19 in on age
   * category.
   */
  private double getLightInfected(final SJYHRSimulator.AgeCategory ageCategory
  ) {
    List<List<Double>> lightInfected = ageCategory.getJi();
    double sum = 0;
    for (List<Double> tmp : lightInfected) {
      sum += Iterators.getLast(tmp.iterator());
    }
    return sum;
  }

  /**
   * Computes percentage of heavy infected people in on age category.
   *
   * @param ageCategory specific ageCategories.
   * @return the percentage of heavy infected people from covid-19 in on age
   * category.
   */
  private double getHeavyInfected(final SJYHRSimulator.AgeCategory ageCategory
  ) {
    List<List<Double>> heavyInfected = ageCategory.getYi();
    double sum = 0;
    for (List<Double> tmp : heavyInfected) {
      sum += Iterators.getLast(tmp.iterator());
    }
    return sum;
  }

}
