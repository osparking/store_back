package com.bumsoap.store.repository;

import com.bumsoap.store.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepoI extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
