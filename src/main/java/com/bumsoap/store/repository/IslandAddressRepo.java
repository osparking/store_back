package com.bumsoap.store.repository;

import com.bumsoap.store.model.IslandAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IslandAddressRepo extends JpaRepository<IslandAddress, Long> {
    Optional<IslandAddress> findByZipcode(String zipcode);
}
