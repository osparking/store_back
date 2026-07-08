package com.bumsoap.store.service.token;

import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.model.VerifinToken;
import com.bumsoap.store.util.TokenResult;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.Optional;

public interface VerifinTokenServInt {
    BsUser findUserByToken(String token);

    TokenResult verifyToken(String token);

    Date saveTokenForUser(String token, BsUser user);
    VerifinToken makeNewToken(String oldToken);
    Optional<VerifinToken> findByToken(String token);
    void deleteTokenByUserId(Long tokenId, Long userId);
    boolean hasTokenExpired(String token);

    TokenResult reIssueToken(String token);

    @Transactional
    TokenResult verifyPasswordResetToken(String token);

    boolean isBeingVerified(@NotBlank String email);
}
