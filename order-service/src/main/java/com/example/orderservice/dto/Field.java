package com.example.orderservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Field {

  private final String type;
  private final boolean optional;
  private final String field;

  @Builder
  public Field(String type,
               boolean optional,
               String field) {
    this.type = type;
    this.optional = optional;
    this.field = field;
  }
}
