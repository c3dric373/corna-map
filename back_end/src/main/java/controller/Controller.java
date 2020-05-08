package controller;

import com.google.gson.Gson;
import model.project.ProjectDataWrapper;
import model.project.ProjectDataWrapperImpl;
import org.apache.commons.lang.Validate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Controller of the spring boot application.
 */
@RestController
@CrossOrigin("http://localhost:4200")
public class Controller {

  /**
   * Wrapper of data.
   */
  private ProjectDataWrapper wrapper = new ProjectDataWrapperImpl();

  /**
   * Project state.
   */
  private State projectState = State.MAP;

  /**
   * Lock for concurrent access.
   */
  private Lock lock = new ReentrantLock();

  /**
   * Constructor. We get all the data from the web when the constructor is
   * built. Such that the data will already by available to the user when he
   * will access it.
   *
   * @throws IOException thrown if there are issues while writing the downloaded
   *                     data.
   */
  Controller() throws IOException {
    wrapper.getCurrentAllDataFrance();
  }

  /**
   * Show root home page to user.
   *
   * @return greetings.
   */
  @RequestMapping("/")
  String index() {
    return "Welcome!";
  }

  /**
   * Get information about the whole country. A specific date always needs to
   * be specified. A call on the wrapper will be invoked, which will return the
   * requested data.
   *
   * @param date The date of which we want the information.
   * @return the requested data in a json format.
   * @throws IOException read error while getting data
   */
  @RequestMapping(value = "/map/infosFrance", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  String index(@RequestParam("date") final String date) throws IOException {
    Validate.notNull(date, "date is null");
    Validate.notEmpty(date, "date empty");
    if (projectState == State.SIMULATION) {
      wrapper.getCurrentAllDataFrance();
      projectState = State.MAP;
    }
    Gson gson = new Gson();
    return gson.toJson(wrapper.infosFrance(date));
  }

  /**
   * Get information about a specific location or type of location. For
   * example the information about all county's can be queried (then the
   * param name is null) or the information about the region Ile de France
   * can be queried (then the param name is REG-11).
   * A specific date always needs to be specified.  A call on the wrapper
   * will be invoked, which will
   * return the
   * requested data.
   *
   * @param date the date at which we want the data.
   * @param name Name of region if it's specified.
   * @return the requested data in a json format.
   * @throws IOException read error while getting data
   */
  @RequestMapping(value = {"/map/infosRegion"}, method =
    RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  String infosRegion(@RequestParam("date") final String date,
                     @RequestParam(value = "name",
                       required = false) final String name) throws IOException {
    lock.lock();
    try {
      Validate.notNull(date, "date is null");
      Validate.notEmpty(date, "date empty");
      if (projectState == State.SIMULATION) {
        wrapper.getCurrentAllDataFrance();
        projectState = State.MAP;
      }
      Gson gson = new Gson();
      if (name == null) {
        return gson.toJson(wrapper.infosRegion(date));
      } else {
        return gson.toJson(wrapper.infosLocation(name, date));
      }
    } finally {
      lock.unlock();
    }
  }

  /**
   * Get information about a specific location or type of location. For
   * example the information about all county's can be queried (then the
   * param name is null) or the information about the departement Paris
   * can be queried .
   * A specific date always needs to be specified.  A call on the wrapper
   * will be invoked, which will
   * return the
   * requested data.
   *
   * @param date the date at which we want the data.
   * @param name Name of departement if it's specified.
   * @return the requested data in a json format.
   * @throws IOException read error while getting data
   */
  @RequestMapping(value = {"/map/infosDept"}, method =
    RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  String infosDept(@RequestParam("date") final String date,
                   @RequestParam(value = "name",
                     required = false) final String name) throws IOException {
    lock.lock();
    try {
      Validate.notNull(date, "date is null");
      Validate.notEmpty(date, "date empty");
      if (projectState == State.SIMULATION) {
        wrapper.getCurrentAllDataFrance();
        projectState = State.MAP;
      }
      Gson gson = new Gson();
      if (name == null) {
        return gson.toJson(wrapper.infosDept(date));
      } else {
        return gson.toJson(wrapper.infosLocation(name, date));
      }
    } finally {
      lock.unlock();
    }
  }

  /**
   * Get all the data about a location.
   *
   * @param location The location from which the data is requested.
   * @return the requested data in a json format.
   * @throws IOException read error while getting data
   */
  @RequestMapping(value = {"/historique"}, method =
    RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  String history(@RequestParam("location") final String location)
    throws IOException {
    lock.lock();
    try {
      Validate.notNull(location, "location null");
      Validate.notEmpty(location, "location empty");
      Gson gson = new Gson();
      if (projectState == State.SIMULATION) {
        wrapper.getCurrentAllDataFrance();
        projectState = State.MAP;
      }
      return gson.toJson(wrapper.historyLocation(location));

    } finally {
      lock.unlock();
    }
  }

  /**
   * Get all the data about a location.
   *
   * @param date The location from which the data is requested.
   * @return the requested data in a json format.
   */
  @RequestMapping(value = {"/simulation/infosFrance"}, method =
    RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  String simulationFrance(@RequestParam("date") final String date) {
    Validate.notNull(date, "location null");
    if (projectState != State.SIMULATION) {
      throw new IllegalStateException("Not in Simulation State");
    }
    lock.lock();
    try {
      Gson gson = new Gson();
      return gson.toJson(wrapper.simulateFrance(date));
    } finally {
      lock.unlock();
    }
  }

  /**
   * Simulates the spread of covid in france until a given date. Then retruns
   * the data for a given region.
   *
   * @param date date until which we should simulate.
   * @param name name of given region.
   * @return the {@link model.data.DayData} containing the wanted information.
   */
  @RequestMapping(value = {"simulation/infosRegion"}, method =
    RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  String simulationRegion(@RequestParam("date") final String date,
                          @RequestParam(value = "name",
                            required = false) final String name) {
    Validate.notNull(date, "date is null");
    Validate.notEmpty(date, "date empty");
    if (projectState != State.SIMULATION) {
      throw new IllegalStateException("Not in Simulation State");
    }
    lock.lock();
    try {
      Gson gson = new Gson();
      wrapper.simulateFrance(date);
      if (name == null) {
        return gson.toJson(wrapper.infosRegion(date));
      } else {
        return gson.toJson(wrapper.infosLocation(name, date));
      }
    } finally {
      lock.unlock();
    }
  }

  /**
   * Simulates the spread of covid in france until a given date. Then return
   * the data for a given department.
   *
   * @param date date until which we should simulate.
   * @param name name of given region.
   * @return the {@link model.data.DayData} containing the wanted information.
   */
  @RequestMapping(value = {"simulation/infosDept"}, method =
    RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  String simulationDept(@RequestParam("date") final String date,
                        @RequestParam(value = "name",
                          required = false) final String name) {
    Validate.notNull(date, "date is null");
    Validate.notEmpty(date, "date empty");
    if (projectState != State.SIMULATION) {
      throw new IllegalStateException("Not in Simulation State");
    }

    lock.lock();
    try {
      Gson gson = new Gson();
      wrapper.simulateFrance(date);
      if (name == null) {
        return gson.toJson(wrapper.infosDept(date));
      } else {
        return gson.toJson(wrapper.infosLocation(name, date));
      }
    } finally {
      lock.unlock();
    }

  }

  /**
   * Start simulation.
   *
   * @param content info that should be taken into consideration when starting
   *                the simulation.
   * @return Ack.
   */
  @RequestMapping(value = {"simulation/start"}, method =
    RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
  String initializeSimulation(@RequestBody final String content) {
    Gson gson = new Gson();
    lock.lock();
    try {
      projectState = State.SIMULATION;
      System.out.println(content);
      wrapper.startSimulation(content, true);
      return gson.toJson("bien reçu chef!");
    } finally {
      lock.unlock();
    }
  }

  /**
   * Start simulation.
   *
   * @return Ack.
   */
  @RequestMapping(value = {"simulation/start"}, method =
    RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  String initializeSimulationGet() {
    lock.lock();
    try {
      projectState = State.SIMULATION;
      Gson gson = new Gson();
      wrapper.startSimulation("", true);
      return gson.toJson("bien reçu chef!");
    } finally {
      lock.unlock();
    }
  }
}
