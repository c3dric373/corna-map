package model.io;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
  private static final String PATH_TO_DATA = "/home/c3dric/Uni/Studium/master"
    + "/ss20/smart"
    + "/back_end/src/main"
    + "/java"
    + "/model"
    + "/data"
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
}
