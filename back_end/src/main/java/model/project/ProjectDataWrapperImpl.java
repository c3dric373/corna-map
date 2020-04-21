package model.project;

import lombok.Getter;
import model.data.DayData;
import model.io.DataScrapperImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class acts as a wrapper for the {@link ProjectData} object. It offers
 * convenient methods to add, modify and remove project to all
 * project types that are stored inside the ProjectData object.
 * Most methods making changes to the
 * project return a value which will be passed on to  the View about those
 * changes.
 */
@Getter
public class ProjectDataWrapperImpl implements ProjectDataWrapper {

  ProjectDataImpl projectData = new ProjectDataImpl();

  private static final String FRA = "FRA";

  @Override
  public void getCurrentAllDataFrance() throws IOException {
    DataScrapperImpl dataScrapper = new DataScrapperImpl();
    dataScrapper.getCurrentDataFromWeb();
    dataScrapper.extract(this);
  }

  @Override
  public void addLocalisation(final String localisation, final String date,
                              final DayData dayData) {
    Map<String, Map<String, DayData>> localisations =
      projectData.getLocalisations();
    Map<String, DayData> info = localisations.get(localisation);
    info.put(date, dayData);
  }

  @Override
  public DayData infosFrance(final String date) {
    Map<String, Map<String, DayData>> localisations =
      projectData.getLocalisations();
    Map<String, DayData> tmp = localisations.get(FRA);
    return tmp.get(date);
  }

  @Override
  public List<DayData> infosRegion(String name) {
    return null;
  }

  @Override
  public DayData infosRegion(String name, String date) {
    return null;
  }

  @Override
  public void addKey(String key) {
    Map<String, Map<String, DayData>> localisations =
      projectData.getLocalisations();
    localisations.put(key, new HashMap<>());
  }

}


