package com.bumsoap.store.repository;

import com.bumsoap.store.model.SoapProduce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduceRepo extends JpaRepository<SoapProduce, Long> {
}
