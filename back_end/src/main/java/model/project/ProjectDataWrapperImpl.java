package model.project;

import com.google.common.collect.Iterables;
import lombok.Getter;
import model.data.DayData;
import model.io.DataScrapperImpl;
import model.service.DayDataService;
import model.simulator.SIRSimulator;

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
  private ProjectData projectData = new ProjectDataImpl();

  @Override
  public void getCurrentAllDataFrance() throws IOException {
    final DataScrapperImpl dataScrapper = new DataScrapperImpl();
    dataScrapper.getCurrentDataFromWeb();
    dataScrapper.extract(this);
  }

  @Override
  public void addLocation(final String location, final String date,
                          final DayData dayData) {
    projectData.getLocations().get(location).put(date, dayData);
  }

  @Override
  public DayData infosFrance(final String date) {
    final Map<String, Map<String, DayData>> localisations =
      projectData.getLocations();
    final Map<String, DayData> tmp = localisations.get(FRA);
    return tmp.get(date);
  }

  @Override
  public List<DayData> historyLocalisation(final String name) {
    final Map<String, Map<String, DayData>> localisations =
      projectData.getLocations();
    final Map<String, DayData> tmp = localisations.get(name);
    return new ArrayList<>(tmp.values());
  }

  @Override
  public DayData infosLocalisation(final String name, final String date) {
    final Map<String, Map<String, DayData>> localisations =
      projectData.getLocations();
    final Map<String, DayData> tmp = localisations.get(name);
    return tmp.get(date);
  }

  @Override
  public List<DayData> infosRegion(final String date) {
    final Map<String, Map<String, DayData>> locations =
      projectData.getLocations().entrySet().stream().filter(map -> map.getKey()
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
      projectData.getLocations().entrySet().stream().filter(
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
    final Map<String, Map<String, DayData>> localisations =
      projectData.getLocations();
    localisations.computeIfAbsent(key, k -> new HashMap<>());
  }

  @Override
  public DayData simulateFrance(final String date) {
    final DayData latestData = getLatestData(FRA);
    LocalDate latestDate = latestData.getDate();
    // We need to check if the date is in the future or the past
    // if it's in the past we only return the data of the asked day and delete
    // the days coming after
    if (latestDate.isAfter(LocalDate.parse(date))) {
      truncateData(date);
      return getLatestData(FRA);
    }
    DayData dayData = new DayData();
    while (!LocalDate.parse(date).equals(latestDate)) {
      final double dead = DayDataService.getDeathRate(latestData);
      final double recovered = DayDataService.getRecoveryRate(latestData);
      final double susceptible = DayDataService.getSusceptible(latestData);
      final double infectious = 1 - susceptible;
      System.out.println("Dead: " + dead);
      System.out.println("recovered: " + recovered);
      System.out.println("susceptible: " + susceptible);
      System.out.println("infectious: " + infectious);
      final SIRSimulator sirSimulator = new SIRSimulator(susceptible,
        infectious,
        recovered,
        dead);
      dayData = simulateDay(latestData, sirSimulator);
      dayData.setDate(latestDate.plusDays(1));
      addLocation(FRA, latestDate.plusDays(1).toString(), dayData);
      latestDate = latestDate.plusDays(1);
      System.out.println("updated date0" + latestDate);
    }

    return dayData;
  }

  /**
   * Deletes all the entries after a specific date.
   *
   * @param date the specific date.
   */
  private void truncateData(final String date) {
    final Map<String, Map<String, DayData>> localisations =
      projectData.getLocations();
    final Map<String, DayData> dataFrance = localisations.get(FRA);
    dataFrance.keySet().removeIf(key
      -> LocalDate.parse(key).isAfter(LocalDate.parse(date)));
  }

  /**
   * Simulates a the spread of COVID-19 for one day, according to a given
   * simulator.
   *
   * @param startState The data on the situation form which the simulated day
   *                   should start.
   * @param simulator  the given simulator.
   * @return the simulated data.
   */
  private DayData simulateDay(final DayData startState,
                              final SIRSimulator simulator) {
    // Compute Start State
    final double totalDeaths = startState.getTotalDeaths();
    final double totalCases = startState.getTotalCases();
    final double lethality = totalDeaths / totalCases;
    simulator.setMu(lethality);
    // Simulate a day
    simulator.step();
    final double deadNew = Iterables.getLast(simulator.getDead());
    final double recoveredNew = Iterables.getLast(simulator.getRecovered());
    final double susceptibleNew =
      Iterables.getLast(simulator.getSusceptible());
    final double infectiousNew =
      Iterables.getLast(simulator.getInfectious());
    // Create Object which encapsulates the simulated data
    final DayData dayData = new DayData();
    dayData.setTotalDeaths((int) (deadNew * POPULATION_FRA));
    dayData.setRecoveredCases((int) (recoveredNew * POPULATION_FRA));
    dayData.setTotalCases((int) (POPULATION_FRA
      - (susceptibleNew * POPULATION_FRA)));

    System.out.println("------------------");
    System.out.println("deadNew: " + deadNew * POPULATION_FRA);
    System.out.println("recoveredNew: " + recoveredNew * POPULATION_FRA);
    System.out.println("susceptibleNew: " + susceptibleNew);
    System.out.println("infectiousNew: " + infectiousNew);
    System.out.println("totalCases: " + (POPULATION_FRA - (susceptibleNew *
      POPULATION_FRA)));
    return dayData;
  }

  /**
   * Get's the latest available data (covid-19 stats) for a specific location.
   *
   * @param location The location for which we want the data.
   * @return the data.
   */
  private DayData getLatestData(final String location) {
    final Map<String, Map<String, DayData>> localisations =
      projectData.getLocations();
    final Map<String, DayData> franceMap = localisations.get(FRA);
    final Optional<String> latestDate =
      franceMap.keySet().stream().max(dateComparator);
    if (!latestDate.isPresent()) {
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

  public static void main(String[] args) throws IOException {
    ProjectDataWrapper wrapper = new ProjectDataWrapperImpl();
    DataScrapperImpl scrapper = new DataScrapperImpl();
    scrapper.extract(wrapper);
    DayData dayData = wrapper.simulateFrance("2020-04-29");
    wrapper.simulateFrance("2020-04-30");
  }

}


