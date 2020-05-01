package model.project;

import model.data.DayData;
import model.data.TypeLocalisation;
import model.io.DataScrapper;
import model.io.DataScrapperImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.time.LocalDate;

public class ProjectDataWrapperImplTest {

  private final static TypeLocalisation TYPE = TypeLocalisation.DEPARTEMENT;
  private final static LocalDate DATE1 = LocalDate.ofEpochDay(4);
  private final static LocalDate DATE2 = LocalDate.ofEpochDay(5);
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
  private final static String FRA = "FRA";

  private static ProjectDataWrapperImpl subject =
    new ProjectDataWrapperImpl();

  private static final String PATH_TO_DATA = System.getProperty("user.dir")
    + "/src/test"
    + "/resources"
    + "/output.csv";

  private static final DataScrapper scrapper = new DataScrapperImpl();

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @BeforeClass
  public static void setUp() throws IOException {
    scrapper.setPathToData(PATH_TO_DATA);
    scrapper.extract(subject);
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
    Assert.assertEquals("wong DayData returned", expectedDayData, result);

  }

}
