package com.bumsoap.store.repository;

import com.bumsoap.store.model.SoapPrice;
import com.bumsoap.store.util.BsShape;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface SoapPriceRepo extends JpaRepository<SoapPrice, Long> {
  int countByBsShape(BsShape bsShape);

  @Query(nativeQuery = true,
      value = "select sp.unit_price from soap_price sp " +
          "where sp.bs_shape = :bsShape " +
          "order by sp.apply_time desc limit 1")
  BigDecimal findSoapPrice(@Param("bsShape") int bsShape);
}
