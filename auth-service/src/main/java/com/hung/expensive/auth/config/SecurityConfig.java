package com.hung.expensive.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Why: Security Configuration cho Auth Service
 * Design decision: Stateless JWT authentication, không cần session
 * Business requirement: Secure endpoints, password encoding, CORS support
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Why: Password Encoder bean - BCrypt là industry standard
     * Design decision: BCrypt với salt tự động, secure và widely used
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Why: Security Filter Chain configuration
     * Design decision: Stateless JWT, permit all auth endpoints, disable CSRF
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Why: Disable CSRF vì dùng JWT stateless authentication
            .csrf(csrf -> csrf.disable())
            
            // Why: Configure CORS để allow frontend calls
            .cors(cors -> cors.configure(http))
            
            // Why: Stateless session management cho JWT
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Why: Configure authorization rules
            .authorizeHttpRequests(authz -> authz
                // Why: Permit all auth endpoints - public access
                .requestMatchers("/api/auth/**").permitAll()
                // Why: Permit actuator endpoints cho monitoring
                .requestMatchers("/actuator/**").permitAll()
                // Why: Permit health check endpoint
                .requestMatchers("/health").permitAll()
                // Why: All other endpoints require authentication
                .anyRequest().authenticated()
            );

        return http.build();
    }
}