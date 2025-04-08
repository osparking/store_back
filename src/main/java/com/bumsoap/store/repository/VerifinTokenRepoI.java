package com.bumsoap.store.repository;

import com.bumsoap.store.model.VerifinToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VerifinTokenRepoI extends JpaRepository<VerifinToken, Long> {
    Optional<VerifinToken> findByToken(String token);

    @Query(value = "DELETE FROM verifin_token WHERE id != :tokenId AND user_id = :userId",
            nativeQuery = true)
    void deleteByUserId(@Param("tokenId") Long tokenId, @Param("userId") Long userId);

    @Query(value = "SELECT vt.token FROM verifin_token vt JOIN bs_user bu " +
            "ON vt.user_id = bu.id WHERE bu.email = :email",
            nativeQuery = true)
    Optional<String> findTokenByEmail(@Param("email") String email);
}
