package com.bumsoap.store.config;

import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.model.Customer;
import com.bumsoap.store.model.Role;
import com.bumsoap.store.security.jwt.JwtUtilBean;
import com.bumsoap.store.security.user.BsUserDetails;
import com.bumsoap.store.service.CustomerServInt;
import com.bumsoap.store.service.role.RoleServInt;
import com.bumsoap.store.service.user.UserServInt;
import com.bumsoap.store.util.LoginSource;
import com.bumsoap.store.util.UserType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler
    extends SavedRequestAwareAuthenticationSuccessHandler {

  @Autowired
  private final UserServInt userService;
  private final CustomerServInt customerServ;

  @Autowired
  private final JwtUtilBean jwtUtilBean;

  @Autowired
  private final RoleServInt roleServ;

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
                redirectWithJwt(user, oauth2User);
              },
              // 이메일이 DB 에 부재인 경우 처리
              () -> {
                Customer customer = new Customer();

                customer.setFullName(name);
                customer.setEmail(email);
                customer.setUserType(UserType.CUSTOMER);
                customer.setRoles(
                    Set.of(roleServ.findByName("ROLE_CUSTOMER")));
                customer.setEnabled(true);
                customer.setSignUpMethod(loginSource.toString());

                BsUser user = customerServ.add(customer);

                putAuth2Context("ROLE_CUSTOMER",
                    attributes, idAttributeKey, oAuth2);
                signUpMethod = oAuth2;
                redirectWithJwt(user, oauth2User);
              }
          );
    }
    super.onAuthenticationSuccess(request, response, authentication);
  }

  /**
   * 전단 URL 에 착륙하되, URL에는 JWT 토큰을 붙여서 보낸다.
   *
   * @param user
   * @param oAuth2User
   */
  private void redirectWithJwt(
      BsUser user, DefaultOAuth2User oAuth2User) {
    Map<String, Object> attributes = oAuth2User.getAttributes();

    // JWT 생성용 이메일 및 롤 수집 확보
    String email = (String) attributes.get("email");
    Set<GrantedAuthority> authorities=
        oAuth2User.getAuthorities().stream()
            .map(authority -> new SimpleGrantedAuthority(
                authority.getAuthority()))
            .collect(Collectors.toSet());

    Collection<Role> roles = user.getRoles();
    String firstRoleStr = roles.iterator().next().toString();
    authorities.add(new SimpleGrantedAuthority(firstRoleStr));

    BsUserDetails userDetails = new BsUserDetails(
        user.getId(), email, null, true, authorities, signUpMethod);

    this.setAlwaysUseDefaultTargetUrl(true);

    // Generate JWT token
    String jwtToken = jwtUtilBean.generateTokenForUser(userDetails);

    // Redirect to the frontend with the JWT token
    String targetUrl = UriComponentsBuilder.fromUriString(
            frontendUrl + "/oauth2/redirect")
        .queryParam("token", jwtToken)
        .build().toUriString();
    this.setDefaultTargetUrl(targetUrl);
  }

}