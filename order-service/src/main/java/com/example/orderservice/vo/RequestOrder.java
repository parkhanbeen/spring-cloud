package com.example.orderservice.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RequestOrder {
  private String productId;
  private Integer qty;
  private Integer unitPrice;


}
