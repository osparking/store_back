package com.bumsoap.store.service.store;

import com.bumsoap.store.model.StoreIngre;

import java.util.Optional;

public interface StoreIngreServI {
  StoreIngre findById(Long id);

  String deleteById(Long id);

  Object getIngredientPage(String name, Optional<Integer> page,
                           Optional<Integer> size);
}
