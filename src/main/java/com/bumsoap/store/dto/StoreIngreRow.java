package com.bumsoap.store.dto;

import com.bumsoap.store.model.StoreIngre;
import com.bumsoap.store.util.PackUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreIngreRow extends StoreIngre {
  private String workerName;

  public StoreIngreRow(Long id, String ingreName, int quantity,
                       byte packunit, int count,
                       Date storeDate, Timestamp addTime,
                       String buyPlace, long workerId,
                       Date expireDate, String workerName) {
    super(id, ingreName, quantity, PackUnit.values()[packunit],
        count, storeDate.toLocalDate(),
        addTime.toLocalDateTime(), buyPlace,
        workerId, expireDate.toLocalDate());
    this.workerName = workerName;
  }
}
