package com.bumsoap.store.service.token;

import com.bumsoap.store.exception.RefreshTokenException;
import com.bumsoap.store.model.RefreshToken;
import com.bumsoap.store.repository.RefreshTokenRepoI;
import com.bumsoap.store.service.user.UserServInt;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServ implements RefreshTokenServInt{
    private final RefreshTokenRepoI refreshRepo;
    private final UserServInt userService;
    private static final Logger logger =
            LoggerFactory.getLogger(RefreshTokenServ.class);

    @Scheduled(cron = "0 0 3 * * ?") // 매일 새벽 3시 실행
    @Transactional
    public void cleanExpiredRefreshTokens() {
        LocalDateTime now = LocalDateTime.now();
        int deletedCount = refreshRepo.deleteByExpiryDateBefore(now);
        logger.info("만료된 리프레시 토큰 {}개 삭제 완료", deletedCount);
    }

    /**
     * 날것 RT 문자열의 해쉬를 구해 DB 에서 RT 행을 찾고, DB에 사용됨 기록함
     * @param refresh 날것 RT 문자열
     * @return 사용 가능한 RT 객체
     */
    @Override
    @Transactional
    public RefreshToken consultConsumeRefreshToken(String refresh) {
        String hashedToken = DigestUtils.sha256Hex(refresh);
        RefreshToken refreshToken = refreshRepo.findByTokenHash(hashedToken)
                .orElseThrow(() -> new RefreshTokenException("RT 검색 실패"));

        if (!refreshToken.isValid()) {
            throw new RefreshTokenException("사용되었거나 만료된 RT");
        }
        // 발견된 사용가능한 RT 사용되었다고 표시함
        refreshToken.setRevoked(true);

        return refreshToken;
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
