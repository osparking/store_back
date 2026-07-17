package com.bumsoap.store.controller;

import com.bumsoap.store.exception.RefreshTokenException;
import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.model.RefreshToken;
import com.bumsoap.store.repository.RefreshTokenRepoI;
import com.bumsoap.store.request.LoginRequest;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.response.JwtResponse;
import com.bumsoap.store.security.TokenCache;
import com.bumsoap.store.security.jwt.JwtUtilBean;
import com.bumsoap.store.security.user.BsUserDetails;
import com.bumsoap.store.service.TotpService;
import com.bumsoap.store.service.token.RefreshTokenServInt;
import com.bumsoap.store.service.token.VerifinTokenServInt;
import com.bumsoap.store.service.user.UserServInt;
import com.bumsoap.store.service.worker.WorkerServInt;
import com.bumsoap.store.util.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(UrlMap.AUTHO)
@RequiredArgsConstructor
public class AuthCon {
    private final AuthenticationManager authenticationManager;
    private final JwtUtilBean jwtUtilBean;
    private final VerifinTokenServInt verifinTokenService;
    private final UserServInt userService;
    private final AuthUtil authUtil;
    private final TotpService totpService;
    private final TokenCache tokenCache;

    @GetMapping("/email")
    public ResponseEntity<?> getEmailByToken(@RequestParam String token) {
        String email = tokenCache.getAndRemove(token);
        if (email==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Invalid or expired token"));
        }
        return ResponseEntity.ok(new ApiResp(Feedback.EMAIL_FOUND,
                Map.of("email", email)));
    }

