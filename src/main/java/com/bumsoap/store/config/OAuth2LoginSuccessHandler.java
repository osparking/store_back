package com.bumsoap.store.config;

import com.bumsoap.store.repository.RoleRepoI;
import com.bumsoap.store.security.jwt.JwtUtilBean;
import com.bumsoap.store.service.user.UserServInt;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler
    extends SavedRequestAwareAuthenticationSuccessHandler {

  @Autowired
  private final UserServInt userService;

  @Autowired
  private final JwtUtilBean jwtUtilBean;

  @Autowired
  RoleRepoI roleRepository;

  @Value("${frontend.base.url}")
  private String frontendUrl;

  String username;
  String idAttributeKey;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication)
      throws ServletException, IOException
  {
    return;
  }

}