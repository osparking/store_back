package com.bumsoap.store.repository;

import com.bumsoap.store.model.BsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepoI extends JpaRepository<BsUser, Long> {
    boolean existsByEmail(String email);
}
