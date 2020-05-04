package model.service;

import model.data.DayData;
import model.data.TypeLocalisation;
import model.project.ProjectDataWrapper;
import model.project.ProjectDataWrapperImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;

public class DayDataServiceTest {

  private final static TypeLocalisation TYPE = TypeLocalisation.DEPARTEMENT;
  private final static LocalDate DATE = LocalDate.ofEpochDay(4);
  private final static String DATE1 = "2020-04-10";
  private final static String DATE2 = "2020-04-11";
  private final static String ID = "DEP-44";
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
  private static final double POPULATION_FRA = 67000000.0;
  private static final double SUSCEPTIBLE =
    (POPULATION_FRA - TOTAL_CASE) / POPULATION_FRA;
  private static final int INFECTED_DAY1 =
    TOTAL_CASE - RECOVERD_CASES - TOTAL_DEATHS;
  private static final int DEAD_DAY1 = TOTAL_DEATHS_1 - TOTAL_DEATHS;
  private static final int RECOVERED_DAY1 = RECOVERD_CASES_1 - RECOVERD_CASES;
  private static final double DEATH_RATE = (double) DEAD_DAY1 / INFECTED_DAY1;
  private static final double RECOVERY_RATE =
    (double) RECOVERED_DAY1 / INFECTED_DAY1;

  private static ProjectDataWrapper wrapper = new ProjectDataWrapperImpl();
  final static double delta = 0.001;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @BeforeClass
  public static void setUp() {
    dayData = new DayData(TYPE, LocalDate.parse(DATE1), ID, NAME, TOTAL_CASE,
      EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES,
      TOTAL_TEST);
    dayData1 = new DayData(TYPE, LocalDate.parse(DATE2), ID, NAME,
      TOTAL_CASE_1, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS_1,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES_1,
      TOTAL_TEST);
    wrapper.addKey(ID);
    wrapper.addLocation(ID, DATE1, dayData);
    wrapper.addLocation(ID, DATE2, dayData1);
  }

  @Test
  public void testGetDeathRate_validCall_correctResult() {
    // Arrange

    // Act
    final double result = DayDataService.getDeathRate(dayData1, wrapper, ID);

    // Assert
    Assert.assertEquals("wrong death rate", DEATH_RATE, result, delta);
  }

  @Test
  public void testGetRecoveryRate_validCall_correctResult() {
    // Arrange

    // Act
    final double result = DayDataService.getRecoveryRate(dayData1, wrapper, ID);

    // Assert
    Assert.assertEquals("wrong death rate", RECOVERY_RATE, result, delta);
  }

  @Test
  public void testGetDeathRate_dayDataNull_throwsIllegalArgumentException() {
    // Arrange
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("dayData null");

    // Act
    DayDataService.getDeathRate(null, wrapper, ID);

    // Assert -> via annotation
  }

  @Test
  public void testGetDeathRate_wrapperNull_throwsIllegalArgumentException() {
    // Arrange
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("wrapper null");

    // Act
    DayDataService.getDeathRate(dayData, null, ID);

    // Assert -> via annotation
  }

  @Test
  public void testGetDeathRate_locationNull_throwsIllegalArgumentException() {
    // Arrange
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("location null");

    // Act
    DayDataService.getDeathRate(dayData, wrapper, null);

    // Assert -> via annotation
  }

  @Test
  public void testGetDeathRate_locationEmpty_throwsIllegalArgumentException() {
    // Arrange
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("location empty");

    // Act
    DayDataService.getDeathRate(dayData, wrapper, EMPTY_STRING);

    // Assert -> via annotation
  }

  @Test
  public void testGetRecoveryRate_locationEmpty_throwsIllegalArgumentException() {
    // Arrange
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("location empty");

    // Act
    DayDataService.getRecoveryRate(dayData, wrapper, EMPTY_STRING);

    // Assert -> via annotation
  }

  @Test
  public void testGetRecoveryRate_wrapperNull_throwsIllegalArgumentException() {
    // Arrange
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("wrapper null");

    // Act
    DayDataService.getRecoveryRate(dayData, null, ID);

    // Assert -> via annotation
  }

  @Test
  public void testGetRecoveryRate_locationNull_throwsIllegalArgumentException() {
    // Arrange
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("location null");

    // Act
    DayDataService.getRecoveryRate(dayData, wrapper, null);

    // Assert -> via annotation
  }

  @Test
  public void testGetRecoveryRate_dayDataNull_throwsIllegalArgumentException() {
    // Arrange
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("dayData null");

    // Act
    DayDataService.getRecoveryRate(null, wrapper, ID);

    // Assert -> via annotation
  }

  @Test
  public void testGetSusceptible_dayDataNull_throwsIllegalArgumentException() {
    // Arrange
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("dayData null");

    // Act
    DayDataService.getSusceptible(null);

    // Assert -> via annotation
  }

  @Test
  public void testGetSusceptible_validCall_correctCalculation() {
    // Arrange

    // Act
    final double result = DayDataService.getSusceptible(dayData);

    // Assert -> via annotation
    Assert.assertEquals("wrong susceptible", SUSCEPTIBLE, result, 0);

  }

  @Test
  public void testGetDeathRate_validCall_correctCalculation() {
    // Arrange

    // Act
    //final double result = DayDataService.getDeathRate(subject);

    // Assert -> via annotation
    //  Assert.assertEquals("wrong deathRate", DEATH_RATE, result, 0);
  }

  @Test
  public void testGetRecoveryRate_validCall_correctCalculation() {
    // Arrange

    // Act
    //final double result = DayDataService.getRecoveryRate(subject);

    // Assert -> via annotation
    // Assert.assertEquals("wrong recoveryRate", RECOVERY_RATE, result, 0);
  }
}
