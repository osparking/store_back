package com.bumsoap.store.model;

import com.bumsoap.store.util.BsShape;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoapInven {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, unique = true)
  private BsShape bsShape;
  @Column(nullable = false)
  private int stockLevel;

  public SoapInven(BsShape bsShape, int stockLevel) {
    this.bsShape = bsShape;
    this.stockLevel = stockLevel;
  }
}
