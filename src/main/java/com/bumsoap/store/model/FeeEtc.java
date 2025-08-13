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
@Table(name = "fee_etc", indexes = {
    @Index(name = "latest_fee", columnList = "deli_basis, apply_time desc")
})
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
}
