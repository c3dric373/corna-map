package model.project;

import model.io.DataScrapper;
import model.io.DataScrapperImpl;
import org.junit.BeforeClass;

import java.io.IOException;

public class ProjectDataWrapperImplTest {

  private static ProjectDataWrapperImpl subject =
    new ProjectDataWrapperImpl();

  private static final String PATH_TO_DATA = System.getProperty("user.dir")
    + "/src/test"
    + "/resources"
    + "/output.csv";

  private static final DataScrapper scrapper = new DataScrapperImpl();

  @BeforeClass
  public static void setUp() throws IOException {
    scrapper.setPathToData(PATH_TO_DATA);
    scrapper.extract(subject);
  }


}
