package com.bumsoap.store.dto;

import com.bumsoap.store.util.PackUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StoreIngreDto {
  private long id;
  private String ingreName; // 재료명; 제공
  private int quantity; // 용량
  private PackUnit packunit; // 용량 단위
  private int count; // 수량
  private LocalDate storeDate;  // 입고일
  private LocalDateTime addTime;  // 자료 입력 일시, 자동 부여
  private String buyPlace; // 구매처, 제공
  private long workerId; // 정보 입력 직원ID
  private LocalDate expireDate;
  public String getPackunit() {
    return packunit.toString();
  }
}
