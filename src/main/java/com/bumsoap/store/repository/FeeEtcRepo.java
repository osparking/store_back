package com.bumsoap.store.repository;

import com.bumsoap.store.model.FeeEtc;
import com.bumsoap.store.util.BoxSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeeEtcRepo extends JpaRepository<FeeEtc, Long> {
    @Query("""
            select fe from FeeEtc fe where fe.boxSize = :boxSize
            order by fe.id desc limit 1
            """)
    FeeEtc findLatestFee(@Param("boxSize") BoxSize boxSize);
}
