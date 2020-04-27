package model.project;

import lombok.Getter;
import lombok.Setter;
import model.data.DayData;

import java.util.HashMap;
import java.util.Map;

/**
 * Class containing all the data of the model. Given a Day we have the data
 * about county's, region's and france itself.
 */
@Getter
@Setter
public class ProjectDataImpl implements ProjectData {

  /**
   * This map will store for each region or county the data for each date.
   * The first key is the region, the second one the date.
   */
  private Map<String, Map<String, DayData>> locations = new HashMap<>();

  @Override
  public Map<String, Map<String, DayData>> getLocations() {
    return locations;
  }
}
