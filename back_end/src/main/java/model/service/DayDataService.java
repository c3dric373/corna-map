package model.service;

import model.data.DayData;
import model.simulator.SJYHRSimulator;
import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DayDataService {

  /**
   * French population size.
   */
  public static final double POPULATION_FRA = 67000000.0;

  /**
   * Id for France.
   */
  private static final String FRA = "FRA";

  /**
   * Factor used to approximate the total number of infected people in relation
   * to the total number of cases.
   * Taken from:https://hal-pasteur.archives-ouvertes.fr/pasteur-02548181
   */
  private static final int TOTAL_CASES_TO_INFECTED_FACTOR = 40;

  /**
   * Number of age categories in our model.
   */
  private static final int NB_AGE_CATEGORIES = 5;

  /**
   * Computes the percentage of people susceptible of catching the coronavirus
   * given on stats of specific day.
   *
   * @param sum sum of other
   * @return percentage of susceptible people.
   */
  public static List<Double> getSusceptibleSIR(final double sum) {
    double susceptible = 1 - sum;
    return computePercentageAgeClasses(susceptible);

  }

  /**
   * Computes the percentage of recovered people from coronavirus
   * given on stats of specific day.
   *
   * @param dayData Data of specific day.
   * @return percentage of recovered people.
   */
  public static List<Double> getRecoveryRateSIR(final DayData dayData) {
    Validate.notNull(dayData, "dayData null");
    double recoveryRate =
      dayData.getRecoveredCases() * TOTAL_CASES_TO_INFECTED_FACTOR
        / POPULATION_FRA;
    return computePercentageAgeClasses(recoveryRate);
  }

  /**
   * Computes the current deathRate of the coronavirus
   * given on stats of specific day.
   *
   * @param dayData Data of specific day.
   * @return deathRate.
   */
  public static List<Double> getDeathRateSIR(
    final DayData dayData) {
    Validate.notNull(dayData, "dayData null");
    double deathRate = dayData.getTotalDeaths() / POPULATION_FRA;
    return computePercentageAgeClasses(deathRate);
  }

  /**
   * Get infectious.
   *
   * @param latestData latest data.
   * @return percentage of infectious people.
   */
  public static List<Double> getInfectiousSir(final DayData latestData) {
    Validate.notNull(latestData, "dayData null");
    double infected =
      latestData.getHospitalized() * TOTAL_CASES_TO_INFECTED_FACTOR
        / POPULATION_FRA;
    return computePercentageAgeClasses(infected);
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

  /**
   * Computes for each age class the percentage of people
   * susceptible to get infected from the COVID-19.
   *
   * @param latestData The data for which we should calculate such percentages.
   * @return List of percentages.
   */
  public static List<Double> getSusceptibleSJYHR(final DayData latestData) {
    Validate.notNull(latestData, "dayData null");
    final int totalCases = latestData.getTotalCases();
    final double susceptibleFra =
      (POPULATION_FRA - totalCases) / POPULATION_FRA;
    System.out.println("Susceptible FRA: " + susceptibleFra);
    return computePercentageAgeClasses(susceptibleFra);
  }

  /**
   * Computes for each age class the percentage of people
   * lightly infected from the COVID-19.
   *
   * @param latestData The data for which we should calculate such percentages.
   * @param simulator  simulator.
   * @return List of percentages.
   */
  public static List<Double> getInfectedSJYHR(final DayData latestData,
                                              final SJYHRSimulator simulator) {
    Validate.notNull(latestData, "dayData null");
    Validate.notNull(simulator, "simulator null");
    final int totalCases = latestData.getTotalCases();
    final int infectedCases =
      (totalCases - latestData.getHospitalized() - latestData.getTotalDeaths()
        - latestData.getRecoveredCases()) * 150;
    final double sumCi =
      simulator.getC().stream().mapToDouble(f -> f).sum();
    final List<Double> betaI =
      simulator.getC().stream().map(ci -> ci
        / sumCi * infectedCases / POPULATION_FRA)
        .collect(Collectors.toList());
    return computePercentageAgeClasses(betaI);
  }

  /**
   * Computes list of light infected people from COVID-19 per age class.
   *
   * @param initI              List of infected people per age Class.
   * @param ageCategoryService ageCategoryService giving us access to initial
   *                           parameters
   * @return the list of ratios of
   * light infected  people per age group.
   */
  public static List<Double> getLightInfectedSJYHR(final List<Double> initI,
                                                   final AgeCategoryService
                                                     ageCategoryService) {
    final List<Double> result = new ArrayList<>(5);
    for (int i = 0; i < initI.size(); i++) {
      final double thetaI = ageCategoryService.getTheta().get(i);
      result.add(initI.get(i) * (1 - thetaI));
    }
    return result;
  }

  /**
   * Computes list of heavy infected people from COVID-19 per age class.
   *
   * @param initI              List of infected people per age Class.
   * @param ageCategoryService ageCategoryService giving us access to initial
   *                           parameters
   * @return the list of ratios of
   * heavy infected  people per age group.
   */
  public static List<Double> getHeavyInfectedSJYHR(final List<Double> initI,
                                                   final AgeCategoryService
                                                     ageCategoryService) {
    final List<Double> result = new ArrayList<>(5);
    for (int i = 0; i < initI.size(); i++) {
      final double thetaI = ageCategoryService.getTheta().get(i);
      result.add(initI.get(i) * (thetaI));
    }
    return result;
  }

  /**
   * Computes list of heavy infected people from COVID-19 per age class.
   *
   * @param initI      List of infected people per age Class.
   * @param latestData Data that sets the base state for the calculation.
   * @return the list of ratios of hospitalized people per age group.
   */
  public static List<Double> getHospitalizedSJYHR(final List<Double> initI,
                                                  final DayData latestData) {
    final int criticalCases = latestData.getCriticalCases();
    final double sumInitI = initI.stream().mapToDouble(f -> f).sum();
    return initI.stream().map(infectedCat -> (infectedCat / sumInitI)
      * criticalCases / POPULATION_FRA).collect(Collectors.toList());
  }

  /**
   * Computes list of dead people from COVID-19 per age class.
   *
   * @param latestData         Data setting base state.
   * @param initH              List of hospitalized people per age Class.
   * @param ageCategoryService ageCategoryService giving us access to initial
   *                           parameters
   * @return the list ratios of dead people per age group.
   */
  public static List<Double> getDeadSJYHR(final DayData latestData,
                                          final List<Double> initH,
                                          final AgeCategoryService
                                            ageCategoryService) {
    final int totalDeaths = latestData.getTotalDeaths();
    final double sumMuI =
      ageCategoryService.getMu().stream().mapToDouble(f -> f).sum();
    final List<Double> result = new ArrayList<>(5);
    for (int i = 0; i < initH.size(); i++) {
      result.add(totalDeaths / POPULATION_FRA
        * ageCategoryService.getMu().get(i) / sumMuI);
    }
    return result;
  }

  /**
   * Computes list of dead people from COVID-19 per age class.
   *
   * @param latestData Data setting base state.
   * @param initH      List of hospitalized people per age Class.
   * @param initJ      List of light infected people people per age Class.
   * @return the list of ratios of recovered people per age group.
   */
  public static List<Double> getRecoveredSJYHR(final DayData latestData,
                                               final List<Double> initJ,
                                               final List<Double> initH) {
    final double sumJ = initJ.stream().mapToDouble(f -> f).sum();
    final double sumH = initH.stream().mapToDouble(f -> f).sum();
    final double alphaJ = sumJ / (sumJ + sumH);
    final double alphaH = sumH / (sumJ + sumH);
    final int recovered = latestData.getRecoveredCases();
    final List<Double> result = new ArrayList<>(NB_AGE_CATEGORIES);
    for (int i = 0; i < initJ.size(); i++) {
      result.add((recovered * (initJ.get(i) * alphaJ / sumJ
        + initH.get(i) * alphaH / sumH)) / POPULATION_FRA);
    }
    return result;
  }

  /**
   * converts a parameter into a list of weighted parameters according to the
   * age distribution in france.
   *
   * @return the list with the weighted parameters
   */
  private static List<Double> computePercentageAgeClasses(final double param) {
    final double infectious014 = param * AgeCategoryService.FR_POP_0_14;
    final double infectious1544 = param * AgeCategoryService.FR_POP_15_44;
    final double infectious4564 = param * AgeCategoryService.FR_POP_45_64;
    final double infectious6475 = param * AgeCategoryService.FR_POP_64_75;
    final double infectious75INF = param * AgeCategoryService.FR_POP_75_INF;
    return createParamsList(infectious014, infectious1544, infectious4564,
      infectious6475, infectious75INF);
  }

  /**
   * Apply weight of people distribution according to age categories in france
   * to a list of
   * size 5.
   *
   * @param params List of parameters.
   * @return weighted list of params.
   */
  private static List<Double> computePercentageAgeClasses(final List<Double>
                                                            params) {
    Validate.notNull(params, "params null");
    Validate.isTrue(params.size() == NB_AGE_CATEGORIES,
      "list does not contains exactly 5 elements");
    final double infectious014 = params.get(0) * AgeCategoryService.FR_POP_0_14;
    final double infectious1544 =
      params.get(1) * AgeCategoryService.FR_POP_15_44;
    final double infectious4564 =
      params.get(2) * AgeCategoryService.FR_POP_45_64;
    final double infectious6475 =
      params.get(3) * AgeCategoryService.FR_POP_64_75;
    final double infectious75INF =
      params.get(4) * AgeCategoryService.FR_POP_75_INF;
    return createParamsList(infectious014, infectious1544, infectious4564,
      infectious6475, infectious75INF);
  }

  /**
   * Create a liste with all the given parameters in order from method call.
   *
   * @param param014   Param at index 0 in returned List.
   * @param param1544  Param at index 1 in returned List.
   * @param param4564  Param at index 2 in returned List.
   * @param param6475  Param at index 3 in returned List.
   * @param param75INF Param at index 4 in returned List.
   * @return List containing all methods parameters.
   */
  private static List<Double> createParamsList(final double param014,
                                               final double param1544,
                                               final double param4564,
                                               final double param6475,
                                               final double param75INF) {
    final List<Double> result = new ArrayList<>();
    result.add(param014);
    result.add(param1544);
    result.add(param4564);
    result.add(param6475);
    result.add(param75INF);
    return result;
  }

  /**
   * Sets all parameters on a {@link DayData}
   *
   * @param dead          number of dead people.
   * @param recovered     number of recovered people.
   * @param susceptible   number of susceptible people.
   * @param hospitalized  number of hospitalized people.
   * @param heavyInfected number of heavyInfected people.
   * @return the  created {@link DayData}
   */
  public static DayData setSJYHRDayData(double dead, double recovered,
                                        double susceptible, double hospitalized,
                                        double heavyInfected) {
    final DayData result = new DayData();
    result.setTotalDeaths((int) (dead * DayDataService.POPULATION_FRA));
    result.setRecoveredCases((int) (recovered
      * DayDataService.POPULATION_FRA));
    result.setTotalCases((int) ((1 - susceptible)
      * DayDataService.POPULATION_FRA));
    result.setHospitalized((int) (hospitalized
      * DayDataService.POPULATION_FRA));
    result.setCriticalCases((int) (heavyInfected
      * DayDataService.POPULATION_FRA));
    return result;
  }

  /**
   * * Sets all parameters on a {@link DayData}
   *
   * @param dead        number of dead people.
   * @param recovered   number of recovered people.
   * @param susceptible number of susceptible people.
   * @return the created {@link DayData}
   */
  public static DayData setSIRDayData(final double dead,
                                      final double recovered,
                                      final double susceptible) {
    final DayData dayData = new DayData();
    dayData.setTotalDeaths((int) (dead * DayDataService.POPULATION_FRA));
    dayData.setRecoveredCases((int) (recovered * DayDataService.POPULATION_FRA)
      / TOTAL_CASES_TO_INFECTED_FACTOR);
    dayData.setTotalCases((int) (DayDataService.POPULATION_FRA
      - (susceptible * DayDataService.POPULATION_FRA))
      / TOTAL_CASES_TO_INFECTED_FACTOR);
    return dayData;
  }
}

