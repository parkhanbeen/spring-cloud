package com.example.orderservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseOrder {
  private String productId;
  private Integer qty;
  private Integer stock;
  private Integer unitPrice;
  private Integer totalPrice;
  private Date createdAt;

  private String orderId;
}
