package com.bumsoap.store.request;

import com.bumsoap.store.util.PackUnit;
import lombok.Data;
import java.time.LocalDate;

@Data
public class IngreStoreReq {
  private String ingreName; // 재료명; 제공; 필수
  private int quantity; // 용량; 필수
  private PackUnit packunit; // 용량 단위; 필수
  private int count; // 수량; 필수
  private LocalDate storeDate;  // 입고일
  private String buyPlace; // 구매처, 제공; 필수
  private long workerId; // 정보 입력 직원ID; 필수
  private LocalDate expireDate;
  public void setPackunit(String packunit) {
    this.packunit = PackUnit.valueOfLabel(packunit);
  }
}
