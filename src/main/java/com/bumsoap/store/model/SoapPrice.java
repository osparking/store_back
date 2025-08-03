package com.bumsoap.store.model;

import com.bumsoap.store.util.BsShape;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "soap_price", indexes = {
  @Index(name = "latest_price", columnList = "bs_shape, apply_time desc")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoapPrice {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private BsShape bsShape;
  @Column(nullable = false)
  private BigDecimal unitPrice;
  @Column(name = "apply_time", updatable = false, nullable = false,
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime applyTime = LocalDateTime.now();

  public SoapPrice(BsShape bsShape, BigDecimal unitPrice) {
    this.bsShape = bsShape;
    this.unitPrice = unitPrice;
  }
}
