package com.bumsoap.store.controller;

import com.bumsoap.store.exception.RefreshTokenException;
import com.bumsoap.store.exception.SocialLoginRequiredException;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.bumsoap.store.util.BsUtils.getNow_HH_MM;
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
                    Feedback.NOT_FOUND_EMAIL, "로그인 이중 검증"));
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

    @PostMapping(UrlMap.LOGOUT)
    public ResponseEntity<ApiResp> logout(
            HttpServletRequest request,
            @CookieValue(value = "refreshToken", required = false)
            String refreshToken) {
        // 1. (중요) 세션과 JSESSIONID 정리
        try {
            if (refreshToken==null) { // RefreshToken 처리 (DB 삭제)
                throw new RefreshTokenException("RT_MISSING");
            }
            refreshTokenServ.consultDeleteRefreshToken(refreshToken);
        } catch (RuntimeException e) {
            System.out.println("Logout error: " + e.getMessage());
        }

        // 2. (필수) 서버 내부 세션 무효화
        HttpSession session = request.getSession(false);

        if (session!=null) {
            session.invalidate(); // 세션에 저장된 모든 데이터 제거
        }

        // 3. (필수) Spring Security 컨텍스트 클리어 (메모리 내 인증 정보 제거)
        SecurityContextHolder.clearContext();

        // 4. (필수) JSESSIONID 쿠키 강제 만료 (Set-Cookie 헤더 추가)
        //    Path를 반드시 "/"로 지정해야 웹앱 전체 경로에서 쿠키가 삭제됩니다.
        String jsessionCookieStr = // (운영 환경이라면 Secure; 도 추가)
                "JSESSIONID=; Path=/; HttpOnly; Max-Age=0; SameSite=Lax";

        // 5. (기존) RefreshToken 쿠키 만료 헤더
        String refreshCookieStr =
                "refreshToken=; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=0";

        // 6. 최종 응답 반환 (두 개의 쿠키 삭제 헤더를 모두 포함)
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jsessionCookieStr)
                .header(HttpHeaders.SET_COOKIE, refreshCookieStr)
                .body(new ApiResp(Feedback.LOGOUT_SUCCESS, null));
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthCon.class);
    private final UserDetailsService userDetailsService;

    @PostMapping(UrlMap.LOGIN)
    public ResponseEntity<ApiResp> login(@Valid @RequestBody LoginRequest request) {
        try {
            BsUser user = userService.getByEmail(request.getEmail());

            if (user==null) {
                return ResponseEntity.status(UNAUTHORIZED)
                        .body(new ApiResp(Feedback.NOT_FOUND_EMAIL, null));
            } else {
                var signUpMethod = user.getSignUpMethod();

                if (!"EMAIL".equals(signUpMethod)) {
                    LoginSource source = LoginSource.valueOf(signUpMethod);
                    var message = source.getLabel() + Feedback.TRY_SOCIAL_LOGIN;

                    throw new SocialLoginRequiredException(message);
                }
            }

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
            ResponseCookie refreshCookie = authUtil.createSaveCookie(refresh);
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
        } catch (SocialLoginRequiredException e) {
            // ✅ 소셜 계정으로 유도하는 400 응답
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResp(e.getMessage(), null));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(
                    new ApiResp(Feedback.BAD_CREDENTIAL, null));
        } catch (RuntimeException e) {
            logger.error("Unexpected login error: ", e);
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
        logger.debug("토큰 refresh 시도 시간: {}", getNow_HH_MM());
        try {
            // 1. 쿠키에 RT가 없는 경우 (required = false 로 설정하여 직접 예외 처리)
            if (refresh1==null || refresh1.isEmpty()) {
                throw new RefreshTokenException("RT_MISSING");
            }
            // 2. DB에서 해시 값으로 조회 (만료일, 폐기 여부 체크)
            RefreshToken refreshToken =
                    refreshTokenServ.consultConsumeRefreshToken(refresh1);

            // 3. 새 access token(JWT) 발급
            var details = BsUserDetails.buildUserDetails(refreshToken.getUser());
            String jwt = jwtUtilBean.generateTokenForUser(details);

            // 4. 리프레시 토큰을 만들고, DB 에 저장
            var refresh2 = refreshTokenServ.createRefreshForUser(details.getId());

            // 5. 응답 본문 - JWT 만 포함
            JwtResponse jwtResponse = new JwtResponse(details.getId(), jwt);

            // 6. 응답 헤더 - 리프레시를 HttpOnly 쿠키로 설정
            ResponseCookie refreshCookie = authUtil.createSaveCookie(refresh2);

            // 7. 최종 응답(AT 는 본문에, RT 는 헤더에 적재)
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .body(new ApiResp(Feedback.REFRESHING_SUCCESS, jwtResponse));
        } catch (RefreshTokenException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(
                    new ApiResp(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }
}
