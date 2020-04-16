package controller;

import com.google.gson.Gson;
import model.project.ProjectDataImpl;
import model.project.ProjectDataWrapperImpl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@CrossOrigin("http://localhost:4200")
public class DayDataController {

  @RequestMapping(value = "/testday", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  String index() throws IOException {
    ProjectDataWrapperImpl testScrapper = new ProjectDataWrapperImpl();
    ProjectDataImpl test = testScrapper.getCurrentAllDataFrance();

    Gson gson = new Gson();

    // 2. Java object to JSON string
    return gson.toJson(test.getFrance());
  }
}
