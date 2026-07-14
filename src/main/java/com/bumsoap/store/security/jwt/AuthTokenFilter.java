package com.bumsoap.store.security.jwt;

import com.bumsoap.store.security.user.BsUserDetailsService;
import com.bumsoap.store.util.AuthUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
  @Autowired
  private JwtUtilBean jwtUtilBean;
  @Autowired
  private BsUserDetailsService bsUserDetailsService;
  @Autowired
  private AuthUtil authUtil;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String jwt = authUtil.getJwtFromRequest(request);

      if (jwt != null && jwtUtilBean.validateToken(jwt)) {
        String email = jwtUtilBean.getUsernameFrom(jwt);
        var details = bsUserDetailsService.loadUserByUsername(email);
        var authentication = new UsernamePasswordAuthenticationToken(
            details, null, details.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource()
            .buildDetails(request));
        SecurityContextHolder.getContext()
            .setAuthentication(authentication);
      }
    } catch (JwtException e) { // JwtException을 정확히 캐치
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
      response.setContentType("application/json;charset=UTF-8");
      String cause = null;
      if (e instanceof ExpiredJwtException) {
        cause = "JWT가 만료되었습니다.";
      } else {
        cause = e.getMessage();
      }
      response.getWriter().write("{\"code\":\"UNAUTHORIZED\", \"message\":\""
              + cause + "\"}");
      response.getWriter().flush();
      return;
    } catch (Exception e) {
      throw new ServletException(e.getMessage());
    }
    filterChain.doFilter(request, response);
  }

}
