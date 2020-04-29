package model.service;

import model.data.DayData;

public class DayDataService {

  /**
   * French population size.
   */
  final private static double POPULATION_FRA = 67000000.0;

  public static double getSusceptible(final String location,
                                      final DayData latestData) {
    final int totalCases = latestData.getTotalCases();
    return (POPULATION_FRA - totalCases) / POPULATION_FRA;
  }

  public static double getRecoveryRate(final String location,
                                       final DayData latestData) {
    final double latestDataRecoveredCases = latestData.getRecoveredCases();
    final double latestDataTotalCases = latestData.getTotalCases();
    return latestDataRecoveredCases / latestDataTotalCases;
  }

  public static double getDeathRate(final String location,
                                    final DayData latestData) {
    final int latestDead = latestData.getTotalDeaths();
    return latestDead / POPULATION_FRA;
  }

}