    /**
     * 비밀번호 리셋용 토큰의 유효성을 판단하여 그 결과를 반환한다
     *
     * @param token 검증 대상 토큰
     * @return 반응개체 성공 혹은 실패 상태
     */
    @GetMapping(UrlMap.VERIFY_TOKEN)
    public ResponseEntity<?> verifyToken(@RequestParam String token) {
        TokenResult result = verifinTokenService
                .verifyPasswordResetToken(token, false);

        try {
            // 검증 결과에 따라 예외 메시지를 달리한다.
            switch (result) {
                case EXPIRED:
                case INVALID:
                case DISCARDED:
                    throw new Exception(result.label);
                default:
                    // 다른 값인 경우 아무 동작도 하지 않음
                    break;
            }
            return ResponseEntity.ok(
                    new ApiResp(Feedback.TOKEN_IS_VALID, null));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @GetMapping("/user/2fa-status")
    public ResponseEntity<?> get2FAstatus() {
        var details = authUtil.loggedInUserDetails();

        if (details==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Feedback.NOT_FOUND);
        } else {
            return ResponseEntity.ok().body(
                    Map.of("2FA-활성화", details.isTwoFaAEnabled()));
        }
    }

    @PostMapping("/verify-2fa")
    public ResponseEntity<String> verify2FA(@RequestParam int code) {
        Long userId = authUtil.loggedInUserId();
        boolean validCode = userService.verifyCode(userId, code);

        if (validCode) {
            userService.enable2FA(userId);
            return ResponseEntity.ok(Feedback.TWO_FA_VERIFIED);
        } else {
            return ResponseEntity
                    .status(BAD_REQUEST)
                    .body(Feedback.TWO_FA_CODE_ERROR);
        }
    }

    @PostMapping("/public/verify-2fa-login")
    public ResponseEntity<ApiResp> verify2FaLogin(@RequestParam int code,
                                                  @RequestParam String jwtToken) {
        String username = jwtUtilBean.getUsernameFrom(jwtToken);
        try {
            var user = userService.getBsUserByEmail(username);
            if (userService.verifyCode(user.getId(), code)) {
                return ResponseEntity.ok(
                        new ApiResp(Feedback.AUTHEN_SUCCESS, null));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ApiResp(Feedback.TWO_FA_CODE_ERROR, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResp(
                    Feedback.NOT_FOUND_EMAIL + username, "로그인 이중 검증"));
        }
    }

    @PostMapping("/disable-2fa")
    public ResponseEntity<String> disableUserFor2FA() {
        Long userId = authUtil.loggedInUserId();
        userService.disable2FA(userId);
        return ResponseEntity.ok(Feedback.DISABLED_2FA);
    }

    @PostMapping("/enable-2fa")
    public ResponseEntity<String> enableUserFor2FA() {
        Long userId = authUtil.loggedInUserId();
        var secret = userService.generateSecret(userId);
        String qrCodeUrl = totpService.getQRcodeUrl(secret,
                userService.getUserById(userId).getEmail());
        return ResponseEntity.ok(qrCodeUrl);
    }

    @GetMapping(UrlMap.EMAIL_ADDRESS)
    public ResponseEntity<ApiResp> verifyEmailToken(@RequestParam("token") String token) {
        try {
            TokenResult result = verifinTokenService.verifyToken(token);
            // 만료된 토큰의 경우 새 토큰을 발급하고 새 이메일을 보낸다.
            HttpStatus status = HttpStatus.OK;
            if (result==TokenResult.EXPIRED) {
                result = verifinTokenService.reIssueToken(token);
            }
            if (result==TokenResult.INVALID) {
                status = HttpStatus.GONE;
            }
            return ResponseEntity.status(status).body(new ApiResp(result.label, null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    private final WorkerServInt workerServ;
    private final RefreshTokenServInt refreshTokenServ;

    @PostMapping(UrlMap.LOGIN)
    public ResponseEntity<ApiResp> login(@Valid @RequestBody LoginRequest request) {
        try {
            if (workerServ.isAccountDeleted(request.getEmail())) {
                throw new AccountExpiredException(Feedback.WRONG_CREDENTIAL);
            }
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(
                    authentication);
            var userDetails = (BsUserDetails) authentication.getPrincipal();
            userDetails.setLoginMethod(LoginSource.EMAIL.getLabel());
            String jwt = jwtUtilBean.generateTokenForUser(userDetails);
            /**
             * 리프레시 토큰을 만들고, DB 에 저장하며, 반응에 포함한다.
             */
            var refresh = refreshTokenServ.createRefreshForUser(userDetails.getId());
            ResponseCookie refreshCookie = createRefreshCookie(refresh);
            JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .body(new ApiResp(Feedback.AUTHEN_SUCCESS, jwtResponse));
        } catch (DisabledException e) {
            String message = Feedback.CLOSED_ACCOUNT;
            HttpStatus status = ACCEPTED;

            if (verifinTokenService.isBeingVerified(request.getEmail())) {
                status = ALREADY_REPORTED;
                message = Feedback.PLZ_VERIFY_EMAIL;
            }
            return ResponseEntity.status(status).body(
                    new ApiResp(message, null));
        } catch (AccountExpiredException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(
                    new ApiResp(e.getMessage(), null));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(
                    new ApiResp(e.getMessage(), Feedback.BAD_CREDENTIAL));
        } catch (RuntimeException e) {
            BsUser user = userService.getByEmail(request.getEmail());
            if (user==null) {
                return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                        .body(new ApiResp(e.getMessage(), null));
            } else {
                var signUp = LoginSource.valueOf(user.getSignUpMethod());
                var message = signUp.getLabel() + Feedback.TRY_SOCIAL_LOGIN;
                return ResponseEntity.status(BAD_REQUEST).body(
                        new ApiResp(message, null));
            }
        }
    }

    private final RefreshTokenRepoI refreshRepo;

    @PostMapping(UrlMap.REFRESH_TOKEN)
    public ResponseEntity<?> refresh(@CookieValue(value = "refreshToken",
            required = false) String refresh1) {
        try {
            // 1. 쿠키에 RT가 없는 경우 (required = false 로 설정하여 직접 예외 처리)
            if (refresh1==null || refresh1.isEmpty()) {
                throw new RefreshTokenException("리프레시 토큰 쿠키 부재");
            }
            // 2. DB에서 해시 값으로 조회 (만료일, 폐기 여부 체크)
            RefreshToken entity = refreshTokenServ.getRefrechTokenEntity(refresh1);

            if (entity.getExpiryDate().isBefore(LocalDateTime.now())
                    || entity.isRevoked()) {
                throw new RuntimeException("만료 또는 폐기된 RT");
            }

            // 3. 유효성 검증 (만료 or 폐기 여부)
            if (entity.getExpiryDate().isBefore(LocalDateTime.now())
                    || entity.isRevoked()) {
                throw new RefreshTokenException("만료 또는 폐기된 RT");
            }

            // 4. (로테이션) 기존 RT 폐기(Revoked), 새로운 RT 생성 및 DB 저장
            entity.setRevoked(true);
            refreshRepo.save(entity); // 궂이 필요?

            // 5. 새 access token(JWT) 발급
            var userDetails = BsUserDetails.buildUserDetails(entity.getUser());
            String jwt = jwtUtilBean.generateTokenForUser(userDetails);

            // 6. 리프레시 토큰을 만들고, DB 에 저장
            var refresh = refreshTokenServ.createRefreshForUser(userDetails.getId());

            // 7. 응답 본문 - JWT 만 포함
            JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);

            // 8. 응답 헤더 - 리프레시를 HttpOnly 쿠키로 설정
            ResponseCookie refreshCookie = createRefreshCookie(refresh);

            // 9. 최종 응답(AT 는 본문에, RT 는 헤더에 적재)
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .body(new ApiResp(Feedback.AUTHEN_SUCCESS, jwtResponse));
        } catch (RefreshTokenException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(
                    new ApiResp(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @Value("${auth.refresh.expirationSec}")
    private int expirationSec;

    private ResponseCookie createRefreshCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken) // 키 이름
                .httpOnly(true) // JavaScript 접근 차단 (보안 핵심)
                // HTTPS 에서만 전송 (운영 환경 필수, 테스트 시 false 가능)
                .secure(true)
                // 모든 경로에서 쿠키 전송 (refresh 엔드포인트가
                // - /autho/refresh_token 이므로 최소한 해당 경로 포함)
                .path("/")
                .maxAge(expirationSec) // (현장용) 1 주 / (시험용) 1 분
                .sameSite("Strict") // CSRF 방지 (Strict 또는 Lax )
                .build();
    }
}
