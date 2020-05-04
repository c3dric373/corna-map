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
  private final static LocalDate DATE = LocalDate.ofEpochDay(4);
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
  private final static int RECOVERD_CASES = 66;
  private final static int RECOVERD_CASES_1 = 99;
  private final static int TOTAL_TEST = 6;
  private final static String EMPTY_STRING = "";
  private final static int NEGATIVE_NUMBER = -1;
  private static DayData dayData;
  private static DayData dayData1;
  private static DayData dayData2;
  private static DayData dayData3;
  private static DayData dayData4;
  private static DayData dayData5;
  private static DayData dayData6;

  private static ProjectDataWrapperImpl subject =
    new ProjectDataWrapperImpl();
  private static ProjectDataWrapperImpl wrapper =
    new ProjectDataWrapperImpl();

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
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES,
      TOTAL_TEST);
    dayData1 = new DayData(TYPE, LocalDate.parse(DATE2), DEP_44, NAME,
      TOTAL_CASE_1, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS_1,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES_1,
      TOTAL_TEST);
    dayData2 = new DayData(TYPE, LocalDate.parse(DATE1), FRA, NAME,
      TOTAL_CASE_1, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS_1,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES_1,
      TOTAL_TEST);
    dayData3 = new DayData(TYPE, LocalDate.parse(DATE2), FRA, NAME,
      TOTAL_CASE_1, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS_1,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES_1,
      TOTAL_TEST);
    dayData4 = new DayData(TYPE, LocalDate.parse(DATE1), REG_11, NAME,
      TOTAL_CASE_1, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS_1,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES_1,
      TOTAL_TEST);
    dayData5 = new DayData(TYPE, LocalDate.parse(DATE1), REG_12, NAME,
      TOTAL_CASE_1, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS_1,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES_1,
      TOTAL_TEST);
    dayData6 = new DayData(TYPE, LocalDate.parse(DATE1), REG_12, NAME,
      TOTAL_CASE_1, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS_1,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES_1,
      TOTAL_TEST);
    wrapper.addKey(DEP_44);
    wrapper.addKey(DEP_45);
    wrapper.addKey(FRA);
    wrapper.addKey(REG_12);
    wrapper.addKey(REG_11);
    wrapper.addLocation(DEP_44, DATE1, dayData);
    wrapper.addLocation(DEP_45, DATE1, dayData6);
    wrapper.addLocation(DEP_44, DATE2, dayData1);
    wrapper.addLocation(FRA, DATE1, dayData2);
    wrapper.addLocation(FRA, DATE2, dayData3);
    wrapper.addLocation(REG_11, DATE1, dayData4);
    wrapper.addLocation(REG_12, DATE1, dayData5);
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
    final DayData result = wrapper.infosFrance(DATE1);

    // Assert
    Assert.assertEquals("wrong dayData", dayData2, result);
  }

  @Test
  public void testHistoryLocation_validCallFranceLocation_correctResult() {
    // Arrange
    final List<DayData> expected = new ArrayList<>(2);
    expected.add(dayData3);
    expected.add(dayData2);
    // Act
    final List<DayData> result = wrapper.historyLocation(FRA);

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
    final List<DayData> result = wrapper.historyLocation(DEP_44);

    // Assert
    Assert.assertEquals("wrong dayData", expected, result);
  }

  @Test
  public void testInfosLocation_validCallDeptLocation_correctResult() {
    // Arrange

    // Act
    final DayData result = wrapper.infosLocation(DEP_44, DATE1);

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
    final List<DayData> result = wrapper.infosRegion(DATE1);

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
    final List<DayData> result = wrapper.infosDept(DATE1);

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
    final List<DayData> result = wrapper.infosDept(DATE1);

    // Assert
    Assert.assertEquals("wrong dayData", expected, result);
  }

}
