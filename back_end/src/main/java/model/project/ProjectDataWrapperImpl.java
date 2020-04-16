package model.project;

import model.data.DayData;
import model.data.TypeLocalisation;
import model.io.DataScrapperImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProjectDataWrapperImpl implements ProjectDataWrapper {

  @Override
  public ProjectDataImpl getCurrentAllDataFrance() throws IOException {
    ProjectDataImpl rawData = new ProjectDataImpl();
    DataScrapperImpl dataScrapper = new DataScrapperImpl();
    dataScrapper.getCurrentDataFromWeb();
    BufferedReader br = new BufferedReader(new FileReader("back_end/src/main" +
      "/java/model/data/output.csv"));
    String line;
    while ((line = br.readLine()) != null) {
      String[] row = line.split(",");
      if (row[0].equals("date")) {
        continue;
      }
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
      LocalDate date = LocalDate.parse(row[0]);
      LocalDate today = LocalDate.now().minusDays(2);
      if ((row[1].equals("pays") || row[1].equals("departement") || row[1].equals("region")) && date.equals(today)) {
        extraction(rawData, row);
      }

    }
    return rawData;
  }

  private void extraction(final ProjectDataImpl rawData, final String[] row) {

    for (int i = 0; i < row.length; i++) {
      if (row[i].equals("")) {
        row[i] = "0";
      }

    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
    LocalDate date = LocalDate.parse(row[0]);
    if (row.length <= 6) {
      return;
    }
    DayData dayData =
      new DayData(TypeLocalisation.valueOf(row[1].toUpperCase()), date,
        row[2], row[3], Integer.parseInt(row[4]), Integer.parseInt(row[5]),
        Integer.parseInt(row[6]), Integer.parseInt(row[7]),
        Integer.parseInt(row[8]), Integer.parseInt(row[9]),
        Integer.parseInt(row[10]), Integer.parseInt(row[11]),
        Integer.parseInt(row[12]), Integer.parseInt(row[13]));
    switch (row[1]) {
      case "pays":
        rawData.france.add(dayData);
        break;
      case "departement":
        rawData.county.add(dayData);
        break;
      case "region":
        rawData.region.add(dayData);
        break;
      default:
        break;
    }

  }
}
