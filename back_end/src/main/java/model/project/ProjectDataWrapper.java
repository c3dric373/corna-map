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
   * @throws IOException read error.
   */
  void getCurrentAllDataFrance() throws IOException;

  /**
   * Adds data of a location at a date to the project.
   *
   * @param location the given location.
   * @param date     the date.
   * @param dayData  the data.
   */
  void addLocation(String location, String date,
                   DayData dayData);

  /**
   * Query to get information about france at a given date.
   *
   * @param date the specific date.
   * @return the queried data.
   */
  DayData infosFrance(String date);

  /**
   * @param name id of the Location of which the data is queried.
   * @return the queried data.
   */
  List<DayData> historyLocation(String name);

  /**
   * Returns the data for a specific location on a specific date.
   *
   * @param name the specific location.
   * @param date the specific date.
   * @return the queried data.
   */
  DayData infosLocation(String name, String date);

  /**
   * Returns the data about all regions on a specific date.
   *
   * @param date the specific date.
   * @return the queried data.
   */
  List<DayData> infosRegion(String date);

  /**
   * Returns the data about all departements on a specific date.
   *
   * @param date the specific date.
   * @return the queried data.
   */
  List<DayData> infosDept(String date);

  /**
   * Adds a key to the map that stores data for each location. This is needed
   * to initialize the maps before adding data.
   *
   * @param key key to add.
   */
  void addKey(String key);

  /**
   * Simulates the propagation of the COVID-19 until a given date.
   *
   * @param date the date for which we should simulate.
   * @return the {@link DayData} containing the simulated info.
   */
  DayData simulateFrance(String date);

  /**
   * Return data of project.
   *
   * @return the project.
   */
  ProjectData getProject();

  /**
   * Notifies the model that the view is in Simulation mode.
   *
   * @param content      measures values
   * @param sirSimulator true iff {@link model.simulator.SIRSimulator} should
   *                     be used to simulate covid-19. Else
   *                     {@link model.simulator.SJYHRSimulator} will be used.
   */
  void startSimulation(String content, boolean sirSimulator);

}
