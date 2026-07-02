package com.bumsoap.store.repository;

import com.bumsoap.store.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepoI extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c")
    Page<Customer> getOnePage(Pageable pageable);
}
