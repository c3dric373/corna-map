package model.service;

import model.data.DayData;
import org.apache.commons.lang.Validate;

public class DayDataService {

  /**
   * French population size.
   */
  private static final double POPULATION_FRA = 67000000.0;

  /**
   * Computes the percentage of people susceptible of catching the coronavirus
   * given on stats of specific day.
   *
   * @param dayData Data of specific day.
   * @return percentage of susceptible people.
   */
  public static double getSusceptible(final DayData dayData) {
    Validate.notNull(dayData, "dayData null");
    final int totalCases = dayData.getTotalCases();
    return (POPULATION_FRA - totalCases) / POPULATION_FRA;
  }

  /**
   * Computes the percentage of recovered people from coronavirus
   * given on stats of specific day.
   *
   * @param dayData Data of specific day.
   * @return percentage of recovered people.
   */
  public static double getRecoveryRate(final DayData dayData) {
    Validate.notNull(dayData, "dayData null");
    final double latestDataRecoveredCases = dayData.getRecoveredCases();
    final double latestDataTotalCases = dayData.getTotalCases();
    return latestDataRecoveredCases / latestDataTotalCases;
  }

  /**
   * Computes the current deathRate of the coronavirus
   * given on stats of specific day.
   *
   * @param dayData Data of specific day.
   * @return deathRate.
   */
  public static double getDeathRate(
    final DayData dayData) {
    Validate.notNull(dayData, "dayData null");
    final int latestDead = dayData.getTotalDeaths();
    return latestDead / POPULATION_FRA;
  }

}
