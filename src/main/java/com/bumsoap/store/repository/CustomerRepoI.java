package com.bumsoap.store.repository;

import com.bumsoap.store.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepoI extends JpaRepository<Customer, Long> {
}
