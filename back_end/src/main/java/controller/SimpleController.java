package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller of the spring boot application.
 */
@RestController
public class SimpleController {

  /**
   * Show root home page to user.
   * @return greetings.
   */
  @RequestMapping("/")
  String index() {
    return "Greetings from Spring Boot!";
  }

}
