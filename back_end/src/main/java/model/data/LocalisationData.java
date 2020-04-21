package model.data;

import java.util.List;

/**
 * Wrapper class for a list of {@link DayData}. Here we can store all the
 * dayData for a given region or county.
 */
public class LocalisationData {
  /**
   * Id of the localisation.
   */
  private String id;
  /**
   * Type of the localisation.
   */
  private TypeLocalisation typeLocalisation;
  /**
   *List of all DayData about this specific localisation.
   */
  private List<DayData> data;

}
