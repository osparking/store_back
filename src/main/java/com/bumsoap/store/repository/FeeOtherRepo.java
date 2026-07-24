package com.bumsoap.store.repository;

import com.bumsoap.store.model.FeeOther;
import com.bumsoap.store.util.BoxSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeeOtherRepo extends JpaRepository<FeeOther, Long> {
    @Query("select fo.* from FeeOther fo order by fo.id desc limit 1")
    FeeOther findLatestFeeOther(@Param("boxSize") BoxSize boxSize);
}
