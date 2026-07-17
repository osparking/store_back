package com.bumsoap.store.service.token;

import com.bumsoap.store.model.RefreshToken;
import com.bumsoap.store.repository.RefreshTokenRepoI;
import com.bumsoap.store.service.user.UserServInt;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServ implements RefreshTokenServInt{
    private final RefreshTokenRepoI refreshRepo;
    private final UserServInt userService;

    /**
     * 날것 RT 를 받아서, 해쉬를 구하고 해쉬로 DB에서 RT 를 채취함
     * @param refresh
     * @return 채취한 refresh token
     */
    @Override
    public RefreshToken getRefrechTokenEntity(String refresh) {
        String hashedToken = DigestUtils.sha256Hex(refresh);
        RefreshToken foundToken = refreshRepo.findByTokenHash(hashedToken)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (!foundToken.isValid()) {
            throw new RuntimeException("Token expired or revoked");
        }
        return foundToken;
    }

    @Value("${auth.refresh.expirationSec}")
    private int expirationSec;

    @Transactional
    @Override
    public String createRefreshForUser(Long userId) {
        String refresh = UUID.randomUUID().toString();
        var user = userService.findById(userId);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .tokenHash(DigestUtils.sha256Hex(refresh)) // Apache Commons Codec
//                .expiryDate(LocalDateTime.now().plusWeeks(1))
                .expiryDate(LocalDateTime.now().plusSeconds(expirationSec))
                .build();

        refreshRepo.save(refreshToken);
        user.addRefreshToken(refreshToken);

        return refresh;
    }
}
