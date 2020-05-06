package model.project;

import com.google.gson.Gson;
import lombok.Getter;
import model.data.DayData;
import model.io.DataScrapperImpl;
import model.service.AgeCategoryService;
import model.service.DayDataService;
import model.service.SimulatorService;
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
  private SJYHRSimulator simulator = new SJYHRSimulator();

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

  @Override
  public void getCurrentAllDataFrance() throws IOException {
    final DataScrapperImpl dataScrapper = new DataScrapperImpl();
    dataScrapper.getCurrentDataFromWeb();
    dataScrapper.extract(this);
  }

  @Override
  public void startSimulation(final String content) {
    setSimulator(content);
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
      return getLatestData(FRA);
    }
    Map<String, Double> locationPercentages =
      DayDataService.getLocationPercentages(this.project.getLocations(),
        latestDate.toString());
    DayData dayData = new DayData();
    while (!LocalDate.parse(date).equals(latestDate)) {
      // Simulate a day
      dayData = simulateDay(latestData, simulator);
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
      latestData = dayData;
    }

    return dayData;
  }

  /**
   * Sets the parameter on the simulator with the latest data.
   *
   * @param content measures values
   */
  private void setSimulator(final String content) {
    DayData latestData = getLatestData(FRA);
    final List<Double> susceptibleComplex =
      DayDataService.getSusceptibleSJYHR(latestData);
    List<Double> lightInfected =
      DayDataService.getLightInfectedSJYHR(latestData);
    List<Double> heavyInfected =
      DayDataService.getHeavyInfectedSJYHR(latestData);
    simulator = new SJYHRSimulator(susceptibleComplex, lightInfected,
      heavyInfected);
    final Gson gson = new Gson();
    final Map map = gson.fromJson(content, Map.class);
    List<List<Integer>> measures = simulatorService.getMeasures(map);
    final int confinedCategoriesIndex = 0;
    final int maskedCategoriesIndex = 1;
    final int confinementRespectIndex = 2;
    simulator.applyMeasures(measures.get(confinedCategoriesIndex),
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
   * @param startState     The data on the situation form which the simulated
   *                       day
   *                       should start.
   * @param sjyhrSimulator the given sjyhrSimulator.
   * @return the simulated data.
   */
  private DayData simulateDay(final DayData startState,
                              final SJYHRSimulator sjyhrSimulator) {
    // Simulate a day
    sjyhrSimulator.step();
    final DayData result = new DayData();
    final List<SJYHRSimulator.AgeCategory> ageCategories =
      sjyhrSimulator.getAgeCategories();
    final double dead = ageCategoryService.getDead(ageCategories);
    for(SJYHRSimulator.AgeCategory ageCategory : ageCategories){
      System.out.println("DeadAgeCat: " + ageCategory.getDi());
    }
    System.out.println("Dead: " + dead * POPULATION_FRA);
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


