package com.bumsoap.store.dto;

import com.bumsoap.store.util.BsUtils;
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
  private Long id;
  private String ingreName; // 재료명; 제공
  private int quantity; // 용량
  private PackUnit packunit; // 용량 단위
  private int count; // 수량
  private LocalDate storeDate;  // 입고일
  private String addTime;  // 자료 입력 일시, 자동 부여
  private String buyPlace; // 구매처, 제공
  private long workerId; // 정보 입력 직원ID
  private LocalDate expireDate;
  private String workerName; // 입력 직원 성명
  public void setAddTime(LocalDateTime addTime) {
    this.addTime = BsUtils.getShortTimeStr(addTime);
  }
  public String getPackunit() {
    return packunit.toString();
  }
}
