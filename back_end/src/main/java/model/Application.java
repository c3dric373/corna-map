package model;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("controller")
public class Application {

  /**
   * Start spring application.
   *
   * @param args command line args, not used.
   */
  public static void main(final String[] args) {
    SpringApplication.run(Application.class, args);

  }
}
