package com.bumsoap.store.repository;

import com.bumsoap.store.model.FeeEtc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeeEtcRepo extends JpaRepository<FeeEtc, Long> {
  @Query(nativeQuery = true, value =
      "select fe.* from fee_etc fe order by fe.id desc limit 1")
  FeeEtc findLatestFee();
}
