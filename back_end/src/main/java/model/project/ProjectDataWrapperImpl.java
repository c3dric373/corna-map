package model.project;

import com.google.common.collect.Iterables;
import lombok.Getter;
import model.data.DayData;
import model.io.DataScrapperImpl;
import model.simulator.SIRSimulator;
import model.simulator.Simulator;

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
   * The {@link ProjectData} this wrapper manages.
   */
  private ProjectData projectData = new ProjectDataImpl();

  /**
   * The {@link model.simulator.Simulator} to simulate the propagation of the
   * COVID-19.
   */
  private Simulator simulator = new SIRSimulator();
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
    final double dead = getDead(FRA);
    final double recovered = getRecovered(FRA);
    final double susceptible = getSusceptible(FRA);
    final double infectious = 1 - susceptible;
    System.out.println("Dead: " + dead);
    System.out.println("recovered: " + recovered);
    System.out.println("susceptible: " + susceptible);
    System.out.println("infectious: " + infectious);
    SIRSimulator simulator = new SIRSimulator(susceptible, infectious,
      recovered,
      dead);
    simulator.step();
    final double deadNew = Iterables.getLast(simulator.getDead());
    final double recoveredNew = Iterables.getLast(simulator.getRecovered());
    final double susceptibleNew = Iterables.getLast(simulator.getSusceptible());
    final double infectiousNew = Iterables.getLast(simulator.getInfectious());
    final DayData dayData = new DayData();
    dayData.setTotalDeaths((int) (deadNew * POPULATION_FRA));
    dayData.setRecoveredCases((int) (recovered * POPULATION_FRA));
    dayData.setTotalCases((int) (POPULATION_FRA - (susceptibleNew * POPULATION_FRA)));
    System.out.println("------------------");

    System.out.println("deadNew: " + deadNew);
    System.out.println("recoveredNew: " + recoveredNew);
    System.out.println("susceptibleNew: " + susceptibleNew);
    System.out.println("infectiousNew: " + infectiousNew);
    return dayData;
  }

  private DayData getLatestData(final String location) {
    final Map<String, Map<String, DayData>> localisations =
      projectData.getLocations();
    final Map<String, DayData> franceMap = localisations.get(FRA);
    final Optional<String> latestDate =
      franceMap.keySet().stream().max(dateComparator);
    if (latestDate.isEmpty()) {
      throw new IllegalStateException("No max date in map");
    }
    return franceMap.get(latestDate.get());
  }

  private double getSusceptible(final String location) {
    final DayData latestData = getLatestData(location);
    final int infectedPersons = latestData.getTotalCases()
      - latestData.getRecoveredCases() - latestData.getTotalDeaths();
    return infectedPersons / POPULATION_FRA;
  }

  private double getRecovered(final String location) {
    final DayData latestData = getLatestData(location);
    final double latestDataRecoveredCases = latestData.getRecoveredCases();
    final double latestDataTotalCases = latestData.getTotalCases();
    return latestDataRecoveredCases / latestDataTotalCases;
  }

  private double getDead(final String location) {
    final DayData latestData = getLatestData(location);
    final int latestDead = latestData.getTotalDeaths();
    return latestDead / POPULATION_FRA;
  }

  /**
   * Simple class to compare to string dates.
   */
  private static class DateComparator implements Comparator<String> {
    @Override
    public int compare(final String s, final String t1) {
      LocalDate date1 = LocalDate.parse(s);
      LocalDate date2 = LocalDate.parse(t1);
      return date1.compareTo(date2);
    }
  }

  public static void main(String[] args) throws IOException {
    ProjectDataWrapper wrapper = new ProjectDataWrapperImpl();
    wrapper.getCurrentAllDataFrance();
    DayData dayData = wrapper.simulateFrance("2020-04-28");
    int i = 0;
  }
}


