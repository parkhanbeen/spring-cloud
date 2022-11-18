package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.entity.OrderEntity;
import com.example.orderservice.messagequeue.KafkaProducer;
import com.example.orderservice.messagequeue.OrderProducer;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/order-service")
@RestController
public class OrderController {

  private final Environment environment;
  private final OrderService orderService;
  private final KafkaProducer kafkaProducer;
  private final OrderProducer orderProducer;

  @GetMapping("/health-check")
  public String status() {
    return String.format("It's Working In Order Service on Port %s",
        environment.getProperty("local.server.port"));
  }

  @PostMapping("/{userId}/orders")
  public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId,
                                                   @RequestBody RequestOrder orderDetails) {
    log.info("Before add orders data");
    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    OrderDto orderDto = mapper.map(orderDetails, OrderDto.class);
    orderDto.setUserId(userId);

    OrderDto createOrder = orderService.createOrder(orderDto);
    ResponseOrder responseOrder = mapper.map(createOrder, ResponseOrder.class);

    /* kafka */
//    orderDto.setOrderId(UUID.randomUUID().toString());
//    orderDto.setTotalPrice(orderDetails.getQty() * orderDetails.getUnitPrice());
//
    kafkaProducer.send("example-catalog-topic", orderDto);
//    orderProducer.send("orders", orderDto);
//
//    ResponseOrder responseOrder = mapper.map(orderDto, ResponseOrder.class);

    log.info("After add orders data");

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(responseOrder);
  }

  @GetMapping("/{userId}/orders")
  public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId)
      throws Exception {
    log.info("Before retrieve orders data");
    Iterable<OrderEntity> orders = orderService.getOrdersByUserId(userId);
    List<ResponseOrder> responseOrders = new ArrayList<>();

    orders.forEach(order ->
      responseOrders.add(new ModelMapper().map(order, ResponseOrder.class))
    );

//    try {
//
//      Thread.sleep(1000);
//      throw new Exception("장애 발생");
//
//    } catch (InterruptedException ex) {
//      log.warn(ex.getMessage());
//    }

    log.info("After retrieved orders data");

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(responseOrders);
  }

}
