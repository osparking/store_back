package com.bumsoap.store.model;

import com.bumsoap.store.util.BsShape;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
  @JoinColumn(name="order_id", nullable = false)
  private BsOrder order;

  @Column(nullable = false)
  private BsShape shape;
  
  @Column(nullable = false)
  private int count;
  private BigDecimal subTotal; // 일종의 정보 중복
}