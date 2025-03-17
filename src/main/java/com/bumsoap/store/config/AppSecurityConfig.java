package com.bumsoap.store.config;

import com.bumsoap.store.security.jwt.AuthTokenFilter;
import com.bumsoap.store.security.jwt.BsJwtErrorEntry;
import com.bumsoap.store.security.user.BsUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AppSecurityConfig {
    private final BsUserDetailsService userDetailsService;
    private final BsJwtErrorEntry bsJwtErrorEntry;
    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }
}
