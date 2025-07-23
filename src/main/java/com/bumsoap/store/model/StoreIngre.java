package com.bumsoap.store.model;

import com.bumsoap.store.util.PackUnit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class StoreIngre {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String ingreName; // 재료명; 제공
  private int quantity; // 용량
  private PackUnit packunit; // 용량 단위
  private int count; // 수량
  private LocalDate storeDate;  // 입고일

  @Column(name = "add_time", updatable = false, nullable = false,
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime addTime = LocalDateTime.now();
  // 자료 입력 일시, 자동 부여
  private String buyPlace; // 구매처, 제공
  private long workerId; // 정보 입력 직원ID
  /**
   * 만료일 - 제조일, 유효 기한, 소비 기한 등에서 도출
   */
  private LocalDate expireDate;
}


