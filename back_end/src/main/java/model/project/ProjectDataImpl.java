package model.project;

import lombok.Getter;
import lombok.Setter;
import model.data.DayData;

import java.util.ArrayList;
import java.util.List;

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

}
