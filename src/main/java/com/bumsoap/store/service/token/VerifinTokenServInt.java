package com.bumsoap.store.service.token;

import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.model.VerifinToken;
import com.bumsoap.store.util.TokenResult;

import java.util.Optional;

public interface VerifinTokenServInt {
    TokenResult verifyToken(String token);

    boolean hasNotExpiredTokenFor(String email);

    void saveTokenForUser(String token, BsUser user);
    VerifinToken makeNewToken(String oldToken);
    Optional<VerifinToken> findByToken(String token);
    void deleteTokenByUserId(Long tokenId, Long userId);
    boolean hasTokenExpired(String token);
}
