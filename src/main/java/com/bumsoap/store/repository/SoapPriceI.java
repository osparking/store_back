package com.bumsoap.store.repository;

import com.bumsoap.store.model.SoapPrice;
import com.bumsoap.store.util.BsShape;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoapPriceI extends JpaRepository<SoapPrice, Long> {
  int countByBsShape(BsShape bsShape);
}
