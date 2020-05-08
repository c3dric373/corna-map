package model.io;

import lombok.Setter;
import model.data.DayData;
import model.project.ProjectDataWrapper;
import model.service.DayDataService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Class which holds methods to get information about the coronavirus from the
 * web.
 */
public class DataScrapperImpl implements DataScrapper {
  /**
   * Url from which we will pull data.
   */
  private static final String DATA_URL = "https://raw.githubusercontent"
    + ".com/opencovid19-fr/data/master/dist/chiffres-cles.csv";

  /**
   * Path where the data will be stored.
   */
  @Setter
  private String pathToData = System.getProperty("user.dir")
    + "/src/main"
    + "/resources"
    + "/output.csv";

  /**
   * {@inheritDoc}
   */
  @Override
  public void getCurrentDataFromWeb() throws IOException {

    //GET Request to get the csv file as a string object
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
      .url(DATA_URL)
      .build();
    Response response = client.newCall(request).execute();
    String output = Objects.requireNonNull(response.body()).string();

    //We create a csv file from the string object then we read and parse the
    // csv file to fill the DayData Array
    final File file = new File(pathToData);
    try {
      FileWriter csvOutput = new FileWriter(file);
      csvOutput.write(output);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void extract(final ProjectDataWrapper projectDataWrapper)
    throws IOException {
    final int dateCsvIndex = 0;
    final int typeIndex = 1;
    final int minusDays = 3;
    final int totalCasesIndex = 4;
    final String newPathToData = System.getProperty("user.dir")
      + "/src/main"
      + "/resources"
      + "/output.csv";
    BufferedReader br = new BufferedReader(new FileReader(
      newPathToData));
    String line;
    int i = 0;
    while ((line = br.readLine()) != null) {
      String[] row = line.split(",");
      if (row[dateCsvIndex].equals("date")) {
        continue;
      }
      DateTimeFormatter formatter = DateTimeFormatter
        .ofPattern("yyyy-MMM-dd");
      LocalDate date = LocalDate.parse(row[dateCsvIndex]);
      LocalDate today = LocalDate.now().minusDays(minusDays);
      // Sometimes there are rows with the date only, we ignore them
      if ((row.length == 1)) {
        continue;
      }
      if ((row[typeIndex].equals("pays")
        && !row[totalCasesIndex].equals("")
        || row[typeIndex].equals("departement")
        || row[typeIndex].equals("region"))) {
        extraction(projectDataWrapper, row);
      }

    }
  }

  /**
   * Extract Data.
   *
   * @param rawData the projectData we want to create.
   * @param row     the data to add to the project.
   */
  private void extraction(final ProjectDataWrapper rawData,
                          final String[] row) {
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
    final int maxLength = 12;
    for (int i = 0; i < row.length; i++) {
      if (row[i].equals("")) {
        row[i] = "0";
      }

    }
    LocalDate date = LocalDate.parse(row[dateCsvIndex]);
    if (row.length <= maxLength) {
      return;
    }
    final DayData dayData =
      new DayData(date, row[idIndex], row[nameIndex],
        Integer.parseInt(row[totalCasesIndex]),
        Integer.parseInt(row[ephadCasesIndex]),
        Integer.parseInt(row[ephadConfirmedCasesIndex]),
        Integer.parseInt(row[ephadPossibleCasesIndex]),
        Integer.parseInt(row[totalDeathsIndex]),
        Integer.parseInt(row[totalEphadDeathsIndex]),
        Integer.parseInt(row[criticalCasesIndex]),
        Integer.parseInt(row[hospitalizedIndex]),
        Integer.parseInt(row[recoveredCasesIndex]),
        Integer.parseInt(row[totalTestsIndex]));
    if (dayData.getTotalCases() == 0) {
      dayData.setTotalCases(DayDataService.computeTotalCases(dayData));
    }
    rawData.addKey(row[idIndex]);
    rawData.addLocation(row[idIndex], row[dateCsvIndex], dayData);
  }
}
