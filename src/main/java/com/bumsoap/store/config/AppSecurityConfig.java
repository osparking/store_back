package com.bumsoap.store.config;

import com.bumsoap.store.security.jwt.AuthTokenFilter;
import com.bumsoap.store.security.jwt.BsJwtErrorEntry;
import com.bumsoap.store.security.user.BsUserDetailsService;
import com.bumsoap.store.security.user.PrincipalOauthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.security.Principal;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class AppSecurityConfig {
    private final BsUserDetailsService userDetailsService;
    private final BsJwtErrorEntry bsJwtErrorEntry;
    private final PrincipalOauthUserService pOUserService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .exceptionHandling(exception
                        -> exception.authenticationEntryPoint(bsJwtErrorEntry))
                .sessionManagement(session
                        -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/s1/user/add")
                        .permitAll()
                        .requestMatchers("/api/s1/admin/**")
                        .hasAuthority("ROLE_ADMIN")
                        .requestMatchers(URLS)
                        .authenticated()
                        .anyRequest().permitAll())
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("http://localhost:5173", true)
                        .userInfoEndpoint(userInfo -> userInfo.userService(pOUserService)));
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(
                authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private static  String[] URLS = {
            "/api/s1/user/**",
            "/api/s1/photo/**"
    };

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173/")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
