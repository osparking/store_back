package com.bumsoap.store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class AddressBasis {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String zipcode;
  @Column(nullable = false, unique = true)
  private String roadAddress; // 도로명 주소
  @Column(nullable = false)
  private String zBunAddress; // 지번 주소
}
