package com.bumsoap.store.repository;

import com.bumsoap.store.model.SoapInven;
import com.bumsoap.store.util.BsShape;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SoapInvenI extends JpaRepository<SoapInven, Long> {
  Optional<SoapInven> findByBsShape(BsShape bsShape);
}
