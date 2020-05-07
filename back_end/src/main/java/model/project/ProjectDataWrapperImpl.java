package model.project;

import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import lombok.Getter;
import model.data.DayData;
import model.io.DataScrapperImpl;
import model.service.AgeCategoryService;
import model.service.DayDataService;
import model.service.SimulatorService;
import model.simulator.SIRSimulator;
import model.simulator.SJYHRSimulator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class acts as a wrapper for the {@link ProjectData} object. It offers
 * convenient methods to add, modify and remove project to all
 * project types that are stored inside the ProjectData object.
 * Most methods making changes to the
 * project return a value which will be passed on to  the View about those
 * changes.
 */
@Getter
public class ProjectDataWrapperImpl implements ProjectDataWrapper {

  /**
   * Id for france to access data.
   */
  private static final String FRA = "FRA";
  /**
   * Population of France.
   */
  private static final double POPULATION_FRA = 67000000.0;
  /**
   * Comparator used to compare dates as string.
   */
  private final DateComparator dateComparator = new DateComparator();
  /**
   * The {@link ProjectData} this wrapper manages.
   */
  @Getter
  private final ProjectData project = new ProjectDataImpl();

  /**
   * Simulator used to simulate COVID-19.
   */
  private SJYHRSimulator sjyhrSimulator = new SJYHRSimulator();

  /**
   * {@link SIRSimulator} used to simulate COVID-19.
   */
  private SIRSimulator sirSimulator = new SIRSimulator();

  /**
   * Dictionary mapping id to name for regions and departments.
   */
  private final Map<String, String> idToName = new HashMap<>();

  /**
   * Service used to compute numbers after simulating.
   */
  AgeCategoryService ageCategoryService = new AgeCategoryService();

  /**
   * Service used to compute numbers after simulating.
   */
  SimulatorService simulatorService = new SimulatorService();

  /**
   * Flag to know if we should use a SIRSimulator or not.
   */
  private boolean useSir = true;

  /**
   * Latest Content.
   */
  private String latestContent;

  @Override
  public void getCurrentAllDataFrance() throws IOException {
    final DataScrapperImpl dataScrapper = new DataScrapperImpl();
    dataScrapper.getCurrentDataFromWeb();
    dataScrapper.extract(this);
  }

  @Override
  public void startSimulation(final String content, final boolean sir) {
    if (sir) {
      setSirSimulator(content);
      latestContent = content;
      useSir = true;
    } else {
      setSJYHRSimulator(content);
      latestContent = content;
      useSir = false;
    }
  }

  @Override
  public void addLocation(final String location, final String date,
                          final DayData dayData) {
    project.getLocations().get(location).put(date, dayData);
    idToName.put(dayData.getId(), dayData.getName());
  }

  @Override
  public DayData infosFrance(final String date) {
    final Map<String, Map<String, DayData>> localisations =
      project.getLocations();
    final Map<String, DayData> tmp = localisations.get(FRA);
    return tmp.get(date);
  }

  @Override
  public List<DayData> historyLocation(final String name) {
    final Map<String, Map<String, DayData>> locations =
      project.getLocations();
    final Map<String, DayData> tmp = locations.get(name);
    return new ArrayList<>(tmp.values());
  }

  @Override
  public DayData infosLocation(final String name, final String date) {
    final Map<String, Map<String, DayData>> localisations =
      project.getLocations();
    final Map<String, DayData> tmp = localisations.get(name);
    return tmp.get(date);
  }

  @Override
  public List<DayData> infosRegion(final String date) {
    final Map<String, Map<String, DayData>> locations =
      project.getLocations().entrySet().stream().filter(map -> map.getKey()
        .contains("REG")).
        collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    final List<DayData> res = new ArrayList<>(locations.size());
    for (Map<String, DayData> map : locations.values()) {
      res.add(map.get(date));
    }
    return res;
  }

