package com.example.userservice.controller;

import com.example.userservice.vo.Greeting;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {

  private final Environment environment;
  private final Greeting greeting;

  public UserController(Environment environment, Greeting greeting) {
    this.environment = environment;
    this.greeting = greeting;
  }

  @GetMapping("/health-check")
  public String status() {
    return "It's Working In User Service";
  }

  @GetMapping("/welcome")
  public String welcome() {
//    return environment.getProperty("greeting.message");
    return greeting.getMessage();
  }
}
