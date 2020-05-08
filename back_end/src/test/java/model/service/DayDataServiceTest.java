package model.service;

import model.data.DayData;
import model.project.ProjectDataWrapper;
import model.project.ProjectDataWrapperImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DayDataServiceTest {

  private final static String DATE1 = "2020-04-10";
  private final static String DATE2 = "2020-04-11";
  private final static String DEP_44 = "DEP-44";
  private final static String DEP_45 = "DEP-45";
  private final static String REG_11 = "REG-11";
  private final static String REG_12 = "REG-12";
  private final static String FRA = "FRA";
  private final static String NAME = "guadeloupe";
  private final static int TOTAL_CASE_1 = 1000;
  private final static int TOTAL_CASE_2 = 1100;

  private final static int TOTAL_CASE_REG_11_1 = 500;
  private final static int TOTAL_CASE_DEP_44_1 = TOTAL_CASE_1;
  private final static int TOTAL_CASE_REG_12_1 = 500;
  private final static int TOTAL_CASE_DEP_45_1 = 400;
  private final static double PERCENTAGE_REG_11_1 =
    (double) TOTAL_CASE_REG_11_1 / TOTAL_CASE_1;
  private final static double PERCENTAGE_DEP_44_1 =
    (double) TOTAL_CASE_DEP_44_1 / TOTAL_CASE_1;
  private final static double PERCENTAGE_REG_12_1 = (double)
    TOTAL_CASE_REG_12_1 / TOTAL_CASE_1;
  private final static double PERCENTAGE_DEP_45_1 = (double)
    TOTAL_CASE_DEP_45_1 / TOTAL_CASE_1;

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
  private static DayData dayData;

  private static final ProjectDataWrapper wrapper =
    new ProjectDataWrapperImpl();

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @BeforeClass
  public static void setUp() {

    dayData = new DayData( LocalDate.parse(DATE1), DEP_44, NAME,
      TOTAL_CASE_1,
      EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES,
      TOTAL_TEST);
    DayData dayData1 = new DayData( LocalDate.parse(DATE2), DEP_44, NAME,
      TOTAL_CASE_DEP_44_1, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS_1,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES_1,
      TOTAL_TEST);
    DayData dayData2 = new DayData( LocalDate.parse(DATE1), FRA, NAME,
      TOTAL_CASE_1, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS_1,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES_1,
      TOTAL_TEST);
    DayData dayData3 = new DayData( LocalDate.parse(DATE2), FRA, NAME,
      TOTAL_CASE_2, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS_1,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES_1,
      TOTAL_TEST);
    DayData dayData4 = new DayData( LocalDate.parse(DATE1), REG_11, NAME,
      TOTAL_CASE_REG_11_1, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS_1,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES_1,
      TOTAL_TEST);
    DayData dayData5 = new DayData( LocalDate.parse(DATE1), REG_12, NAME,
      TOTAL_CASE_REG_12_1, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS_1,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES_1,
      TOTAL_TEST);
    DayData dayData6 = new DayData( LocalDate.parse(DATE1), REG_12, NAME,
      TOTAL_CASE_DEP_45_1, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS_1,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES_1,
      TOTAL_TEST);
    wrapper.addKey(DEP_44);
    wrapper.addKey(DEP_45);
    wrapper.addKey(FRA);
    wrapper.addKey(REG_12);
    wrapper.addKey(REG_11);
    wrapper.addLocation(FRA, DATE1, dayData2);
    wrapper.addLocation(REG_11, DATE1, dayData4);
    wrapper.addLocation(REG_12, DATE1, dayData5);
    wrapper.addLocation(DEP_44, DATE1, dayData);
    wrapper.addLocation(DEP_45, DATE1, dayData6);
    wrapper.addLocation(DEP_44, DATE2, dayData1);
    wrapper.addLocation(FRA, DATE2, dayData3);

  }

  @Test
  public void testComputeTotalCases_validCall_correctResult() {
    // Arrange
    final int expected = EPHAD_CONFIRMED_CASES + TOTAL_DEATHS
      + TOTAL_EPHAD_DEATHS + CRITICAL_CASES + HOSPITALIZED + RECOVERD_CASES;

    // Act
    final int result = DayDataService.computeTotalCases(dayData);

    // Assert
    Assert.assertEquals("wrong computation of total cases", expected,
      result);
  }

  @Test
  public void testGetLocationPercentages_validCall_correctResult() {
    // Arrange 
    Map<String, Double> expectedMap = new HashMap<>();
    expectedMap.put(REG_11, PERCENTAGE_REG_11_1);
    expectedMap.put(REG_12, PERCENTAGE_REG_12_1);
    expectedMap.put(FRA, 1.);
    expectedMap.put(DEP_44, PERCENTAGE_DEP_44_1);
    expectedMap.put(DEP_45, PERCENTAGE_DEP_45_1);

    // Act
    Map<String, Double> actualMap =
      DayDataService.getLocationPercentages(wrapper.getProject().getLocations(),
        DATE1);

    // Assert
    Assert.assertEquals("wrong map", expectedMap, actualMap);

  }

  @Test
  public void testGetDeathRate_validCall_correctResult() {
    // Arrange

    // Act
    //final double result = DayDataService.getDeathRateSIR(dayData1);

    // Assert
    //Assert.assertEquals("wrong death rate", DEATH_RATE, result, delta);
  }

  @Test
  public void testGetRecoveryRate_validCall_correctResult() {
    // Arrange

    // Act
    // final double result = DayDataService.getRecoveryRateSIR(dayData1);

    // Assert
    //Assert.assertEquals("wrong death rate", RECOVERY_RATE, result, delta);
  }

  @Test
  public void testGetDeathRate_dayDataNull_throwsIllegalArgumentException() {
    // Arrange
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("dayData null");

    // Act
    DayDataService.getDeathRateSIR(null);

    // Assert -> via annotation
  }

  @Test
  public void
  testComputeTotalCases_dayDataNull_throwsIllegalArgumentException() {
    // Arrange
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("dayData null");

    // Act
    DayDataService.computeTotalCases(null);

    // Assert -> via annotation
  }

  @Test
  public void
  testGetLocationsPercentage_locationsNull_throwsIllegalArgumentException() {
    // Arrange
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("locations null");

    // Act
    DayDataService.getLocationPercentages(null, DATE1);

    // Assert -> via annotation
  }

  @Test
  public void
  testGetLocationsPercentage_locationsEmpty_throwsIllegalArgumentException() {
    // Arrange
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("locations empty");
    final Map<String, Map<String, DayData>> emptyMap = new HashMap<>();

    // Act
    DayDataService.getLocationPercentages(emptyMap, DATE1);

    // Assert -> via annotation
  }

  @Test
  public void
  testGetLocationsPercentage_dateEmpty_throwsIllegalArgumentException() {
    // Arrange
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("date empty");
    final Map<String, Map<String, DayData>> map =
      wrapper.getProject().getLocations();

    // Act
    DayDataService.getLocationPercentages(map, EMPTY_STRING);

    // Assert -> via annotation
  }

  @Test
  public void
  testGetLocationsPercentage_dayDataNull_throwsIllegalArgumentException() {
    // Arrange
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("date null");
    final Map<String, Map<String, DayData>> map =
      wrapper.getProject().getLocations();
    // Act
    DayDataService.getLocationPercentages(map, null);

    // Assert -> via annotation
  }

  @Test
  public void testGetRecoveryRate_dayDataNull_throwsIllegalArgumentException() {
    // Arrange
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("dayData null");

    // Act
    DayDataService.getRecoveryRateSIR(null);

    // Assert -> via annotation
  }

}
