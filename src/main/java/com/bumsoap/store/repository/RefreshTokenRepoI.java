package com.bumsoap.store.repository;

import com.bumsoap.store.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepoI extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String hashedToken);
}
