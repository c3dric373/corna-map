package model.project;

import model.data.DayData;
import model.data.TypeLocalisation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ProjectDataImplTest {

  private final static TypeLocalisation TYPE = TypeLocalisation.DEPARTEMENT;
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
  private static DayData dayData;

  private static final ProjectDataWrapper wrapper = new ProjectDataWrapperImpl();
  final static double delta = 0.001;
  private static ProjectData subject;
  private final HashMap<String, Map<String, DayData>> expectedMap =
    new HashMap<>();

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @BeforeClass
  public static void setUp() {
    dayData = new DayData(TYPE, LocalDate.parse(DATE1), ID, NAME, TOTAL_CASE,
      EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES,
      TOTAL_TEST);
    DayData dayData1 = new DayData(TYPE, LocalDate.parse(DATE2), ID, NAME,
      TOTAL_CASE_1, EPHAD_CASES,
      EPHAD_CONFIRMED_CASES, EPHAD_POSSIBLE_CASES, TOTAL_DEATHS_1,
      TOTAL_EPHAD_DEATHS, CRITICAL_CASES, HOSPITALIZED, RECOVERD_CASES_1,
      TOTAL_TEST);
    wrapper.addKey(ID);
    wrapper.addLocation(ID, DATE1, dayData);
    subject = wrapper.getProject();
  }

  @Test
  public void testGetLocations_validCall_correctLocations() {
    // Arrange
    final Map<String, DayData> tmp = new HashMap<>();
    tmp.put(DATE1, dayData);
    expectedMap.put(ID, tmp);

    // Act
    final Map<String, Map<String, DayData>> map = subject.getLocations();

    // Assert
    Assert.assertEquals("wrong map", expectedMap, map);
  }

}
