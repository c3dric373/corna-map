package controller;

import com.google.gson.Gson;
import model.project.ProjectDataImpl;
import model.project.ProjectDataWrapperImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;


@RestController
@CrossOrigin("http://localhost:4200")
public class DayDataController {

  @RequestMapping(value = "/testday", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  String index(@RequestParam("paramName") String param) throws IOException {
    ProjectDataWrapperImpl testScrapper = new ProjectDataWrapperImpl();
    ProjectDataImpl test = testScrapper.getCurrentAllDataFrance();

    Gson gson = new Gson();

    // 2. Java object to JSON string
    switch (param) {
      case france:
        return gson.toJson(test.getFrance());
      case region:
        return gson.toJson(test.getRegion());
      case dept:
        return gson.toJson(test.getCounty());
      default :
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
    }

  }
}
