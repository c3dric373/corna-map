package model.project;

import java.io.IOException;

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
  ProjectDataImpl getCurrentAllDataFrance() throws IOException;

  ProjectData getProjectData();

}
