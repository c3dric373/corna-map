package model.data;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class DayDataTest {

  private final static LocalDate DATE = LocalDate.ofEpochDay(4);
  private final static String ID = "DEP-44";
  private final static String NAME = "guadeloupe";
  private final static int TOTAL_CASE = 1;
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

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @BeforeClass
  public static void setUp() {
    subject = new DayData(DATE, ID, NAME, TOTAL_CASE, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES,
      TOTAL_TEST);
  }

  @Test
  public void testEmptyCTOR_validCall_correctDayData() {
    // Arrange
    final int intInitValue = 0;
    final DayData expectedDayData = new DayData(DATE,
      ID, NAME, intInitValue, intInitValue, intInitValue,
      intInitValue, intInitValue, intInitValue, intInitValue, intInitValue,
      intInitValue, intInitValue);
    expectedDayData.setDate(null);
    expectedDayData.setId(null);
    expectedDayData.setName(null);
    // Act
    final DayData dayData = new DayData();

    // Assert
    assertEquals("DayData not equal", dayData, expectedDayData);

  }
  
  @Test
  public void testCTOR_dateNull_throwsIllegalArgumentException() {

    // Arrange
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("dateNew null");

    // Act
    new DayData( null, ID, NAME, TOTAL_CASE, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES,
      TOTAL_TEST);

    // Assert via Annotation
  }

  @Test
  public void testCTOR_iDNull_throwsIllegalArgumentException() {

    // Arrange
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("idNew null");

    // Act
    new DayData( DATE, null, NAME, TOTAL_CASE, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES,
      TOTAL_TEST);

    // Assert via Annotation
  }

  @Test
  public void testCTOR_iDEmpty_throwsIllegalArgumentException() {

    // Arrange
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("idNew empty");

    // Act
    new DayData( DATE, EMPTY_STRING, NAME, TOTAL_CASE, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES,
      TOTAL_TEST);

    // Assert via Annotation
  }

  @Test
  public void testCTOR_nameEmpty_throwsIllegalArgumentException() {

    // Arrange
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("nomNew empty");

    // Act
    new DayData( DATE, ID, EMPTY_STRING, TOTAL_CASE, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES,
      TOTAL_TEST);

    // Assert via Annotation
  }

  @Test
  public void testCTOR_totalCaseNegative_throwsIllegalArgumentException() {

    // Arrange
    thrown.expect(IllegalArgumentException.class);

    // Act
    new DayData( DATE, ID, NAME, NEGATIVE_NUMBER, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES,
      TOTAL_TEST);

    // Assert via Annotation
  }

  @Test
  public void testCTOR_ephadCasesNegative_throwsIllegalArgumentException() {

    // Arrange
    thrown.expect(IllegalArgumentException.class);

    // Act
    new DayData( DATE, ID, NAME, TOTAL_CASE, NEGATIVE_NUMBER,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES,
      TOTAL_TEST);

    // Assert via Annotation
  }

  @Test
  public void
  testCTOR_ephadConfirmedCaseNegative_throwsIllegalArgumentException() {

    // Arrange
    thrown.expect(IllegalArgumentException.class);

    // Act
    new DayData( DATE, ID, NAME, TOTAL_CASE, EPHAD_CASES,
      NEGATIVE_NUMBER, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES,
      TOTAL_TEST);

    // Assert via Annotation
  }

  @Test
  public void
  testCTOR_ephadPossibleCaseNegative_throwsIllegalArgumentException() {

    // Arrange
    thrown.expect(IllegalArgumentException.class);

    // Act
    new DayData( DATE, ID, NAME, TOTAL_CASE, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, NEGATIVE_NUMBER, TOTAL_DEATHS,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES,
      TOTAL_TEST);

    // Assert via Annotation
  }

  @Test
  public void testCTOR_totalDeathsNegative_throwsIllegalArgumentException() {

    // Arrange
    thrown.expect(IllegalArgumentException.class);

    // Act
    new DayData( DATE, ID, NAME, TOTAL_CASE, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, NEGATIVE_NUMBER,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES,
      TOTAL_TEST);

    // Assert via Annotation
  }

  @Test
  public void
  testCTOR_totalEphadDeathsNegative_throwsIllegalArgumentException() {

    // Arrange
    thrown.expect(IllegalArgumentException.class);

    // Act
    new DayData( DATE, ID, NAME, TOTAL_CASE, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS,
      NEGATIVE_NUMBER, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES,
      TOTAL_TEST);

    // Assert via Annotation
  }

  @Test
  public void testCTOR_criticalCaseNegative_throwsIllegalArgumentException() {

    // Arrange
    thrown.expect(IllegalArgumentException.class);

    // Act
    new DayData( DATE, ID, NAME, TOTAL_CASE, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS,
      TOTAL_EPHAD_DEATHS, NEGATIVE_NUMBER, HOSPITALIZED, RECOVERD_CASES,
      TOTAL_TEST);

    // Assert via Annotation
  }

  @Test
  public void
  testCTOR_hospitalizedCaseNegative_throwsIllegalArgumentException() {

    // Arrange
    thrown.expect(IllegalArgumentException.class);

    // Act
    new DayData( DATE, ID, NAME, TOTAL_CASE, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, NEGATIVE_NUMBER, RECOVERD_CASES,
      TOTAL_TEST);

    // Assert via Annotation
  }

  @Test
  public void testCTOR_recoveredCaseNegative_throwsIllegalArgumentException() {

    // Arrange
    thrown.expect(IllegalArgumentException.class);

    // Act
    new DayData( DATE, ID, NAME, TOTAL_CASE, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, NEGATIVE_NUMBER,
      TOTAL_TEST);

    // Assert via Annotation
  }

  @Test
  public void testCTOR_totalTestNegative_throwsIllegalArgumentException() {

    // Arrange
    thrown.expect(IllegalArgumentException.class);

    // Act
    new DayData( DATE, ID, NAME, TOTAL_CASE, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES,
      NEGATIVE_NUMBER);

    // Assert via Annotation
  }

  @Test
  public void testCTOR_validCall_correctDayData() {

    // Arrange
    thrown.expect(IllegalArgumentException.class);

    // Act
    final DayData result = new DayData( DATE, ID, NAME, TOTAL_CASE,
      EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES,
      NEGATIVE_NUMBER);

    // Assert via Annotation
    Assert.assertEquals(subject, result);
  }

  @Test
  public void testGetDate_validCall_correctRes() {

    // Arrange

    // Act
    final LocalDate result = subject.getDate();

    // Assert
    assertEquals("Incorrect Date!", result, DATE);
  }

  @Test
  public void testGetId_validCall_correctRes() {

    // Arrange

    // Act
    final String result = subject.getId();

    // Assert
    assertEquals("Incorrect Id!", result, ID);
  }

  @Test
  public void testGetName_validCall_correctRes() {

    // Arrange

    // Act
    final String result = subject.getName();

    // Assert
    assertEquals("Incorrect name!", result, NAME);
  }

  @Test
  public void testGetCriticalCases_validCall_correctRes() {

    // Arrange

    // Act
    final int result = subject.getCriticalCases();

    // Assert
    assertEquals("Incorrect CriticalCases!", result, CRITICAL_CASES);
  }

  @Test
  public void testGetEphadCases_validCall_correctRes() {

    // Arrange

    // Act
    final int result = subject.getEphadCases();

    // Assert
    assertEquals("Incorrect EphadCases!", result, EPHAD_CASES);
  }

  @Test
  public void testGetEphadPossibleCases_validCall_correctRes() {

    // Arrange

    // Act
    final int result = subject.getEphadPossibleCases();

    // Assert
    assertEquals("Incorrect EphadPossibleCases!", result, EPHAD_POSSIBLE_CASES);
  }

  @Test
  public void testGetEphadConfirmedCases_validCall_correctRes() {

    // Arrange

    // Act
    final int result = subject.getEphadConfirmedCases();

    // Assert
    assertEquals("Incorrect EphadConfirmedCases!", result,
      EPHAD_CONFIRMED_CASES);
  }

  @Test
  public void testGetHospitalized_validCall_correctRes() {

    // Arrange

    // Act
    final int result = subject.getHospitalized();

    // Assert
    assertEquals("Incorrect Hospitalized!", result, HOSPITALIZED);
  }

  @Test
  public void testGetRecoveredCases_validCall_correctRes() {

    // Arrange

    // Act
    final int result = subject.getRecoveredCases();

    // Assert
    assertEquals("Incorrect RecoveredCases!", result, RECOVERD_CASES);
  }

  @Test
  public void testTotalCases_validCall_correctRes() {

    // Arrange

    // Act
    final int result = subject.getTotalCases();

    // Assert
    assertEquals("Incorrect TotalCases!", result, TOTAL_CASE);
  }

  @Test
  public void testTotalDeaths_validCall_correctRes() {

    // Arrange

    // Act
    final int result = subject.getTotalDeaths();

    // Assert
    assertEquals("Incorrect TotalDeaths!", result, TOTAL_DEATHS);
  }

  @Test
  public void testTotalEphadDeaths_validCall_correctRes() {

    // Arrange

    // Act
    final int result = subject.getTotalEphadDeaths();

    // Assert
    assertEquals("Incorrect TotalEphadDeaths!", result, TOTAL_EPHAD_DEATHS);
  }

  @Test
  public void testTotalTests_validCall_correctRes() {

    // Arrange

    // Act
    final int result = subject.getTotalTests();

    // Assert
    assertEquals("Incorrect TotalTests!", result, TOTAL_TEST);
  }

  @Test
  public void testEqualsAndHashCode_allCases_noErrors() {

    // Arrange

    // Act
    EqualsVerifier.forClass(DayData.class);

    // Assert
  }
}
