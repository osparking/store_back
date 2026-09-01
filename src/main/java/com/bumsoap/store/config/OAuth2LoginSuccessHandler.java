package com.bumsoap.store.config;

import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.model.Customer;
import com.bumsoap.store.model.Role;
import com.bumsoap.store.security.TokenCache;
import com.bumsoap.store.security.jwt.JwtUtilBean;
import com.bumsoap.store.security.user.BsUserDetails;
import com.bumsoap.store.service.CustomerServInt;
import com.bumsoap.store.service.role.RoleServInt;
import com.bumsoap.store.service.token.RefreshTokenServInt;
import com.bumsoap.store.service.user.UserServInt;
import com.bumsoap.store.util.AuthUtil;
import com.bumsoap.store.util.LoginSource;
import com.bumsoap.store.util.UserType;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.util.*;
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

    @Autowired
    private final AuthUtil  authUtil;

    @Autowired
    private final RefreshTokenServInt refreshTokenServ;

    @Value("${frontend.base.url}")
    private String frontendUrl;

    private String username;
    private String idAttributeKey;
    private LoginSource signUpSource = null;

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

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    private String fetchPhoneNumber(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://people.googleapis.com/v1/people/me?personFields=phoneNumbers";

        try {
            ResponseEntity<JsonNode> response = restTemplate
                    .exchange(url, HttpMethod.GET, entity, JsonNode.class);
            JsonNode phoneNumbers = Objects.requireNonNull(response.getBody())
                    .path("phoneNumbers");
            if (phoneNumbers.isArray() && !phoneNumbers.isEmpty()) {
                return phoneNumbers.get(0).path("value").asText();
            }
        } catch (Exception e) {
            logger.error("Failed to fetch phone number from Google People API", e);
        }
        return null;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws ServletException, IOException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken
                = (OAuth2AuthenticationToken) authentication;
        String registrationId = oAuth2AuthenticationToken
                .getAuthorizedClientRegistrationId();


        String oAuth2 = registrationId.toUpperCase();
        LoginSource loginSource = LoginSource.valueOf(oAuth2);
        var oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oauth2User.getAttributes();

        if (loginSource==LoginSource.GOOGLE
                || loginSource==LoginSource.NAVER) {

            if (loginSource==LoginSource.NAVER) {
                attributes = (Map<String, Object>) attributes.get("response");
            }
            String name = attributes.getOrDefault("name", "").toString();
            String email = attributes.getOrDefault("email", "").toString();

            switch (loginSource) {
                case LoginSource.GOOGLE -> {
                    username = email.split("@")[0];
                    idAttributeKey = "sub";
                }
                case LoginSource.NAVER -> {
                    username = attributes.getOrDefault("nickname", "").toString();
                    idAttributeKey = "id";
                }
                default -> {
                }
            }

            System.out.println("attrs: " + email + ", " + username);
            final Map<String, Object> finalAttributes = attributes;

            try {
                var user = userService.getBsUserByEmail(email);
                // 등록된 유저의 OAuth 2 로그인 처리
                if (user.isEnabled()) {
                    Collection<Role> roles = user.getRoles();
                    Role firstRole = roles.iterator().next();
                    putAuth2Context(firstRole.getName(),
                            finalAttributes, idAttributeKey, oAuth2);
                    username = user.getEmail();
                    this.signUpSource =
                            LoginSource.valueOf(user.getSignUpMethod());
                    redirectWithJwt(user, oauth2User, loginSource, response);
                } else {
                    redirectToLogin(user.getEmail());
                }
            } catch (AccountNotFoundException e) {
                // 이메일이 DB 에 부재인 경우 처리
                Customer customer = new Customer();
                customer.setFullName(name);

                String userName = oAuth2AuthenticationToken.getName();
                OAuth2AuthorizedClient authorizedClient = authorizedClientService
                        .loadAuthorizedClient(registrationId, userName);
                String accessToken = authorizedClient.getAccessToken().getTokenValue();
                String mbPhone = fetchPhoneNumber(accessToken);

                if (mbPhone==null || mbPhone.isEmpty()) {
                    mbPhone = "01000000000";
                }
                customer.setMbPhone(mbPhone);
                customer.setEmail(email);
                customer.setUserType(UserType.CUSTOMER);
                customer.setRoles(
                        Set.of(roleServ.findByName("ROLE_CUSTOMER")));
                customer.setEnabled(true);
                customer.setSignUpMethod(loginSource.toString());

                BsUser user = customerServ.add(customer, false);

                putAuth2Context("ROLE_CUSTOMER",
                        finalAttributes, idAttributeKey, oAuth2);
                this.signUpSource = LoginSource.valueOf(oAuth2);
                redirectWithJwt(user, oauth2User, loginSource, response);
            }
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

    /**
     * 전단 URL 에 착륙하되, URL에는 JWT 토큰을 붙여서 보낸다.
     *
     * @param user
     * @param oAuth2User
     */
    private void redirectWithJwt(BsUser user,
                                 DefaultOAuth2User oAuth2User,
                                 LoginSource loginSource,
                                 HttpServletResponse response) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        Set<GrantedAuthority> authorities =
                oAuth2User.getAuthorities().stream()
                        .map(authority -> new SimpleGrantedAuthority(
                                authority.getAuthority()))
                        .collect(Collectors.toSet());

        Collection<Role> roles = user.getRoles();
        String firstRoleStr = roles.iterator().next().toString();
        authorities.add(new SimpleGrantedAuthority(firstRoleStr));

        BsUserDetails userDetails = new BsUserDetails(
                user.getId(), user.getEmail(), null, user.getFullName(),
                true, authorities, signUpSource.toString(),
                loginSource.getLabel(), user.isTwoFaEnabled(), user.getMbPhone());

        this.setAlwaysUseDefaultTargetUrl(true);

        // 리프레시 토큰 생성 및 DB 저장
        var refresh = refreshTokenServ.createRefreshForUser(user.getId());
        ResponseCookie refreshCookie = authUtil.createSaveCookie(refresh);

        // Generate JWT token
        String jwtToken = jwtUtilBean.generateTokenForUser(userDetails);

        // Redirect to the frontend with the JWT token
        String targetUrl = UriComponentsBuilder.fromUriString(
                        frontendUrl + "/oauth2/redirect")
                .queryParam("token", jwtToken)
                .build().toUriString();

        // ★ 응답에 Refresh Cookie 추가
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        this.setDefaultTargetUrl(targetUrl);
        this.setAlwaysUseDefaultTargetUrl(true);
    }

    private final TokenCache tokenCache;

    private void redirectToLogin(String email) {
        String tempToken = tokenCache.put(email, 300);

        // Redirect to the frontend with the email address
        String targetUrl = UriComponentsBuilder.fromUriString(
                        frontendUrl + "/login")
                .queryParam("token", tempToken)
                .build().toUriString();
        this.setDefaultTargetUrl(targetUrl);
    }
}