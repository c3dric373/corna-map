package model.project;

import model.data.DayData;

import java.util.ArrayList;
import java.util.List;


public class ProjectDataImpl {

  /**
   * List which contains the data about corona for each county in france.
   */
  List<DayData> county = new ArrayList<>();


  /**
   * List which contains the data about corona for each region in france.
   */
  List<DayData> region = new ArrayList<>();

  /**
   * Data about corona for france.
   */
  DayData france = null;


}
