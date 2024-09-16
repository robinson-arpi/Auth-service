package com.merge_conflict.AuthModule.Config;

import com.merge_conflict.AuthModule.AuthJwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthFilter jwtAuthenticationFilter;
        private final AuthenticationProvider authProvider;

        // Access to API
        private static final String[] SWAGGER_WHITELIST = {
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/swagger-resources",
                        "/api/candidates/**",
                        "/webjars/**"
        };

        // Access to login an register
        private static final String[] AUTH = {
                        "/auth/**"
        };

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                        .csrf(csrf -> csrf.disable())
                        .authorizeRequests(authRequest -> authRequest
                                        .requestMatchers(AUTH).permitAll()
                                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                                        .anyRequest().authenticated())
                        .sessionManagement(sessionManager -> sessionManager
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .authenticationProvider(authProvider)
                        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                        .build();
        }

}