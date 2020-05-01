package model.service;

import model.data.DayData;
import model.data.TypeLocalisation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;

public class DayDataServiceTest {

  private final static TypeLocalisation TYPE = TypeLocalisation.DEPARTEMENT;
  private final static LocalDate DATE = LocalDate.ofEpochDay(4);
  private final static String ID = "DEP-44";
  private final static String NAME = "guadeloupe";
  private final static int TOTAL_CASE = 1000;
  private final static int EPHAD_CASES = 2;
  private final static int EPHAD_CONFIRMED_CASES = 5;
  private final static int EPHAD_POSSIBLE_CASES = 4;
  private final static int TOTAL_DEATHS = 3;
  private final static int TOTAL_EPHAD_DEATHS = 7;
  private final static int CRITICAL_CASES = 8;
  private final static int HOSPITALIZED = 9;
  private final static int RECOVERD_CASES = 66;
  private final static int TOTAL_TEST = 6;
  private final static String EMPTY_STRING = "";
  private final static int NEGATIVE_NUMBER = -1;
  private static DayData subject;
  private static final double POPULATION_FRA = 67000000.0;
  private static final double SUSCEPTIBLE =
    (POPULATION_FRA - TOTAL_CASE) / POPULATION_FRA;
  private static final double DEATH_RATE = TOTAL_DEATHS / POPULATION_FRA;
  private static final double RECOVERY_RATE =
    RECOVERD_CASES / (double) TOTAL_CASE;
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @BeforeClass
  public static void setUp() {
    subject = new DayData(TYPE, DATE, ID, NAME, TOTAL_CASE, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES,
      TOTAL_TEST);
  }

  @Test
  public void testGetDeathRate_dayDataNull_throwsIllegalArgumentException() {
    // Arrange
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("dayData null");

    // Act
    //DayDataService.getDeathRate(null);

    // Assert -> via annotation
  }

  @Test
  public void testGetRecoveryRate_dayDataNull_throwsIllegalArgumentException() {
    // Arrange
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("dayData null");

    // Act
   // DayDataService.getRecoveryRate(null,);

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
    final double result = DayDataService.getSusceptible(subject);

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
