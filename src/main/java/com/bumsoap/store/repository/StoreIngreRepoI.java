package com.bumsoap.store.repository;

import com.bumsoap.store.model.StoreIngre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreIngreRepoI extends JpaRepository<StoreIngre, Long> {
  @Query(nativeQuery = true,
      value = "select distinct si.ingre_name from store_ingre si ")
  List<String> findDistinctIngreNames();
}