  @Override
  public List<DayData> infosDept(final String date) {
    final Map<String, Map<String, DayData>> locations =
      project.getLocations().entrySet().stream().filter(
        map -> map.getKey()
          .contains("DEP")).
        collect(Collectors.toMap(Map.Entry::getKey,
          Map.Entry::getValue));
    final List<DayData> res = new ArrayList<>(locations.size());
    for (Map<String, DayData> map : locations.values()) {
      res.add(map.get(date));
    }
    return res;
  }

  @Override
  public void addKey(final String key) {
    final Map<String, Map<String, DayData>> locations =
      project.getLocations();
    locations.computeIfAbsent(key, k -> new HashMap<>());
  }

  @Override
  public DayData simulateFrance(final String date) {
    DayData latestData = getLatestData(FRA);
    LocalDate latestDate = latestData.getDate();
    // We need to check if the date is in the future or the past
    // if it's in the past we only return the data of the asked day and delete
    // the days coming after
    if (latestDate.isAfter(LocalDate.parse(date))
      || latestDate.equals(LocalDate.parse(date))) {
      truncateData(date);
      // Here we need to get latest data again because we have deleted
      // every date after date.
      if (useSir) {
        setSirSimulator(latestContent);
      } else {
        setSJYHRSimulator(latestContent);
      }
      return getLatestData(FRA);
    }
    Map<String, Double> locationPercentages =
      DayDataService.getLocationPercentages(this.project.getLocations(),
        latestDate.toString());
    DayData dayData = new DayData();
    while (!LocalDate.parse(date).equals(latestDate)) {
      // Simulate a day
      if (useSir) {
        dayData = simulateDaySir();
      } else {
        dayData = simulateDaySJYHR();
      }
      // Add the new data to the model and increase the date which we are
      // iterating on.
      LocalDate newDate = latestDate.plusDays(1);
      dayData.setDate(newDate);
      addLocation(FRA, newDate.toString(), dayData);
      for (Map.Entry<String, Double> entry : locationPercentages.entrySet()) {
        final String id = entry.getKey();
        final double percentage = entry.getValue();
        DayData dayDataLocation = new DayData();
        final int deathsLocation =
          (int) ((double) dayData.getTotalDeaths() * percentage);
        final int totalCasesLocation =
          (int) ((double) dayData.getTotalCases() * percentage);
        final int recoveredLocation =
          (int) ((double) dayData.getRecoveredCases() * percentage);
        dayDataLocation.setId(id);
        dayDataLocation.setName(idToName.get(id));
        dayDataLocation.setTotalDeaths(deathsLocation);
        dayDataLocation.setTotalCases(totalCasesLocation);
        dayDataLocation.setRecoveredCases(recoveredLocation);
        dayDataLocation.setDate(newDate);
        addLocation(id, newDate.toString(), dayDataLocation);
      }
      latestDate = newDate;
    }

    return dayData;
  }

  /**
   * Sets the parameter on the simulator with the latest data.
   *
   * @param content content.
   */
  private void setSirSimulator(final String content) {
    DayData latestData = getLatestData(FRA);
    final double deathRate = DayDataService.getDeathRateSIR(latestData);
    final double recoveryRate = DayDataService.getRecoveryRateSIR(latestData);
    final double susceptible = DayDataService.getSusceptibleSIR(latestData);
    final double infectious = 1 - susceptible;
    System.out.println("RecoveryRateBase: " + recoveryRate);
    System.out.println("RecoveredBase:  " + recoveryRate * POPULATION_FRA);
    sirSimulator = new SIRSimulator(susceptible,
      infectious, recoveryRate, deathRate);
    // Apply Measures
    final Gson gson = new Gson();
    final Map map = gson.fromJson(content, Map.class);
    List<List<Integer>> measures = simulatorService.getMeasures(map);
    final int confinedCategoriesIndex = 0;
    final int maskedCategoriesIndex = 1;
    final int confinementRespectIndex = 2;
    sirSimulator.applyMeasures(measures.get(confinedCategoriesIndex),
      measures.get(maskedCategoriesIndex),
      measures.get(confinementRespectIndex).get(0));
  }

