package com.bumsoap.store.model;

import com.bumsoap.store.util.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BsOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  /**
   * id 문자열(0 패딩, 8자리) + 6자리 문자열(영문대소자, 숫자, -, _)
   */
  @Column(nullable = false, unique = true)
  private String orderId;

  @Column(nullable = false)
  private String orderName;

  @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
  @JoinColumn(name="user_id", nullable = false)
  private BsUser user;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,
          orphanRemoval = true)
  private List<OrderItem> items = new ArrayList<>();

  @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
  @JoinColumn(name="recipient_id", nullable = false)
  private Recipient recipient;

  @Column(nullable = false)
  private BigDecimal payment;

  @Column(name = "order_time", updatable = false, nullable = false,
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime orderTime = LocalDateTime.now(); // 주문일시

  @Column(nullable = false)
  private OrderStatus orderStatus;

  @OneToOne(mappedBy = "order")
  private TossPayment tossPayment;

  @Column(unique = true)
  private String waybillNo;
}
