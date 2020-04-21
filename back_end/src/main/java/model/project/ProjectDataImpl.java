package model.project;

import lombok.Getter;
import lombok.Setter;
import model.data.DayData;
import model.data.LocalisationData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class containing all the data of the model. Given a Day we have the data
 * about county's, region's and france itself.
 */
@Getter
@Setter
public class ProjectDataImpl {

  /**
   * List which contains the data about corona for each county in france.
   */
  private List<DayData> county = new ArrayList<>();

  /**
   * List which contains the data about corona for each region in france.
   */
  private List<DayData> region = new ArrayList<>();

  /**
   * Data about corona for france.
   */
  private List<DayData> france = new ArrayList<>();

  /**
   * This map will store for each region or county the data for each date.
   * The first key is the region, the second one the date.
   */
  private Map<String, Map<String, DayData>> localisations = new HashMap<>();

  /**
   * This map will store for each date the data about all regions, county's
   * and france.
   */
  private Map<String, LocalisationData> datesRegion;

  /**
   * This map will store for each date the data about all county's
   * and france.
   */
  private Map<String, LocalisationData> datesCounty;

  ProjectDataImpl() {
  }

}