  /**
   * Simulates a the spread of COVID-19 for one day, according to a given
   * simulator.
   *
   * @return the simulated data.
   */
  private DayData simulateDaySir() {
    // Simulate a day
    sirSimulator.step();
    final double deadNew = Iterables.getLast(sirSimulator.getDead());
    final double recoveredNew = Iterables.getLast(sirSimulator.getRecovered());
    final double susceptibleNew =
      Iterables.getLast(sirSimulator.getSusceptible());

    // Create Object which encapsulates the simulated data
    final DayData dayData = new DayData();
    dayData.setTotalDeaths((int) (deadNew * POPULATION_FRA));
    dayData.setRecoveredCases((int) (recoveredNew * POPULATION_FRA));
    System.out.println("recovered " + dayData.getRecoveredCases());
    dayData.setTotalCases((int) (POPULATION_FRA
      - (susceptibleNew * POPULATION_FRA)));
    return dayData;
  }

  /**
   * Sets the parameter on the simulator with the latest data.
   *
   * @param content measures values
   */
  private void setSJYHRSimulator(final String content) {
    // Latest data
    DayData latestData = getLatestData(FRA);
    System.out.println("Donne de base sim: " + latestData);

    // Apply InitialState
    final List<Double> initS = DayDataService.getSusceptibleSJYHR(latestData);
    int sum = 0;
    for (int i = 0; i < initS.size(); i++) {
      System.out.println("InitS " + i + ": " + initS.get(i) * POPULATION_FRA);
      sum += initS.get(i) * POPULATION_FRA;
    }
    System.out.println("Sum: " + sum);
    sum = 0;

    final List<Double> initI = DayDataService.getInfectedSJYHR(latestData,
      sjyhrSimulator);
    for (int i = 0; i < initI.size(); i++) {
      System.out.println("initI " + i + ": " + initI.get(i) * POPULATION_FRA);
      sum += initI.get(i) * POPULATION_FRA;

    }
    System.out.println("Sum: " + sum);
    sum = 0;

    final List<Double> initJ = DayDataService.getLightInfectedSJYHR(initI,
      ageCategoryService);
    for (int i = 0; i < initJ.size(); i++) {
      System.out.println("initJ " + i + ": " + initJ.get(i) * POPULATION_FRA);
      sum += initJ.get(i) * POPULATION_FRA;

    }
    System.out.println("Sum: " + sum);
    sum = 0;

    final List<Double> initY = DayDataService.getHeavyInfectedSJYHR(initI,
      ageCategoryService);
    for (int i = 0; i < initY.size(); i++) {
      System.out.println("initY " + i + ": " + initY.get(i) * POPULATION_FRA);
      sum += initY.get(i) * POPULATION_FRA;
    }
    System.out.println("Sum: " + sum);
    sum = 0;

    final List<Double> initH = DayDataService.getHospitalizedSJYHR(initI,
      latestData);
    for (int i = 0; i < initH.size(); i++) {
      System.out.println("initH " + i + ": " + initH.get(i) * POPULATION_FRA);
      sum += initH.get(i) * POPULATION_FRA;

    }
    System.out.println("Sum: " + sum);
    sum = 0;

    final List<Double> initD = DayDataService.getDeadSJYHR(latestData,
      initH, ageCategoryService);
    for (int i = 0; i < initD.size(); i++) {
      System.out.println("InitD " + i + ": " + initD.get(i) * POPULATION_FRA);
      sum += initD.get(i) * POPULATION_FRA;
    }
    System.out.println("Sum: " + sum);
    sum = 0;
    final List<Double> initR = DayDataService.getRecoveredSJYHR(latestData,
      initJ, initH);
    for (int i = 0; i < initR.size(); i++) {
      System.out.println("initR " + i + ": " + initR.get(i) * POPULATION_FRA);
      sum += initR.get(i) * POPULATION_FRA;
    }
    System.out.println("Sum: " + sum);

    sjyhrSimulator.setInitialStates(initS, initJ, initY, initH, initR, initD);

    // Apply Measures
    final Gson gson = new Gson();
    final Map map = gson.fromJson(content, Map.class);
    List<List<Integer>> measures = simulatorService.getMeasures(map);
    final int confinedCategoriesIndex = 0;
    final int maskedCategoriesIndex = 1;
    final int confinementRespectIndex = 2;
    sjyhrSimulator.applyMeasures(measures.get(confinedCategoriesIndex),
      measures.get(maskedCategoriesIndex),
      measures.get(confinementRespectIndex).get(0));

  }

