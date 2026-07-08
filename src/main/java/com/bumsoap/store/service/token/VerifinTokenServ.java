package com.bumsoap.store.service.token;

import com.bumsoap.store.event.UserRegisterEvent;
import com.bumsoap.store.exception.DataNotFoundException;
import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.model.VerifinToken;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.repository.VerifinTokenRepoI;
import com.bumsoap.store.util.BsUtils;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.TokenResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static com.bumsoap.store.util.TokenResult.*;

@Service
@RequiredArgsConstructor
public class VerifinTokenServ implements VerifinTokenServInt {
    private final UserRepoI userRepo;
    private final VerifinTokenRepoI verifinTokenRepo;
    private final ApplicationEventPublisher publisher;

    @Override
    public BsUser findUserByToken(String token) {
        return verifinTokenRepo
                .findUserByVerificationToken(token)
                .orElseThrow(() ->
                        new DataNotFoundException("토큰 부재 오류"));
    }

    @Override
    @Transactional
    public TokenResult verifyToken(String token) {
        Optional<VerifinToken> optionalVeriTok = findByToken(token);
        if (optionalVeriTok.isEmpty()) {
            return INVALID;
        } else {
            var tokenFound = optionalVeriTok.get();

            if (tokenFound.getDiscarded()) {
                return DISCARDED;
            } else {
                BsUser user = tokenFound.getUser();
                if (user.isEnabled()) {
                    if (hasTokenExpired(token)) {
                        return VERIFIED;
                    } else {
                        return VALIDATED;
                    }
                }
                if (hasTokenExpired(token)) {
                    return EXPIRED;
                }
                user.setEnabled(true);
                userRepo.save(user);
                tokenFound.setDiscarded(true);

                return VALIDATED;
            }
        }

    }

    @Override
    public boolean isBeingVerified(String email) {
        var verificationToken = verifinTokenRepo.findVerificationToken(email);

        return verificationToken.isPresent() &&
                !verificationToken.get().getDiscarded();
    }

    @Override
    @Modifying
    @Transactional
    public Date saveTokenForUser(String token, BsUser user) {
        verifinTokenRepo.deleteByUserId(user.getId());
        var verifinToken = new VerifinToken(token, user);
        verifinTokenRepo.save(verifinToken);
        return verifinToken.getExpireDate();
    }

    @Override
    public VerifinToken makeNewToken(String oldToken) {
        Optional<VerifinToken> optionalToken = findByToken(oldToken);

        if (optionalToken.isPresent()) {
            VerifinToken verifToken = optionalToken.get();
            verifToken.setToken(UUID.randomUUID().toString());
            verifToken.setExpireDate(BsUtils.getExpireTime());

            return verifinTokenRepo.save(verifToken);
        }
        throw new IllegalStateException(Feedback.JWT_WRONG + oldToken);
    }

    @Override
    public Optional<VerifinToken> findByToken(String token) {
        return verifinTokenRepo.findByToken(token);
    }

    @Override
    public void deleteTokenByUserId(Long tokenId, Long userId) {
        verifinTokenRepo.deleteByUserId(tokenId, userId);
    }

    @Override
    public boolean hasTokenExpired(String token) {
        var verifinToken = verifinTokenRepo.findByToken(token);

        if (verifinToken.isEmpty()) {
            return true;
        } else {
            var expireDate = verifinToken.get().getExpireDate();
            return System.currentTimeMillis() > expireDate.getTime();
        }
    }

    @Override
    @Transactional
    public TokenResult reIssueToken(String tokenString) {
        var optionalToken = findByToken(tokenString);
        VerifinToken expiredToken = optionalToken.orElseThrow(
                () -> new DataNotFoundException("이메일 검증 토큰 부재"));

        var user = (expiredToken.getUser());
        verifinTokenRepo.delete(expiredToken); // 만료된 토큰 삭제

        String newTokenStr = UUID.randomUUID().toString();
        verifinTokenRepo.save(new VerifinToken(newTokenStr, user));
        publisher.publishEvent(new UserRegisterEvent(user, newTokenStr));

        return TokenResult.REISSUED;
    }
}
