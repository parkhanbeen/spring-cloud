package com.example.orderservice.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Schema {
  private final String type;
  private final List<Field> fields;
  private final boolean optional;
  private final String name;

  @Builder
  public Schema(String type,
                List<Field> fields,
                boolean optional,
                String name) {
    this.type = type;
    this.fields = fields;
    this.optional = optional;
    this.name = name;
  }
}