  /**
   * Deletes all the entries after a specific date.
   *
   * @param date the specific date.
   */
  private void truncateData(final String date) {
    final Map<String, Map<String, DayData>> locations =
      project.getLocations();
    for (Map.Entry<String, Map<String, DayData>> entry : locations.entrySet()) {
      final Map<String, DayData> mapId = entry.getValue();
      mapId.keySet().removeIf(key
        -> LocalDate.parse(key).isAfter(LocalDate.parse(date)));
    }
  }

  /**
   * Simulates a the spread of COVID-19 for one day, according to a given
   * sjyhrSimulator.
   *
   * @return the simulated data.
   */
  private DayData simulateDaySJYHR() {
    // Simulate a day
    sjyhrSimulator.step();
    final DayData result = new DayData();
    final List<SJYHRSimulator.AgeCategory> ageCategories =
      sjyhrSimulator.getAgeCategories();
    final double dead = ageCategoryService.getDead(ageCategories);
    for (SJYHRSimulator.AgeCategory ageCategory : ageCategories) {
      System.out.println("DeadAgeCat: " + ageCategory.getDi());
    }
    System.out.println("Dead: " + dead);
    final double lightInfected =
      ageCategoryService.getLightInfected(ageCategories);
    final double hospitalized =
      ageCategoryService.getHospitalized(ageCategories);
    final double recovered = ageCategoryService.getRecovered(ageCategories);
    final double heavyInfected =
      ageCategoryService.getHeavyInfected(ageCategories);
    final double infected = lightInfected + heavyInfected + hospitalized;
    final double susceptible = 1 - infected - dead;
    // Create Object which encapsulates the simulated data
    result.setTotalDeaths((int) (dead * POPULATION_FRA));
    result.setRecoveredCases((int) (recovered * POPULATION_FRA));
    result.setTotalCases((int) ((1 - susceptible) * POPULATION_FRA));
    result.setHospitalized((int) (hospitalized * POPULATION_FRA));
    result.setCriticalCases((int) (heavyInfected * POPULATION_FRA));
    return result;
  }

  /**
   * Get's the latest available data (Covid-19 stats) for a specific location.
   *
   * @param location The location for which we want the data.
   * @return the data.
   */
  public DayData getLatestData(final String location) {
    final Map<String, Map<String, DayData>> localisations =
      project.getLocations();
    final Map<String, DayData> franceMap = localisations.get(location);
    final Optional<String> latestDate =
      franceMap.keySet().stream().max(dateComparator);
    if (latestDate.isEmpty()) {
      throw new IllegalStateException("No max date in map");
    }
    return franceMap.get(latestDate.get());
  }

  /**
   * Simple class to compare to string dates.
   */
  private static class DateComparator implements Comparator<String> {
    @Override
    public int compare(final String s, final String t1) {
      final LocalDate date1 = LocalDate.parse(s);
      final LocalDate date2 = LocalDate.parse(t1);
      return date1.compareTo(date2);
    }
  }

  /*
  public static void main(final String[] args) throws IOException {
    Gson gson = new Gson();
    final String content = "{\"respectConfinement\":50," +
      "\"mask\":{\"m0_15\":false,\"m16_19\":false,\"m30_49\":false," +
      "\"m50_69\":false,\"m70\":false},\"conf\":{\"c0_15\":false," +
      "\"c16_19\":false,\"c30_49\":false,\"c50_69\":false,\"c70\":false}}";
    Map map = gson.fromJson(content, Map.class);
    LinkedTreeMap<String, Boolean> test =
      (LinkedTreeMap<String, Boolean>) map.get(
        "mask");
    System.out.println(map.get("mask"));
    System.out.println(test.get("m0_15").toString());

  }
*/
}


