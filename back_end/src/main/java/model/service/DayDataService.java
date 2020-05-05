package model.service;

import model.data.DayData;
import model.project.ProjectDataWrapper;
import org.apache.commons.lang.Validate;

import java.util.HashMap;
import java.util.Map;

public class DayDataService {

  /**
   * French population size.
   */
  private static final double POPULATION_FRA = 67000000.0;

  /**
   * Id for France.
   */
  private static final String FRA = "FRA";

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
   * @param dayData  Data of specific day.
   * @param wrapper  the {@link ProjectDataWrapper} needed
   *                 to access previous
   *                 days.
   * @param location location for which we should calculate
   *                 the rate.
   * @return percentage of recovered people.
   */
  public static double getRecoveryRate(final DayData dayData,
                                       final ProjectDataWrapper wrapper,
                                       final String location
  ) {
    Validate.notNull(dayData, "dayData null");
    Validate.notNull(wrapper, "wrapper null");
    Validate.notNull(location, "location null");
    Validate.notEmpty(location, "location empty");
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
   * Calculates the newly infected people from one dayData to another.
   *
   * @param dayData       the first dayData.
   * @param dayBeforeData the second dayData.
   * @return the number of people recovered in a day.
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
   * @param dayData  Data of specific day.
   * @param wrapper  the {@link ProjectDataWrapper} needed to access previous
   *                 days.
   * @param location location for which we should calculate the rate.
   * @return deathRate.
   */
  public static double getDeathRate(
    final DayData dayData,
    final ProjectDataWrapper wrapper,
    final String location) {
    Validate.notNull(dayData, "dayData null");
    Validate.notNull(wrapper, "wrapper null");
    Validate.notNull(location, "location null");
    Validate.notEmpty(location, "location empty");
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

  /**
   * Compute number of people dead in day (difference between 2 day data).
   *
   * @param dayData       the first dayData.
   * @param dayBeforeData the second dayData.
   * @return the number of people dead in a day.
   */
  private static int getDeadInADay(final DayData dayData,
                                   final DayData dayBeforeData) {
    Validate.notNull(dayBeforeData, "dayBeforeData is null");
    Validate.notNull(dayData, "dayData is null");
    final int dayBeforeDead = dayBeforeData.getTotalDeaths();
    final int dead = dayData.getTotalDeaths();
    return dead - dayBeforeDead;
  }

  /**
   * Computes for each region how much cases it has in relation to the whole
   * country.
   *
   * @param locations data.
   * @param date      the for which we want to get the percentages.
   * @return a map containing for each key (region) the percentage of cases
   * of the whole country/
   */
  public static Map<String, Double> getLocationPercentages(final Map<String,
    Map<String,
      DayData>> locations, final String date) {
    Validate.notNull(locations, "locations null");
    Validate.notEmpty(locations, "locations empty");
    Validate.notNull(date, "date null");
    Validate.notEmpty(date, "date empty");

    final int totalCasesFrance = locations.get(FRA).get(date).getTotalCases();
    Map<String, Double> resultMap = new HashMap<>();
    for (Map.Entry<String, Map<String, DayData>> entry : locations.entrySet()) {
      String id = entry.getKey();
      Map<String, DayData> mapId = entry.getValue();
      DayData dayDataLocation = mapId.get(date);
      if(dayDataLocation == null){
        int i = 0;
      }
      int totalCasesId = dayDataLocation.getTotalCases();

      if (totalCasesId == 0) {
        totalCasesId = computeTotalCases(dayDataLocation);
      }
      final double percentage = (double) totalCasesId / totalCasesFrance;
      resultMap.put(id, percentage);
    }
    return resultMap;
  }

  /**
   * In our dayData sometimes the field total Cases is empty so we have to
   * built it on our own.
   *
   * @param dayData The {@link DayData} for which we want to compute the
   *                total cases
   * @return the total cases for the {@link DayData}
   */
  public static int computeTotalCases(final DayData dayData) {
    Validate.notNull(dayData, "dayData null");
    return dayData.getRecoveredCases() + dayData.getHospitalized()
      + dayData.getCriticalCases() + dayData.getEphadConfirmedCases()
      + dayData.getTotalDeaths() + dayData.getTotalEphadDeaths();
  }

}
