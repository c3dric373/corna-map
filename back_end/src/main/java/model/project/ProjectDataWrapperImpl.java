package model.project;

import model.data.DayData;
import model.data.TypeLocalisation;
import model.io.DataScrapperImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * This class acts as a wrapper for the {@link ProjectData} object. It offers
 * convenient methods to add, modify and remove project to all
 * project types that are stored inside the ProjectData object.
 * Most methods making changes to the
 * project return a value which will be passed on to  the View about those
 * changes.
 */
public class ProjectDataWrapperImpl implements ProjectDataWrapper {

  @Override
  public ProjectDataImpl getCurrentAllDataFrance() throws IOException {
    final int dateCsvIndex = 0;
    final int typeIndex = 1;
    final int minusDays = 2;
    final String pathToData = "/home/c3dric/Uni/Studium/master"
      + "/ss20/smart"
      + "/back_end/src/main"
      + "/java"
      + "/model"
      + "/data"
      + "/output.csv";
    ProjectDataImpl rawData = new ProjectDataImpl();
    DataScrapperImpl dataScrapper = new DataScrapperImpl();
    dataScrapper.getCurrentDataFromWeb();
    BufferedReader br = new BufferedReader(new FileReader(
      pathToData));
    String line;
    while ((line = br.readLine()) != null) {
      String[] row = line.split(",");
      if (row[dateCsvIndex].equals("date")) {
        continue;
      }
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
      LocalDate date = LocalDate.parse(row[dateCsvIndex]);
      LocalDate today = LocalDate.now().minusDays(minusDays);
      if ((row[typeIndex].equals("pays") || row[typeIndex].equals(
        "departement") || row[typeIndex].equals("region"))
        && date.equals(today)) {
        extraction(rawData, row);
      }

    }
    return rawData;
  }

  /**
   * Extract Data.
   *
   * @param rawData the projectData we want to create.
   * @param row     the data to add to the project.
   */
  private void extraction(final ProjectDataImpl rawData, final String[] row) {
    final int dateCsvIndex = 0;
    final int typeIndex = 1;
    final int idIndex = 2;
    final int nameIndex = 3;
    final int totalCasesIndex = 4;
    final int ephadCasesIndex = 5;
    final int ephadConfirmedCasesIndex = 6;
    final int ephadPossibleCasesIndex = 7;
    final int totalDeathsIndex = 8;
    final int totalEphadDeathsIndex = 9;
    final int criticalCasesIndex = 10;
    final int hospitalizedIndex = 11;
    final int recoveredCasesIndex = 12;
    final int totalTestsIndex = 13;
    final int maxLength = 6;
    for (int i = 0; i < row.length; i++) {
      if (row[i].equals("")) {
        row[i] = "0";
      }

    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
    LocalDate date = LocalDate.parse(row[dateCsvIndex]);
    if (row.length <= maxLength) {
      return;
    }
    DayData dayData =
      new DayData(TypeLocalisation.valueOf(row[typeIndex].toUpperCase()), date,
        row[idIndex], row[nameIndex], Integer.parseInt(row[totalCasesIndex]),
        Integer.parseInt(row[ephadCasesIndex]),
        Integer.parseInt(row[ephadConfirmedCasesIndex]),
        Integer.parseInt(row[ephadPossibleCasesIndex]),
        Integer.parseInt(row[totalDeathsIndex]),
        Integer.parseInt(row[totalEphadDeathsIndex]),
        Integer.parseInt(row[criticalCasesIndex]),
        Integer.parseInt(row[hospitalizedIndex]),
        Integer.parseInt(row[recoveredCasesIndex]),
        Integer.parseInt(row[totalTestsIndex]));
    switch (row[typeIndex]) {
      case "pays":
        rawData.getFrance().add(dayData);
        break;
      case "departement":
        rawData.getCounty().add(dayData);
        break;
      case "region":
        rawData.getRegion().add(dayData);
        break;
      default:
        break;
    }

  }
}
