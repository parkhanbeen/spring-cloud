package com.example.orderservice.messagequeue;

import java.util.Arrays;
import java.util.List;

import com.example.orderservice.dto.Field;
import com.example.orderservice.dto.KafkaOrderDto;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.Payload;
import com.example.orderservice.dto.Schema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderProducer {
  private final KafkaTemplate<String, String> kafkaTemplate;

  private final List<Field> fields = Arrays.asList(
      new Field("string", true, "order_id"),
      new Field("string", true, "user_id"),
      new Field("string", true, "product_id"),
      new Field("int32", true, "qty"),
      new Field("int32", true, "unit_price"),
      new Field("int32", true, "total_price"));

  private final Schema schema = Schema.builder()
      .type("struct")
      .fields(fields)
      .optional(false)
      .name("orders")
      .build();

  public OrderDto send(String topic, OrderDto orderDto) {
    final var payload = Payload.of(orderDto);

    final var kafkaOrderDto = new KafkaOrderDto(schema, payload);

    final ObjectMapper mapper = new ObjectMapper();
    String jsonInString = "";
    try {
      jsonInString = mapper.writeValueAsString(kafkaOrderDto);
    } catch (JsonProcessingException jpe) {
      log.error("JsonProcessingException = ", jpe);
    }

    kafkaTemplate.send(topic, jsonInString);
    log.info("Order Producer sent data from the Order microservice : {}", kafkaOrderDto);

    return orderDto;
  }
}
