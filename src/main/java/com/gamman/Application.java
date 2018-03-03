package com.gamman;

import com.gamman.controllers.GameController;
import com.gamman.service.GameProcessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackageClasses = {GameController.class, GameProcessor.class})
@Import(WebSecurityConfig.class)
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
