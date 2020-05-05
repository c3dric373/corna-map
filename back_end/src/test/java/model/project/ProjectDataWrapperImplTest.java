package model.project;

import model.data.DayData;
import model.data.TypeLocalisation;
import model.io.DataScrapper;
import model.io.DataScrapperImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjectDataWrapperImplTest {

  private final static TypeLocalisation TYPE = TypeLocalisation.DEPARTEMENT;
  private final static String DATE1 = "2020-04-10";
  private final static String DATE2 = "2020-04-11";
  private final static String DEP_44 = "DEP-44";
  private final static String DEP_45 = "DEP-45";
  private final static String REG_11 = "REG-11";
  private final static String REG_12 = "REG-12";
  private final static String FRA = "FRA";
  private final static String NAME = "guadeloupe";
  private final static int TOTAL_CASE = 1000;
  private final static int TOTAL_CASE_1 = 1100;
  private final static int EPHAD_CASES = 2;
  private final static int EPHAD_CONFIRMED_CASES = 5;
  private final static int EPHAD_POSSIBLE_CASES = 4;
  private final static int TOTAL_DEATHS = 3;
  private final static int TOTAL_DEATHS_1 = 10;
  private final static int TOTAL_EPHAD_DEATHS = 7;
  private final static int CRITICAL_CASES = 8;
  private final static int HOSPITALIZED = 9;
  private final static int RECOVERED_CASES = 66;
  private final static int RECOVERED_CASES_1 = 99;
  private final static int TOTAL_TEST = 6;
  private static DayData dayData;
  private static DayData dayData1;
  private static DayData dayData2;
  private static DayData dayData3;
  private static DayData dayData4;
  private static DayData dayData5;
  private static DayData dayData6;

  private static ProjectDataWrapperImpl subject =
    new ProjectDataWrapperImpl();
  private static ProjectDataWrapperImpl customWrapper =
    new ProjectDataWrapperImpl();

  private static ProjectDataWrapperImpl wrapper = new ProjectDataWrapperImpl();

  private static final String PATH_TO_DATA = System.getProperty("user.dir")
    + "/src/test"
    + "/resources"
    + "/output.csv";

  private static final DataScrapper scrapper = new DataScrapperImpl();

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() throws IOException {
    scrapper.setPathToData(PATH_TO_DATA);
    scrapper.extract(subject);
    dayData = new DayData(TYPE, LocalDate.parse(DATE1), DEP_44, NAME,
      TOTAL_CASE,
      EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERED_CASES,
      TOTAL_TEST);
    dayData1 = new DayData(TYPE, LocalDate.parse(DATE2), DEP_44, NAME,
      TOTAL_CASE_1, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS_1,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERED_CASES_1,
      TOTAL_TEST);
    dayData2 = new DayData(TYPE, LocalDate.parse(DATE1), FRA, NAME,
      TOTAL_CASE_1, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS_1,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERED_CASES_1,
      TOTAL_TEST);
    dayData3 = new DayData(TYPE, LocalDate.parse(DATE2), FRA, NAME,
      TOTAL_CASE_1, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS_1,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERED_CASES_1,
      TOTAL_TEST);
    dayData4 = new DayData(TYPE, LocalDate.parse(DATE1), REG_11, NAME,
      TOTAL_CASE_1, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS_1,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERED_CASES_1,
      TOTAL_TEST);
    dayData5 = new DayData(TYPE, LocalDate.parse(DATE1), REG_12, NAME,
      TOTAL_CASE_1, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS_1,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERED_CASES_1,
      TOTAL_TEST);
    dayData6 = new DayData(TYPE, LocalDate.parse(DATE1), REG_12, NAME,
      TOTAL_CASE_1, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS_1,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERED_CASES_1,
      TOTAL_TEST);
    customWrapper.addKey(DEP_44);
    customWrapper.addKey(DEP_45);
    customWrapper.addKey(FRA);
    customWrapper.addKey(REG_12);
    customWrapper.addKey(REG_11);
    customWrapper.addLocation(DEP_44, DATE1, dayData);
    customWrapper.addLocation(DEP_45, DATE1, dayData6);
    customWrapper.addLocation(DEP_44, DATE2, dayData1);
    customWrapper.addLocation(FRA, DATE1, dayData2);
    customWrapper.addLocation(FRA, DATE2, dayData3);
    customWrapper.addLocation(REG_11, DATE1, dayData4);
    customWrapper.addLocation(REG_12, DATE1, dayData5);
  }

  @Test
  public void testSimulateFrance_dateBeforeLatestDate_correctDayDataReturn() {
    // Arrange
    final String dateBeforeLatest = "2020-04-26";
    final DayData expectedDayData =
      subject.getProject().getLocations().get(FRA).get(dateBeforeLatest);

    // Act
    final DayData result = subject.simulateFrance(dateBeforeLatest);

    // Assert
    Assert.assertEquals("wrong DayData returned", expectedDayData, result);

  }

  @Test
  public void testInfosFrance_validCall_correctResult() {
    // Arrange

    // Act
    final DayData result = customWrapper.infosFrance(DATE1);

    // Assert
    Assert.assertEquals("wrong dayData", dayData2, result);
  }

  @Test
  public void testSimulateFrance_sim5daysInFuture_totalCasesNonZero() throws IOException {
    // Arrange
    LocalDate dateIn5Days = LocalDate.now().plusDays(5);
    wrapper.getCurrentAllDataFrance();
    wrapper.startSimulation();
    System.out.println(dateIn5Days);
    final DayData dayData =
      wrapper.simulateFrance(dateIn5Days.toString());
    final int unexpected = 0;

    // Act
    final int result = dayData.getTotalCases();

    // Assert
    Assert.assertNotEquals("totalCases Zero", unexpected, result);
  }

  @Test
  public void testSimulateFrance_simHistory_correctDayData() throws IOException {
    // Arrange
    LocalDate datePast = LocalDate.now().minusDays(10);
    wrapper.getCurrentAllDataFrance();
    wrapper.startSimulation();
    final DayData expected =
      wrapper.getProject().getLocations().get(FRA).get(datePast.toString());

    // Act
    final DayData result = wrapper.simulateFrance(datePast.toString());
    ;

    // Assert
    Assert.assertEquals("wrong DayData", expected, result);
  }

  @Test
  public void testSimulateFrance_simHistorySameDayTwice_totalCasesNotZero()
    throws IOException {
    // Arrange
    String datePast = "2020-03-19";
    wrapper.getCurrentAllDataFrance();
    wrapper.startSimulation();
    wrapper.simulateFrance(datePast);
    final DayData dayDataResult = wrapper.simulateFrance(datePast);
    final int unexpected = 0;

    // Act
    final int result = dayDataResult.getTotalCases();

    // Assert
    Assert.assertNotEquals("totalCases Zero", unexpected, result);
  }

  @Test
  public void testSimulateFrance_simHistoryBeforeFuture_totalCasesNonZero() throws IOException {
    // Arrange
    LocalDate datePast = LocalDate.now().minusDays(10);
    LocalDate dateFuture = LocalDate.now().plusDays(5);

    wrapper.getCurrentAllDataFrance();
    wrapper.startSimulation();
    wrapper.simulateFrance(datePast.toString());
    final DayData dayDataRes =
      wrapper.simulateFrance(dateFuture.toString());
    final int unexpected = 0;

    // Act
    final int result = dayDataRes.getTotalCases();

    // Assert
    Assert.assertNotEquals("totalCases Zero", unexpected, result);
  }

  @Test
  public void testSimulateFrance_simHistoryBeforeFutureTwice_totalCasesNonZero() throws IOException {
    // Arrange
    final String datePast = LocalDate.now().minusDays(10).toString();
    final String datePast1 = LocalDate.now().minusDays(15).toString();
    final String dateFuture = LocalDate.now().plusDays(5).toString();
    final String dateFuture1 = LocalDate.now().plusDays(7).toString();

    wrapper.getCurrentAllDataFrance();
    wrapper.startSimulation();
    wrapper.simulateFrance(datePast);
    wrapper.simulateFrance(dateFuture);
    wrapper.simulateFrance(datePast1);
    final DayData dayDataRes = wrapper.simulateFrance(dateFuture1);
    final int unexpected = 0;

    // Act
    final int result = dayDataRes.getTotalCases();

    // Assert
    Assert.assertNotEquals("totalCases Zero", unexpected, result);
  }

  @Test
  public void testSimulateFrance_simFutureTrice_totalCasesNonZero() throws IOException {
    // Arrange
    final String dateFuture = LocalDate.now().plusDays(5).toString();
    final String dateFuture1 = LocalDate.now().minusDays(10).toString();
    final String dateFuture2 = LocalDate.now().plusDays(15).toString();

    wrapper.getCurrentAllDataFrance();
    wrapper.startSimulation();
    wrapper.simulateFrance(dateFuture);
    wrapper.simulateFrance(dateFuture1);
    final DayData dayDataRes = wrapper.simulateFrance(dateFuture2);
    final int unexpected = 0;

    // Act
    final int result = dayDataRes.getTotalCases();

    // Assert
    Assert.assertNotEquals("totalCases Zero", unexpected, result);
  }

  @Test
  public void testSimulateFrance_simFutureTriceWithInfoInBetween_totalCasesNonZero() throws IOException {
    // Arrange
    final String dateFuture = LocalDate.now().plusDays(5).toString();
    final String dateFuture1 = LocalDate.now().minusDays(10).toString();
    final String dateFuture2 = LocalDate.now().plusDays(15).toString();

    wrapper.getCurrentAllDataFrance();
    wrapper.startSimulation();
    wrapper.simulateFrance(dateFuture);
    // Info
    wrapper.infosFrance(DATE1);
    // Simulate second time
    wrapper.startSimulation();
    wrapper.simulateFrance(dateFuture1);
    // Info
    wrapper.infosFrance(DATE1);
    // Simulate second time
    wrapper.startSimulation();
    final DayData dayDataRes = wrapper.simulateFrance(dateFuture2);
    final int unexpected = 0;

    // Act
    final int result = dayDataRes.getTotalCases();

    // Assert
    Assert.assertNotEquals("totalCases Zero", unexpected, result);
  }

  @Test
  public void
  testSimulateFrance_simHistoryBeforeFutureTwiceWithInfoInBetween_totalCasesNonZero() throws IOException {
    // Arrange
    final String datePast = LocalDate.now().minusDays(10).toString();
    final String datePast1 = LocalDate.now().minusDays(15).toString();
    final String dateFuture = LocalDate.now().plusDays(5).toString();
    final String dateFuture1 = LocalDate.now().plusDays(7).toString();

    wrapper.getCurrentAllDataFrance();
    wrapper.startSimulation();
    wrapper.simulateFrance(datePast);
    wrapper.simulateFrance(dateFuture);
    // Info
    wrapper.infosFrance(DATE1);
    // Simulate second time
    wrapper.startSimulation();
    wrapper.simulateFrance(datePast1);
    final DayData dayDataRes = wrapper.simulateFrance(dateFuture1);
    final int unexpected = 0;

    // Act
    final int result = dayDataRes.getTotalCases();

    // Assert
    Assert.assertNotEquals("totalCases Zero", unexpected, result);
  }

  @Test
  public void testSimulateFrance_simPastThenInfoThenSimFuture_totalCasesNonZero() throws IOException {
    // Arrange
    LocalDate datePast = LocalDate.now().minusDays(10);
    LocalDate dateFuture = LocalDate.now().plusDays(5);

    wrapper.getCurrentAllDataFrance();
    wrapper.startSimulation();
    // Simulate past
    wrapper.simulateFrance(datePast.toString());
    // Get Info
    wrapper.infosFrance(DATE1);
    // Simulate Future
    wrapper.startSimulation();
    final DayData dayData =
      wrapper.simulateFrance(dateFuture.toString());
    final int unexpected = 0;

    // Act
    final int result = dayData.getTotalCases();

    // Assert
    Assert.assertNotEquals("totalCases Zero", unexpected, result);
  }

  @Test
  public void testHistoryLocation_validCallFranceLocation_correctResult() {
    // Arrange
    final List<DayData> expected = new ArrayList<>(2);
    expected.add(dayData3);
    expected.add(dayData2);
    // Act
    final List<DayData> result = customWrapper.historyLocation(FRA);

    // Assert
    Assert.assertEquals("wrong dayData", expected, result);
  }

  @Test
  public void testHistoryLocation_validCallDeptLocation_correctResult() {
    // Arrange
    final List<DayData> expected = new ArrayList<>(2);
    expected.add(dayData1);
    expected.add(dayData);
    // Act
    final List<DayData> result = customWrapper.historyLocation(DEP_44);

    // Assert
    Assert.assertEquals("wrong dayData", expected, result);
  }

  @Test
  public void testInfosLocation_validCallDeptLocation_correctResult() {
    // Arrange

    // Act
    final DayData result = customWrapper.infosLocation(DEP_44, DATE1);

    // Assert
    Assert.assertEquals("wrong dayData", dayData, result);
  }

  @Test
  public void testInfosRegion_validCall_correctResult() {
    // Arrange
    final List<DayData> expected = new ArrayList<>(2);
    expected.add(dayData5);
    expected.add(dayData4);
    // Act
    final List<DayData> result = customWrapper.infosRegion(DATE1);

    // Assert
    Assert.assertEquals("wrong dayData", expected, result);
  }

  @Test
  public void testInfosDept_validCall_correctResult() {
    // Arrange
    final List<DayData> expected = new ArrayList<>(2);
    expected.add(dayData);
    expected.add(dayData6);
    // Act
    final List<DayData> result = customWrapper.infosDept(DATE1);

    // Assert
    Assert.assertEquals("wrong dayData", expected, result);
  }

  @Test
  public void testGetLatestData_validCallFranceLocation_correctResult() {
    // Arrange
    final List<DayData> expected = new ArrayList<>(2);
    expected.add(dayData);
    expected.add(dayData6);
    // Act
    final List<DayData> result = customWrapper.infosDept(DATE1);

    // Assert
    Assert.assertEquals("wrong dayData", expected, result);
  }

}
