package com.bumsoap.store.util;

import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.security.user.BsUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
  @Autowired
  UserRepoI userRepo;

  public BsUserDetails loggedInUserDetails() {
    var principal = SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    BsUserDetails details = switch (principal) {
      case BsUserDetails userDetails -> userDetails;
      case DefaultOAuth2User oauth2User -> {
        String email = oauth2User.getAttribute("email");
        yield userRepo.findByEmail(email)
            .map(BsUserDetails::buildUserDetails)
            .orElseThrow(() -> new RuntimeException
                (Feedback.NOT_FOUND_EMAIL + email));
      }
      default -> null;
    };
    return details;
  }

  public Long loggedInUserId() {
    var principal = SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    Long id = switch (principal) {
      case BsUserDetails userDetails -> userDetails.getId();
      case DefaultOAuth2User oauth2User -> {
        String email = oauth2User.getAttribute("email");
        yield userRepo.findByEmail(email)
            .map(BsUser::getId)
            .orElseThrow(() -> new RuntimeException
                (Feedback.NOT_FOUND_EMAIL + email));
      }
      default -> null;
    };
    return id;
  }

  public String getJwtFromRequest(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer ")) {
      return null;
    }
    return header.substring(7);
  }

  @Value("${auth.refresh.expirationSec}")
  private int expirationSec;
  /**
   * (JWT) 토큰 리프레시 작업용 리프레시 토큰을 ResponseCookie 로 만든다.
   * @param refreshToken 리스레시 토큰     *
   * @return 생성된 ResponseCookie
   */
  public ResponseCookie createSaveCookie(String refreshToken) {
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
