package com.bumsoap.store.model;

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
public class FeeEtc {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private BigDecimal deliBasis; // 기본 배송비
  @Column(nullable = false)
  private BigDecimal deliJeju; // 제주도
  @Column(nullable = false)
  private BigDecimal deliIsol; // 벽오지(제주 외 도서 산간)
  @Column(nullable = false)
  private BigDecimal deliFreeMin; // 무배 최소 주문액

  @Column(name = "apply_time", updatable = false, nullable = false,
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime applyTime = LocalDateTime.now();

  public FeeEtc(BigDecimal deliBasis, BigDecimal deliJeju,
                BigDecimal deliIsol, BigDecimal deliFreeMin) {
    this.deliBasis = deliBasis;
    this.deliJeju = deliJeju;
    this.deliIsol = deliIsol;
    this.deliFreeMin = deliFreeMin;
  }
}
