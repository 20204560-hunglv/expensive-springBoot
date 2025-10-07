package com.hung.expensive.gateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * Why: JWT Service cho API Gateway
 * Design decision: Validate JWT tokens bằng cách gọi Auth Service
 * Business requirement: Secure API Gateway, validate tokens
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final WebClient webClient;

    public JwtService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * Why: Validate JWT token bằng cách gọi Auth Service
     * Business rule: Centralized token validation
     */
    public boolean validateToken(String token) {
        try {
            // Why: Call Auth Service để validate token
            return webClient.post()
                    .uri("http://auth-service:8081/api/auth/validate")
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (Exception e) {
            // Why: Fallback to local validation nếu Auth Service không available
            return validateTokenLocally(token);
        }
    }

    /**
     * Why: Extract username từ JWT token
     * Business rule: Get user info từ token
     */
    public String extractUsername(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract username from token");
        }
    }

    /**
     * Why: Local token validation fallback
     * Business rule: Validate token locally nếu Auth Service down
     */
    private boolean validateTokenLocally(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().after(new java.util.Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Why: Extract all claims từ JWT token
     * Business rule: Parse JWT token để get claims
     */
    private Claims extractAllClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
