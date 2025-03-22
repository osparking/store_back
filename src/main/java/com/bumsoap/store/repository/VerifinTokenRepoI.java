package com.bumsoap.store.repository;

import com.bumsoap.store.model.VerifinToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerifinTokenRepoI extends JpaRepository<VerifinToken, Long> {
    Optional<VerifinToken> findByToken(String token);
}
