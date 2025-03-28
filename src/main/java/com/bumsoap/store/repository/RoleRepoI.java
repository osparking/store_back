package com.bumsoap.store.repository;

import com.bumsoap.store.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepoI extends JpaRepository<Role, Long> {
}
