package com.bumsoap.store.dto;

import com.bumsoap.store.model.StoreIngre;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class StoreIngreRow extends StoreIngre {
  private String workerName;
}
