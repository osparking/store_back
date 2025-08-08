package com.bumsoap.store.model;

import com.bumsoap.store.util.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BsOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
  @JoinColumn(name="user_id", nullable = false)
  private BsUser user;

  @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
  @JoinColumn(name="recipient_id")
  private Recipient recipient;

  private BigDecimal payAmount;

  @Column(name = "order_time", updatable = false, nullable = false,
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime orderTime = LocalDateTime.now(); // 주문일시

  @Column(nullable = false)
  private OrderStatus orderStatus;
}
