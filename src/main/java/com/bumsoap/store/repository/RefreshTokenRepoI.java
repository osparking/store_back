package com.bumsoap.store.repository;

import com.bumsoap.store.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepoI extends JpaRepository<RefreshToken, Long> {
}
