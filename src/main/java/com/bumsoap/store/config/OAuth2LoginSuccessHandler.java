package com.bumsoap.store.config;

import com.bumsoap.store.model.Role;
import com.bumsoap.store.repository.RoleRepoI;
import com.bumsoap.store.security.jwt.JwtUtilBean;
import com.bumsoap.store.service.user.UserServInt;
import com.bumsoap.store.util.LoginSource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler
    extends SavedRequestAwareAuthenticationSuccessHandler {

  @Autowired
  private final UserServInt userService;

  @Autowired
  private final JwtUtilBean jwtUtilBean;

  @Autowired
  private final RoleRepoI roleRepository;

  @Value("${frontend.base.url}")
  private String frontendUrl;

  private String username;
  private String idAttributeKey;
  private String signUpMethod = null;

  private void putAuth2Context(String role,
                               Map<String, Object> attributes,
                               String idAttributeKey,
                               String oAuth2) {
    DefaultOAuth2User oauthUser = new DefaultOAuth2User(
        List.of(new SimpleGrantedAuthority(role)),
        attributes,
        idAttributeKey
    );
    Authentication securityAuth = new OAuth2AuthenticationToken(
        oauthUser,
        List.of(new SimpleGrantedAuthority(role)),
        oAuth2
    );
    SecurityContextHolder.getContext()
        .setAuthentication(securityAuth);
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication)
      throws ServletException, IOException {
    OAuth2AuthenticationToken oAuth2AuthenticationToken
        = (OAuth2AuthenticationToken) authentication;
    String oAuth2 = oAuth2AuthenticationToken
        .getAuthorizedClientRegistrationId();
    LoginSource loginSource = LoginSource.valueOf(oAuth2.toUpperCase());
    var oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
    Map<String, Object> attributes = oauth2User.getAttributes();

    if (loginSource == LoginSource.GOOGLE
        || loginSource == LoginSource.NAVER) {
      String email = attributes.getOrDefault("email", "").toString();
      String name = attributes.getOrDefault("name", "").toString();

      if (loginSource == LoginSource.GOOGLE) {
        username = email.split("@")[0];
        idAttributeKey = "sub";
      } else {
        username = attributes.getOrDefault("login", "").toString();
        idAttributeKey = "id";
      }
      System.out.println("attrs: " + email + ", " + username);

      userService.getBsUserByEmail(email)
          .ifPresentOrElse(
              // 등록된 유저의 OAuth 2 로그인 처리
              user -> {
                Collection<Role> roles = user.getRoles();
                Role firstRole = roles.iterator().next();
                putAuth2Context(firstRole.getName(),
                    attributes, idAttributeKey, oAuth2);
                username = user.getEmail();
                signUpMethod = user.getSignUpMethod();
              },
              // 이메일이 DB 에 부재인 경우 처리
              () -> {
              }
          );
    }
  }

}