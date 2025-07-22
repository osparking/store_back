package com.bumsoap.store.repository;

import com.bumsoap.store.model.StoreIngre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreIngreRepoI extends JpaRepository<StoreIngre, Long> {
}
