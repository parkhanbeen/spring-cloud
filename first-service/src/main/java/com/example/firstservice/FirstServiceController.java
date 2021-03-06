package com.example.firstservice;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/first-service")
@RestController
@Slf4j
public class FirstServiceController {

  Environment environment;

  public FirstServiceController(Environment environment) {
    this.environment = environment;
  }

  @GetMapping("/welcome")
  public String welcome() {
    return "Welcome to the First Service.";
  }

  @GetMapping("/message")
  public String message(@RequestHeader("first-request") String header) {
    log.info(header);
    return "Hello World in First Service";
  }

  @GetMapping("/check")
  public String check(HttpServletRequest request) {
    log.info("server port : {}", request.getServerPort());

    return String.format("Hi, there. This is a message from First Service PORT : %s",
        environment.getProperty("local.server.port"));
  }
}
