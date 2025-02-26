package com.bumsoap.store.repository;

import com.bumsoap.store.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepoI extends JpaRepository<Admin, Long> {
}
