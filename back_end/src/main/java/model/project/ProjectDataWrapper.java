package model.project;

import model.data.DayData;

import java.io.IOException;
import java.util.List;

/**
 * This class acts as a wrapper for the {@link ProjectData} object. It offers
 * convenient methods to add, modify and remove project to all
 * project types that are stored inside the ProjectData object.
 * Most methods making changes to the
 * project return a value which will be passed on to  the View about those
 * changes.
 */
public interface ProjectDataWrapper {

  /**
   * Get all data from france.
   *
   * @return the data.
   * @throws IOException read error.
   */
  void getCurrentAllDataFrance() throws IOException;

  //ProjectData getProjectData();

  void addLocalisation(final String localisation, final String date,
                       final DayData dayData);

  DayData infosFrance(final String date);

  List<DayData> infosRegion(final String name);

  DayData infosRegion(final String name, final String date);

  void addKey(final String key);
}
