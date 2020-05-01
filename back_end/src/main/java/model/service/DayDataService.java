package model.service;

import model.data.DayData;
import model.project.ProjectDataWrapper;
import org.apache.commons.lang.Validate;

import java.util.Map;

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
  public static double getRecoveryRate(final DayData dayData,
                                       final ProjectDataWrapper wrapper,
                                       final String location
  ) {
    Validate.notNull(dayData, "dayData null");
    Validate.notNull(wrapper, " wrapper null");
    Map<String, Map<String, DayData>> map =
      wrapper.getProject().getLocations();
    final String dayBefore = dayData.getDate().minusDays(1).toString();
    final DayData dayBeforeData = map.get(location).get(dayBefore);
    final int recoveredInADay = getRecoveredInADay(dayData, dayBeforeData);
    final int infectedDayBefore =
      dayBeforeData.getTotalCases()
        - dayBeforeData.getRecoveredCases() - dayBeforeData.getTotalDeaths();
    return recoveredInADay / (double) infectedDayBefore;
  }

  /**
   * Calculates the newly infected people from one daydata to another.
   */
  private int getInfectedInADay(final DayData dayData,
                                final DayData dayBeforeData) {
    Validate.notNull(dayBeforeData, "dayBeforeData is null");
    Validate.notNull(dayData, "dayData is null");
    final int infectedDayBefore =
      dayBeforeData.getTotalCases()
        - dayBeforeData.getRecoveredCases() - dayBeforeData.getTotalDeaths();
    final int infectedDay =
      dayData.getTotalCases()
        - dayData.getRecoveredCases() - dayData.getTotalDeaths();
    return infectedDay - infectedDayBefore;
  }

  /**
   * Calculates the newly infected people from one daydata to another.
   */
  private static int getRecoveredInADay(final DayData dayData,
                                        final DayData dayBeforeData) {
    Validate.notNull(dayBeforeData, "dayBeforeData is null");
    Validate.notNull(dayData, "dayData is null");
    final int dayBeforeRecovered = dayBeforeData.getRecoveredCases();
    final int recovered = dayData.getRecoveredCases();
    System.out.println(recovered);
    System.out.println(dayBeforeRecovered);
    return recovered - dayBeforeRecovered;
  }

  /**
   * Computes the current deathRate of the coronavirus
   * given on stats of specific day.
   *
   * @param dayData Data of specific day.
   * @return deathRate.
   */
  public static double getDeathRate(
    final DayData dayData,
    final ProjectDataWrapper wrapper,
    final String location
  ) {
    Validate.notNull(dayData, "dayData null");
    Map<String, Map<String, DayData>> map =
      wrapper.getProject().getLocations();
    final String dayBefore = dayData.getDate().minusDays(1).toString();
    final DayData dayBeforeData = map.get(location).get(dayBefore);
    final int deadInADay = getDeadInADay(dayData, dayBeforeData);
    final int infectedDayBefore =
      dayBeforeData.getTotalCases()
        - dayBeforeData.getRecoveredCases() - dayBeforeData.getTotalDeaths();
    System.out.println("deadDay" + deadInADay);
    System.out.println("infecced day befg" + infectedDayBefore);
    return deadInADay / (double) infectedDayBefore;
  }

  private static int getDeadInADay(final DayData dayData,
                                   final DayData dayBeforeData) {
    Validate.notNull(dayBeforeData, "dayBeforeData is null");
    Validate.notNull(dayData, "dayData is null");
    final int dayBeforeDead = dayBeforeData.getTotalDeaths();
    final int dead = dayData.getTotalDeaths();
    return dead - dayBeforeDead;
  }

}
