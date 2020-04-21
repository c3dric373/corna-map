package model.project;

import lombok.Getter;
import model.data.DayData;
import model.io.DataScrapperImpl;

import java.io.IOException;
import java.util.ArrayList;
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
    final DataScrapperImpl dataScrapper = new DataScrapperImpl();
    dataScrapper.getCurrentDataFromWeb();
    dataScrapper.extract(this);
    int i = 0;
  }

  @Override
  public void addLocalisation(final String localisation, final String date,
                              final DayData dayData) {
    projectData.getLocalisations().get(localisation).put(date, dayData);
  }

  @Override
  public DayData infosFrance(final String date) {
    final Map<String, Map<String, DayData>> localisations =
      projectData.getLocalisations();
    final Map<String, DayData> tmp = localisations.get(FRA);
    return tmp.get(date);
  }

  @Override
  public List<DayData> infosLocalisation(final String name) {
    final Map<String, Map<String, DayData>> localisations =
      projectData.getLocalisations();
    final Map<String, DayData> tmp = localisations.get(name);
    System.out.println(tmp.values());
    return new ArrayList<>(tmp.values());
  }

  @Override
  public DayData infosLocalisation(final String name, final String date) {
    final Map<String, Map<String, DayData>> localisations =
      projectData.getLocalisations();
    final Map<String, DayData> tmp = localisations.get(name);
    return tmp.get(date);
  }

  @Override
  public void addKey(final String key) {
    final Map<String, Map<String, DayData>> localisations =
      projectData.getLocalisations();
    localisations.computeIfAbsent(key, k -> new HashMap<>());
  }
}


