package com.example.orderservice.dto;

import java.io.Serializable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderDto implements Serializable {
  private String productId;
  private Integer qty;
  private Integer unitPrice;
  private Integer totalPrice;

  private String orderId;
  private String userId;
}
