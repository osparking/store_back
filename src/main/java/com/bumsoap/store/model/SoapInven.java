package com.bumsoap.store.model;

import com.bumsoap.store.util.BsShape;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoapInven {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private BsShape bsShape;
  @Column(nullable = false)
  private int stockLevel;
  @Column(nullable = false)
  private BigDecimal unitPrice;
  @Column(name = "add_time", updatable = false, nullable = false,
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime addTime = LocalDateTime.now();

  public SoapInven(BsShape bsShape, int stockLevel, BigDecimal unitPrice) {
    this.bsShape = bsShape;
    this.stockLevel = stockLevel;
    this.unitPrice = unitPrice;
  }
}
