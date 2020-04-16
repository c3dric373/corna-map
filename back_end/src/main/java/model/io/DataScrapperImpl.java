package model.io;


import au.com.bytecode.opencsv.CSVWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jdk.nashorn.internal.parser.JSONParser;
import model.data.DayData;
import model.data.TypeLocalisation;
import model.project.ProjectData;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.h2.util.json.JSONObject;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DataScrapperImpl implements DataScrapper {


    @Override
    public List<DayData> getCurrentDataFrance() throws IOException {
        List<DayData> rawData = new ArrayList<>();
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

        BufferedReader br = new BufferedReader(new FileReader("back_end/src/main/java/model/data/output.csv"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] row = line.split(",");
            if (row[0].equals("date")) {
                continue;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
            LocalDate date = LocalDate.parse(row[0]);
            if (row[1].equals("pays") && date.equals(java.time.LocalDate.now().minusDays(1))) {

                extraction(rawData, row);
            }

        }
        return rawData;
    }

    @Override
    public List<DayData> getCurrentDataRegions() throws IOException {
        List<DayData> rawData = new ArrayList<>();

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

        BufferedReader br = new BufferedReader(new FileReader("back_end/src/main/java/model/data/output.csv"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] row = line.split(",");
            if (row[0].equals("date")) {
                continue;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
            LocalDate date = LocalDate.parse(row[0]);
            if (row[1].equals("region") && date.equals(java.time.LocalDate.now().minusDays(1))) {

                extraction(rawData, row);
            }

        }
        return rawData;
    }

    @Override
    public List<DayData> getCurrentDataFDepartements() throws IOException {
        List<DayData> rawData = new ArrayList<>();

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

        BufferedReader br = new BufferedReader(new FileReader("back_end/src/main/java/model/data/output.csv"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] row = line.split(",");
            if (row[0].equals("date")) {
                continue;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
            LocalDate date = LocalDate.parse(row[0]);
            if (row[1].equals("departement") && date.equals(java.time.LocalDate.now().minusDays(1))) {

                extraction(rawData, row);
            }

        }
        return rawData;
    }

    private void extraction(List<DayData> rawData, String[] row) {
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
        DayData dayData = new DayData(TypeLocalisation.valueOf(row[1].toUpperCase()), date, row[2], row[3], Integer.parseInt(row[4]), Integer.parseInt(row[5]), Integer.parseInt(row[6]), Integer.parseInt(row[7]), Integer.parseInt(row[8]), Integer.parseInt(row[9]), Integer.parseInt(row[10]), Integer.parseInt(row[11]), Integer.parseInt(row[12]), Integer.parseInt(row[13]));
        rawData.add(dayData);
    }
}
