package com.bumsoap.store.model;

import com.bumsoap.store.util.BsShape;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
  @JoinColumn(name="user_id", nullable = false)
  private BsUser user;

  @Column(nullable = false)
  private BsShape shape;

  @Column(nullable = false)
  private int count;

  @Column(name = "add_time", updatable = false, nullable = false,
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime addTime = LocalDateTime.now(); // 주문일시
}
