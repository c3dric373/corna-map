package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import model.io.DataScrapper;
import model.io.DataScrapperImpl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import model.data.DayData;

@RestController
public class DayDataController
{
    @RequestMapping(value = "/testday", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    String index() throws IOException {
        DataScrapper testScrapper= new DataScrapperImpl();
        List<DayData> test =testScrapper.getCurrentDataRegions();
        Gson gson = new Gson();


        // 2. Java object to JSON string
        return gson.toJson(test);
    }
}