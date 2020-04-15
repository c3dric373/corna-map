package model.io;

import model.data.DayData;
import model.project.ProjectData;

import java.io.IOException;
import java.util.List;

public interface DataScrapper {

  List<DayData> getCurrentDataFrance() throws IOException;

}
