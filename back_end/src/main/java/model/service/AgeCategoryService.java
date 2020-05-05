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

  public Map<Integer, Double> indexToPopPercentage = new HashMap<>();

  public AgeCategoryService() {
    indexToPopPercentage.put(0, FR_POP_0_14);
    indexToPopPercentage.put(1, FR_POP_15_44);
    indexToPopPercentage.put(2, FR_POP_45_64);
    indexToPopPercentage.put(3, FR_POP_64_75);
    indexToPopPercentage.put(4, FR_POP_75_INF);
  }

  public double getDead(final List<SJYHRSimulator.AgeCategory> ageCategories) {
    int i = 0;
    double sum = 0;
    for (SJYHRSimulator.AgeCategory ageCategory : ageCategories) {
      sum += getDead(ageCategory) * indexToPopPercentage.get(i);
      i++;
    }
    return sum;
  }

  public double getRecovered(final List<SJYHRSimulator.AgeCategory> ageCategories) {
    int i = 0;
    double sum = 0;
    for (SJYHRSimulator.AgeCategory ageCategory : ageCategories) {
      sum += getRecovered(ageCategory) * indexToPopPercentage.get(i);
      i++;
    }
    return sum;
  }

  public double getHospitalized(final List<SJYHRSimulator.AgeCategory> ageCategories) {
    int i = 0;
    double sum = 0;
    for (SJYHRSimulator.AgeCategory ageCategory : ageCategories) {
      sum += getHospitalized(ageCategory) * indexToPopPercentage.get(i);
      i++;
    }
    return sum;
  }

  public double getHeavyInfected(final List<SJYHRSimulator.AgeCategory> ageCategories) {
    int i = 0;
    double sum = 0;
    for (SJYHRSimulator.AgeCategory ageCategory : ageCategories) {
      sum += getHeavyInfected(ageCategory) * indexToPopPercentage.get(i);
      i++;
    }
    return sum;
  }

  public double getLightInfected(final List<SJYHRSimulator.AgeCategory> ageCategories) {
    int i = 0;
    double sum = 0;
    for (SJYHRSimulator.AgeCategory ageCategory : ageCategories) {
      sum += getLightInfected(ageCategory) * indexToPopPercentage.get(i);
      i++;
    }
    return sum;
  }

  private double getDead(final SJYHRSimulator.AgeCategory ageCategory
  ) {
    List<Double> dead = ageCategory.getDi();
    return Iterators.getLast(dead.iterator());
  }

  private double getRecovered(final SJYHRSimulator.AgeCategory ageCategory
  ) {
    List<Double> recovered = ageCategory.getRi();
    return Iterators.getLast(recovered.iterator());
  }

  private double getHospitalized(final SJYHRSimulator.AgeCategory ageCategory
  ) {
    List<List<Double>> hospitalized = ageCategory.getHi();
    double sum = 0;
    for (List<Double> tmp : hospitalized) {
      sum += Iterators.getLast(tmp.iterator());
    }
    return sum;
  }

  private double getLightInfected(final SJYHRSimulator.AgeCategory ageCategory
  ) {
    List<List<Double>> lightInfected = ageCategory.getJi();
    double sum = 0;
    for (List<Double> tmp : lightInfected) {
      sum += Iterators.getLast(tmp.iterator());
    }
    return sum;
  }

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
