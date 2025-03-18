package com.bumsoap.store.config;

import com.bumsoap.store.security.jwt.AuthTokenFilter;
import com.bumsoap.store.security.jwt.BsJwtErrorEntry;
import com.bumsoap.store.security.user.BsUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
