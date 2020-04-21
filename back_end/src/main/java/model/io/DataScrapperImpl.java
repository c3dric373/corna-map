package model.io;

import model.data.DayData;
import model.data.TypeLocalisation;
import model.project.ProjectDataWrapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Class which holds methods to get information about the coronavirus from
 * the web.
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
  private static final String PATH_TO_DATA = System.getProperty("user.dir")
    + "/src/main"
    + "/resources"
    + "/output.csv";

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
    File file = new File(PATH_TO_DATA);
    try {
      FileWriter csvOutput = new FileWriter(file);
      csvOutput.write(output);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public void extract(final ProjectDataWrapper projectDataWrapper) throws IOException {
    final int dateCsvIndex = 0;
    final int typeIndex = 1;
    final int minusDays = 3;
    final String pathToData = System.getProperty("user.dir")
      + "/src/main"
      + "/resources"
      + "/output.csv";
    BufferedReader br = new BufferedReader(new FileReader(
      pathToData));
    String line;
    int i = 0;
    while ((line = br.readLine()) != null) {
      String[] row = line.split(",");
      if (row[dateCsvIndex].equals("date")) {
        continue;
      }
      i++;
      if (i== 150){
        int j = 0;
      }
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
      LocalDate date = LocalDate.parse(row[dateCsvIndex]);
      LocalDate today = LocalDate.now().minusDays(minusDays);
      if ((row[typeIndex].equals("pays") || row[typeIndex].equals(
        "departement") || row[typeIndex].equals("region"))
        && date.equals(today)) {
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
    final DayData dayData =
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

    rawData.addKey(row[idIndex]);
    rawData.addLocalisation(row[idIndex], row[dateCsvIndex], dayData);
  }
}
