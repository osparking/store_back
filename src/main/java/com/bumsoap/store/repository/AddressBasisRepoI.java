package com.bumsoap.store.repository;

import com.bumsoap.store.model.AddressBasis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressBasisRepoI extends
    JpaRepository<AddressBasis, Long> {
  Optional<AddressBasis> findByRoadAddress(String roadAddress);
}
