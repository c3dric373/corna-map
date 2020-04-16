package model.io;


import model.data.DayData;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataScrapperImpl implements DataScrapper {

    /**
     * This procedure download the csv file directly from the french government official dataset
     * Read error @throws IOException
     */
    @Override
    public void getCurrentDataFromWeb() throws IOException {

        //GET Request to get the csv file as a string object
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://raw.githubusercontent.com/opencovid19-fr/data/master/dist/chiffres-cles.csv")
                .build();
        Response response = client.newCall(request).execute();
        String output = Objects.requireNonNull(response.body()).string();

        //We create a csv file from the string object then we read and parse the csv file to fill the DayData Array
        File file = new File("back_end/src/main/java/model/data/output.csv");
        try {
            FileWriter csvOutput = new FileWriter(file);
            csvOutput.write(output);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
