package com.bumsoap.store.repository;

import com.bumsoap.store.model.FeeDelivery;
import com.bumsoap.store.util.BoxSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeeEtcRepo extends JpaRepository<FeeDelivery, Long> {
    @Query("""
            select fe from FeeEtc fe where fe.boxSize = :boxSize
            order by fe.id desc limit 1
            """)
    FeeDelivery findLatestFee(@Param("boxSize") BoxSize boxSize);
}
