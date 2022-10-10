package com.example.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Payload {
  @JsonProperty("order_id")
  private final String orderId;
  @JsonProperty("user_id")
  private final String userId;
  @JsonProperty("product_id")
  private final String productId;
  @JsonProperty("qty")
  private final int qty;
  @JsonProperty("unit_price")
  private final int unitPrice;
  @JsonProperty("total_price")
  private final int totalPrice;

  @Builder
  @JsonCreator
  private Payload(String orderId,
                 String userId,
                 String productId,
                 int qty,
                 int unitPrice,
                 int totalPrice) {
    this.orderId = orderId;
    this.userId = userId;
    this.productId = productId;
    this.qty = qty;
    this.unitPrice = unitPrice;
    this.totalPrice = totalPrice;
  }

  public static Payload of(OrderDto orderDto) {
    return Payload.builder()
        .orderId(orderDto.getOrderId())
        .userId(orderDto.getUserId())
        .productId(orderDto.getProductId())
        .qty(orderDto.getQty())
        .unitPrice(orderDto.getUnitPrice())
        .totalPrice(orderDto.getTotalPrice())
        .build();
  }
}
