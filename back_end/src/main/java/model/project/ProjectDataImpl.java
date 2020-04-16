package model.project;

import lombok.Getter;
import lombok.Setter;
import model.data.DayData;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProjectDataImpl {

  /**
   * List which contains the data about corona for each county in france.
   */
  public List<DayData> county = new ArrayList<>();

  /**
   * List which contains the data about corona for each region in france.
   */
  public List<DayData> region = new ArrayList<>();

  /**
   * Data about corona for france.
   */
  public List<DayData> france = new ArrayList<>();

}
