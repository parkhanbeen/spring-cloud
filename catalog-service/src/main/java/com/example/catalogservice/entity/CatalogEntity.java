package com.example.catalogservice.entity;

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
@Table(name = "catalog")
public class CatalogEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 120, unique = true)
  private String productId;
  @Column(nullable = false)
  private String productName;
  @Column(nullable = false)
  private Integer stock;
  @Column(nullable = false)
  private Integer unitPrice;

  @Column(nullable = false, updatable = false, insertable = false)
  @ColumnDefault(value = "CURRENT_TIMESTAMP")
  private Date CreatedAt;

  /**
   * 수량을 업데이트 합니다.
   */
  public void updateStock(Integer stock) {
    this.stock = stock;
  }
}
