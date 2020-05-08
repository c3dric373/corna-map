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
import org.apache.commons.lang.Validate;

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
public class ProjectDataWrapperImpl implements ProjectDataWrapper {

  /**
   * Id for france to access data.
   */
  private static final String FRA = "FRA";

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
  private final SJYHRSimulator sjyhrSimulator = new SJYHRSimulator();

  /**
   * {@link SIRSimulator} used to simulate COVID-19.
   */
  private SIRSimulator sirSimulator;

  /**
   * Dictionary mapping id to name for regions and departments.
   */
  private final Map<String, String> idToName = new HashMap<>();

  /**
   * Service used to compute numbers after simulating.
   */
  private final AgeCategoryService ageCategoryService =
    new AgeCategoryService();

  /**
   * Service used to compute numbers after simulating.
   */
  private final SimulatorService simulatorService = new SimulatorService();

  /**
   * Flag to know if we should use a SIRSimulator or not.
   */
  private boolean useSir = true;

  /**
   * Latest Content.
   */
  private String latestContent;

  /**
   * Region identifier.
   */
  private static final String REG = "REG";

  /**
   * County identifier.
   */
  private static final String DEP = "DEP";

  /**
   * {@inheritDoc}
   *
   * @throws IOException
   */
  @Override
  public void getCurrentAllDataFrance() throws IOException {
    final DataScrapperImpl dataScrapper = new DataScrapperImpl();
    dataScrapper.getCurrentDataFromWeb();
    dataScrapper.extract(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void startSimulation(final String content, final boolean sir) {
    Validate.notNull(content, "content null");
    Validate.notEmpty(content, "content empty");
    if (sir) {
      setSirSimulator(content);
      useSir = true;
      latestContent = content;
    } else {
      setSJYHRSimulator(content);
      latestContent = content;
      useSir = false;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addLocation(final String location, final String date,
                          final DayData dayData) {
    Validate.notNull(location, "location null");
    Validate.notEmpty(location, "location empty");
    Validate.notNull(date, "date null");
    Validate.notEmpty(date, "date empty");
    Validate.notNull(dayData, "dayData null");
    project.getLocations().get(location).put(date, dayData);
    idToName.put(dayData.getId(), dayData.getName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DayData infosFrance(final String date) {
    return getDayData(date, FRA);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<DayData> historyLocation(final String name) {
    Validate.notNull(name, "name null");
    Validate.notEmpty(name, "name empty");
    final Map<String, Map<String, DayData>> locations =
      project.getLocations();
    final Map<String, DayData> tmp = locations.get(name);
    return new ArrayList<>(tmp.values());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DayData infosLocation(final String name, final String date) {
    Validate.notNull(date, "date null");
    Validate.notEmpty(date, "date empty");
    return getDayData(date, name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<DayData> infosRegion(final String date) {
    Validate.notNull(date, "date null");
    Validate.notEmpty(date, "date empty");
    return getDayDataList(date, REG);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<DayData> infosDept(final String date) {
    Validate.notNull(date, "date null");
    Validate.notEmpty(date, "date empty");
    return getDayDataList(date, DEP);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addKey(final String key) {
    Validate.notNull(key, "key null");
    Validate.notEmpty(key, "key empty");
    final Map<String, Map<String, DayData>> locations =
      project.getLocations();
    locations.computeIfAbsent(key, k -> new HashMap<>());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DayData simulateFrance(final String date) {
    Validate.notNull(date, "date null");
    Validate.notEmpty(date, "date empty");
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
   * Get List of DayData at a specific date, For a given type of location(
   * region or county).
   *
   * @param date       the date for which we  should get the data.
   * @param locationId Id of the type of location.
   * @return List of {@link DayData} fitting the requirements (date and
   * location)
   */
  private List<DayData> getDayDataList(final String date,
                                       final String locationId) {
    Validate.notNull(date, "date null");
    Validate.notEmpty(date, "date empty");
    Validate.notNull(locationId, "locationId null");
    Validate.notEmpty(locationId, "locationId empty");
    final Map<String, Map<String, DayData>> locations =
      project.getLocations().entrySet().stream().filter(
        map -> map.getKey()
          .contains(locationId)).
        collect(Collectors.toMap(Map.Entry::getKey,
          Map.Entry::getValue));
    final List<DayData> res = new ArrayList<>(locations.size());
    for (Map<String, DayData> map : locations.values()) {
      res.add(map.get(date));
    }
    return res;
  }

  /**
   * Get specific {@link DayData} on specific date with a given location.
   *
   * @param date     specific date.
   * @param location given location.
   * @return the asked {@link DayData}.
   */
  private DayData getDayData(final String date, final String location) {
    Validate.notNull(date, "date null");
    Validate.notEmpty(date, "date empty");
    final Map<String, Map<String, DayData>> localisations =
      project.getLocations();
    final Map<String, DayData> tmp = localisations.get(location);
    return tmp.get(date);
  }

  /**
   * Sets the parameter on the simulator with the latest data.
   *
   * @param content content.
   */
  private void setSirSimulator(final String content) {
    Validate.notNull(content, "content null");
    Validate.notEmpty(content, "content empty");
    Validate.notNull(content, "content Null");
    DayData latestData = getLatestData(FRA);
    final List<Double> deathRate = DayDataService.getDeathRateSIR(latestData);
    final double sumDR = deathRate.stream().mapToDouble(f -> f).sum();
    final List<Double> recoveryRate =
      DayDataService.getRecoveryRateSIR(latestData);
    final double sumRR = recoveryRate.stream().mapToDouble(f -> f).sum();
    final List<Double> infectious = DayDataService.getInfectiousSir(latestData);
    final double sumI = infectious.stream().mapToDouble(f -> f).sum();
    final List<Double> susceptible =
      DayDataService.getSusceptibleSIR(sumDR + sumI + sumRR);
    //System.out.println("RecoveredBase:  " + recoveryRate * DayDataService
    // .POPULATION_FRA);
    sirSimulator = new SIRSimulator(susceptible,
      infectious, recoveryRate, deathRate);
    // Apply Measures
    List<List<Integer>> measures = getMeasures(content);
    sirSimulator.applyMeasures(measures);
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
    final double deadNew =
      sirSimulator.getDead().stream().mapToDouble(Iterables::getLast).sum();
    final double recoveredNew = sirSimulator.getRecovered().stream()
      .mapToDouble(Iterables::getLast).sum();
    final double susceptibleNew = sirSimulator.getSusceptible().stream()
      .mapToDouble(Iterables::getLast).sum();

    return DayDataService.setSIRDayData(deadNew, recoveredNew, susceptibleNew);
  }

  /**
   * Call {@link SimulatorService} to convert a String of measures into a
   * list of lists of integer.
   *
   * @param content The measures to apply as String.
   * @return the measures as a list of lists of integers.
   */
  private List<List<Integer>> getMeasures(final String content) {
    Validate.notNull(content, "content null");
    Validate.notEmpty(content, "content empty");
    final Gson gson = new Gson();
    final Map map = gson.fromJson(content, Map.class);
    return simulatorService.getMeasures(map);

  }

  /**
   * Deletes all the entries after a specific date.
   *
   * @param date the specific date.
   */
  private void truncateData(final String date) {
    Validate.notNull(date, "date null");
    Validate.notEmpty(date, "date empty");
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
   * {@link SJYHRSimulator}
   *
   * @return the simulated data.
   */
  private DayData simulateDaySJYHR() {
    // Simulate a day
    sjyhrSimulator.step();
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
    return DayDataService.setSJYHRDayData(dead, recovered, susceptible,
      hospitalized,
      heavyInfected);
  }

  /**
   * Sets the parameter on the simulator with the latest data.
   *
   * @param content measures values
   */
  private void setSJYHRSimulator(final String content) {
    Validate.notNull(content, "content null");
    Validate.notEmpty(content, "content empty");
    // Latest data
    DayData latestData = getLatestData(FRA);

    // Apply InitialState
    final List<Double> initS = DayDataService.getSusceptibleSJYHR(latestData);
    final List<Double> initI = DayDataService.getInfectedSJYHR(latestData,
      sjyhrSimulator);
    final List<Double> initJ = DayDataService.getLightInfectedSJYHR(initI,
      ageCategoryService);
    final List<Double> initY = DayDataService.getHeavyInfectedSJYHR(initI,
      ageCategoryService);
    final List<Double> initH = DayDataService.getHospitalizedSJYHR(initI,
      latestData);
    final List<Double> initD = DayDataService.getDeadSJYHR(latestData,
      initH, ageCategoryService);
    final List<Double> initR = DayDataService.getRecoveredSJYHR(latestData,
      initJ, initH);
    sjyhrSimulator.setInitialStates(initS, initJ, initY, initH, initR, initD);

    // Apply Measures
    final List<List<Integer>> measures = getMeasures(content);
    sjyhrSimulator.applyMeasures(measures);
  }

  /**
   * Get's the latest available data (Covid-19 stats) for a specific location.
   *
   * @param location The location for which we want the data.
   * @return the data.
   */
  public DayData getLatestData(final String location) {
    Validate.notNull(location, "location null");
    final Map<String, Map<String, DayData>> locations =
      project.getLocations();
    final Map<String, DayData> franceMap = locations.get(location);
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
}


