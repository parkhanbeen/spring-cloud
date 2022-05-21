package com.example.userservice.vo;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Greeting {
  private final String message;

  public Greeting(@Value("${greeting.message}") String message) {
    this.message = message;
  }
}
