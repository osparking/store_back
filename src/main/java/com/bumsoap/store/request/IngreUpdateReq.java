package com.bumsoap.store.request;

import com.bumsoap.store.util.PackUnit;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IngreUpdateReq {
  private String ingreName; // 재료명; 제공
  private int quantity; // 용량
  private PackUnit packunit; // 용량 단위
  private int count; // 수량
  private LocalDate storeDate;  // 입고일
  private String buyPlace; // 구매처, 제공
  private LocalDate expireDate;

  public void setPackunit(String packunit) {
    this.packunit = PackUnit.valueOfLabel(packunit);
  }
}
