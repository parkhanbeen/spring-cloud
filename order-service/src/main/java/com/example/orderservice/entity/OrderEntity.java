package com.example.orderservice.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "orders")
public class OrderEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String productId;

  @Column(nullable = false)
  private Integer qty;

  @Column(nullable = false)
  private Integer unitPrice;

  @Column(nullable = false)
  private Integer totalPrice;

  @Column(nullable = false)
  private String userId;

  @Column(nullable = false, unique = true)
  private String orderId;

  @Column(nullable = false, updatable = false, insertable = false)
  @ColumnDefault(value = "CURRENT_TIMESTAMP")
  private Date CreatedAt;


}