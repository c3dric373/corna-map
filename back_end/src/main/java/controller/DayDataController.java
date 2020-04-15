package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.io.DataScrapper;
import model.io.DataScrapperImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import model.data.DayData;

@RestController
public class DayDataController
{
    @RequestMapping("/testday")
    DayData index() throws IOException {
        DataScrapper testScrapper= new DataScrapperImpl();
        List<DayData> test =testScrapper.getCurrentDataFrance();
        int justepourrigoler=1;
        return test.get(0);
    }
}