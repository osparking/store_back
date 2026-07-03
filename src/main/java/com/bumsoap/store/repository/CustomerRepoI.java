package com.bumsoap.store.repository;

import com.bumsoap.store.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepoI extends JpaRepository<Customer, Long> {
    @Query("""
             SELECT c FROM Customer c
              WHERE LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))
                AND LOWER(c.fullName) LIKE LOWER(CONCAT('%', :name, '%'))
            """)
    Page<Customer> getOnePage(
            @Param("email") String email,
            @Param("name") String name, Pageable pageable);
}
