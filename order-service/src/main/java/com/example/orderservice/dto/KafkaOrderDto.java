package com.example.orderservice.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class KafkaOrderDto implements Serializable {
  private final Schema schema;
  private final Payload payload;

  public KafkaOrderDto(Schema schema, Payload payload) {
    this.schema = schema;
    this.payload = payload;
  }
}
