package com.bumsoap.store.service.store;

import com.bumsoap.store.model.StoreIngre;

public interface StoreIngreServI {
  StoreIngre findById(Long id);

  String deleteById(Long id);
}
