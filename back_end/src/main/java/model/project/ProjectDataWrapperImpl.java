package model.project;

import com.google.common.collect.Iterables;
import lombok.Getter;
import model.data.DayData;
import model.io.DataScrapperImpl;
import model.service.DayDataService;
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
  private ProjectData project = new ProjectDataImpl();

  /**
   * Simulator used to simulate COVID-19.
   */
  private SIRSimulator sirSimulator = new SIRSimulator();

  /**
   * Simulator used to simulate COVID-19.
   */
  private SJYHRSimulator sjyhrSimulator = new SJYHRSimulator();

  /**
   * Dictionnary mapping id to name for regions and departments.
   */
  private Map<String, String> idToName = new HashMap<>();

  @Override
  public void getCurrentAllDataFrance() throws IOException {
    final DataScrapperImpl dataScrapper = new DataScrapperImpl();
    dataScrapper.getCurrentDataFromWeb();
    dataScrapper.extract(this);
  }

  @Override
  public void startSimulation() {
    setSirSimulator();
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
    Map<String, Double> locationPercentages =
      DayDataService.getLocationPercentages(this.project.getLocations(),
        latestDate.toString());
    // We need to check if the date is in the future or the past
    // if it's in the past we only return the data of the asked day and delete
    // the days coming after
    if (latestDate.isAfter(LocalDate.parse(date))
      || latestDate.equals(LocalDate.parse(date))) {
      truncateData(date);
      return getLatestData(FRA);
    }
    DayData dayData = new DayData();
    while (!LocalDate.parse(date).equals(latestDate)) {
      // Simulate a day
      dayData = simulateDay(latestData, sirSimulator);
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
   */
  private void setSirSimulator() {
    DayData latestData = getLatestData(FRA);
    final double deathRate = DayDataService.getDeathRateSIR(latestData,
      this, FRA);
    final double recoveryRate = DayDataService.getRecoveryRateSIR(latestData,
      this, FRA);
    final double susceptible = DayDataService.getSusceptibleSIR(latestData);
    final List<Double> susceptibleComplex =
      DayDataService.getSusceptibleSJYHR(latestData);
    List<Double> infectious = new ArrayList<>();
    for (double susceptiblePerAgeCat : susceptibleComplex) {
      infectious.add(1 - susceptiblePerAgeCat);
    }
    List<Double> lightInfected =
      DayDataService.getLightInfectedSJYHR(latestData);
    List<Double> heavyInfected =
      DayDataService.getHeavyInfectedSJYHR(latestData);

    sjyhrSimulator = new SJYHRSimulator(susceptibleComplex, lightInfected,
      heavyInfected);
    //simulator = new SIRSimulator(susceptible,
    // infectious, recoveryRate, deathRate);
  }

  /**
   * Deletes all the entries after a specific date.
   *
   * @param date the specific date.
   */
  private void truncateData(final String date) {
    final Map<String, Map<String, DayData>> locations =
      project.getLocations();
    final Map<String, DayData> dataFrance = locations.get(FRA);
    for (Map.Entry<String, Map<String, DayData>> entry : locations.entrySet()) {
      final String id = entry.getKey();
      final Map<String, DayData> mapId = entry.getValue();
      mapId.keySet().removeIf(key
        -> LocalDate.parse(key).isAfter(LocalDate.parse(date)));
    }
  }

  /**
   * Simulates a the spread of COVID-19 for one day, according to a given
   * simulator.
   *
   * @param startState   The data on the situation form which the simulated day
   *                     should start.
   * @param sirSimulator the given simulator.
   * @return the simulated data.
   */
  private DayData simulateDay(final DayData startState,
                              final SIRSimulator sirSimulator) {
    // Compute Start State
    final double totalDeaths = startState.getTotalDeaths();
    final double recovered = startState.getRecoveredCases();
    // Simulate a day
    sirSimulator.step();
    final double deadNew = Iterables.getLast(sirSimulator.getDead());
    final double recoveredNew = Iterables.getLast(sirSimulator.getRecovered());
    final double susceptibleNew =
      Iterables.getLast(sirSimulator.getSusceptible());
    final double infectiousNew =
      Iterables.getLast(sirSimulator.getInfectious());
    final double infectedPeople = POPULATION_FRA * infectiousNew;
    // Create Object which encapsulates the simulated data
    final DayData dayData = new DayData();
    dayData.setTotalDeaths((int) (totalDeaths + deadNew * infectedPeople));
    dayData.setRecoveredCases((int) (recovered
      + recoveredNew * infectedPeople));
    System.out.println("recovered " + dayData.getRecoveredCases());
    dayData.setTotalCases((int) (POPULATION_FRA
      - (susceptibleNew * POPULATION_FRA)));
    return dayData;
  }

  /**
   * Get's the latest available data (Covid-19 stats) for a specific location.
   *
   * @param location The location for which we want the data.
   * @return the data.
   */
  private DayData getLatestData(final String location) {
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
    ProjectDataWrapper wrapper = new ProjectDataWrapperImpl();
    DataScrapperImpl scrapper = new DataScrapperImpl();
    scrapper.extract(wrapper);
    DayData dayData = wrapper.simulateFrance("2020-04-30");
    System.out.println("=============");
    wrapper.simulateFrance("2020-05-06");
    //wrapper.simulateFrance("2020-05-01");
  }
  */

}


