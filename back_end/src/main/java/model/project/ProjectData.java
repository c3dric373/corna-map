package model.project;

import model.data.DayData;

import java.util.Map;

/**
 * Interface for ProjectData. See {@link ProjectDataImpl} for info.
 */
public interface ProjectData {

  /**
   * Getter.
   *
   * @return the map storing all the relevant data.
   */
  Map<String, Map<String, DayData>> getLocations();

}
