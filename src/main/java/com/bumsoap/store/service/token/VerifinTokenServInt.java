package com.bumsoap.store.service.token;

import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.model.VerifinToken;
import com.bumsoap.store.util.TokenResult;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.Optional;

public interface VerifinTokenServInt {
    TokenResult verifyToken(String token);

    boolean hasNotExpiredTokenFor(String email);
    boolean hasTokenFor(String email);

    Date saveTokenForUser(String token, BsUser user);
    VerifinToken makeNewToken(String oldToken);
    Optional<VerifinToken> findByToken(String token);
    void deleteTokenByUserId(Long tokenId, Long userId);
    boolean hasTokenExpired(String token);

    TokenResult reIssueToken(String token);

    boolean isOpenAccount(@NotBlank String email);
}
