package model.service;

import com.google.common.collect.Iterators;
import model.simulator.SJYHRSimulator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgeCategoryService {

  // Stats about french pop
  // https://www.statista.com/statistics/464032/distribution-population-age
  // -group-france/

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
   * Death Rate 0_15.
   */
  public static final double MU_0_15 = 0.003;
  /**
   * Death Rate 0_15.
   */
  public static final double MU_15_44 = 0.01;
  /**
   * Death Rate 0_15.
   */
  public static final double MU_44_64 = 0.08;
  /**
   * Death Rate 0_15.
   */
  public static final double MU_64_75 = 0.22;
  /**
   * Death Rate 0_15.
   */
  public static final double MU_75_INF = 0.44;

  /**
   * Map to facilitate working with the above fields.
   */
  public Map<Integer, Double> indexToPopPercentage = new HashMap<>();

  /**
   * Empty Constructor.
   */
  public AgeCategoryService() {
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
    for (SJYHRSimulator.AgeCategory ageCategory : ageCategories) {
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
    for (SJYHRSimulator.AgeCategory ageCategory : ageCategories) {
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
    for (SJYHRSimulator.AgeCategory ageCategory : ageCategories) {
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
    for (SJYHRSimulator.AgeCategory ageCategory : ageCategories) {
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
