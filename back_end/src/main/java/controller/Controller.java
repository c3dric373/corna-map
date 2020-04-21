package controller;

import com.google.gson.Gson;
import model.project.ProjectDataWrapperImpl;
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
   * Show root home page to user.
   *
   * @return greetings.
   */
  @RequestMapping("/")
  String index() {
    return "Welcome!";
  }

  @RequestMapping(value = "/map/infosFrance", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  String index(@RequestParam("date") final String date)
    throws IOException {
    ProjectDataWrapperImpl wrapper = new ProjectDataWrapperImpl();
    wrapper.getCurrentAllDataFrance();

    Gson gson = new Gson();
    return gson.toJson(wrapper.infosFrance(date));
  }

  @RequestMapping(value = {"/map/infosRegion", "/map/infosDept"}, method =
    RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  String infosRegion(@RequestParam("date") final String date,
                     @RequestParam(value = "name", required = false) final String name)
    throws IOException {
    ProjectDataWrapperImpl wrapper = new ProjectDataWrapperImpl();
    wrapper.getCurrentAllDataFrance();

    Gson gson = new Gson();
    if (name == null) {
      return gson.toJson(wrapper.infosRegion(date));
    } else {
      return gson.toJson(wrapper.infosLocalisation(name, date));
    }
  }

  @RequestMapping(value = {"/historique"}, method =
    RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  String history(@RequestParam("location") final String location)
    throws IOException {
    ProjectDataWrapperImpl wrapper = new ProjectDataWrapperImpl();
    wrapper.getCurrentAllDataFrance();
    Gson gson = new Gson();
    return gson.toJson(wrapper.historyLocalisation(location));
  }

}