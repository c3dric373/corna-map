package controller;

import com.google.gson.Gson;
import model.project.ProjectDataWrapper;
import model.project.ProjectDataWrapperImpl;
import org.apache.commons.lang.Validate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
   */
  @RequestMapping(value = "/map/infosFrance", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  String index(@RequestParam("date") final String date) {
    Validate.notNull(date, "date is null");
    Validate.notEmpty(date, "date empty");

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
   */
  @RequestMapping(value = {"/map/infosRegion", "/map/infosDept"}, method =
    RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  String infosRegion(@RequestParam("date") final String date,
                     @RequestParam(value = "name",
                       required = false) final String name) {
    Validate.notNull(date, "date is null");
    Validate.notEmpty(date, "date empty");

    Gson gson = new Gson();
    if (name == null) {
      return gson.toJson(wrapper.infosRegion(date));
    } else {
      return gson.toJson(wrapper.infosLocalisation(name, date));
    }
  }

  /**
   * Get all the data about a location.
   *
   * @param location The location from which the data is requested.
   * @return the requested data in a json format.
   */
  @RequestMapping(value = {"/historique"}, method =
    RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  String history(@RequestParam("location") final String location) {
    Validate.notNull(location, "location null");
    Validate.notEmpty(location, "location empty");
    Gson gson = new Gson();
    return gson.toJson(wrapper.historyLocalisation(location));
  }

}
