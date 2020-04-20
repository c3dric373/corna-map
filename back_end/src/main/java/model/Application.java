package model;

import model.simulator.CauchyProblem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.ComponentScan;

import java.util.List;
import java.util.function.Function;


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


/*
  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {

      System.out.println("Let's inspect the beans provided by Spring Boot:");

      String[] beanNames = ctx.getBeanDefinitionNames();
      Arrays.sort(beanNames);
      for (String beanName : beanNames) {
        System.out.println(beanName);
      }

    };
  }*/

}
