package com.hung.expensive.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Why: Service để handle JWT token operations trong microservice
 * Design decision: Centralized JWT logic để dễ maintain và secure
 * Security: Sử dụng HMAC SHA256 với secret key
 * Migration: Giữ nguyên logic từ monolithic app
 */
@Service
public class JwtService {
    
    // Why: JWT secret key từ application.properties
    @Value("${jwt.secret:mySecretKey}")
    private String secretKey;
    
    // Why: JWT expiration time (24 hours)
    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;
    
    /**
     * Why: Generate JWT token cho user
     * Context: Tạo token với username và expiration time
     * Migration: Giữ nguyên logic từ monolithic app
     * 
     * @param username username của user
     * @return JWT token string
     */
    public String generateToken(String username) {
        return generateToken(new HashMap<>(), username);
    }
    
    /**
     * Why: Generate JWT token với extra claims
     * Context: Có thể thêm custom claims nếu cần
     * Migration: Giữ nguyên logic từ monolithic app
     * 
     * @param extraClaims additional claims
     * @param username username của user
     * @return JWT token string
     */
    public String generateToken(Map<String, Object> extraClaims, String username) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Why: Extract username từ JWT token
     * Context: Dùng để identify user từ token
     * Migration: Giữ nguyên logic từ monolithic app
     * 
     * @param token JWT token
     * @return username
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * Why: Extract expiration date từ JWT token
     * Context: Check token có expired chưa
     * Migration: Giữ nguyên logic từ monolithic app
     * 
     * @param token JWT token
     * @return expiration date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     * Why: Extract specific claim từ JWT token
     * Context: Generic method để extract bất kỳ claim nào
     * Migration: Giữ nguyên logic từ monolithic app
     * 
     * @param token JWT token
     * @param claimsResolver function để extract claim
     * @return claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Why: Extract tất cả claims từ JWT token
     * Context: Parse token và return claims object
     * Migration: Giữ nguyên logic từ monolithic app
     * 
     * @param token JWT token
     * @return Claims object
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    /**
     * Why: Check token có expired chưa
     * Context: Validate token trước khi sử dụng
     * Migration: Giữ nguyên logic từ monolithic app
     * 
     * @param token JWT token
     * @return true nếu token expired
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    /**
     * Why: Validate token với user details
     * Context: Check token có hợp lệ và match với user không
     * Migration: Giữ nguyên logic từ monolithic app
     * 
     * @param token JWT token
     * @param userDetails UserDetails object
     * @return true nếu token valid
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    
    /**
     * Why: Validate token với username string
     * Context: Overload method để support string username
     */
    public boolean isTokenValid(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username)) && !isTokenExpired(token);
    }
    
    /**
     * Why: Get signing key từ secret
     * Context: Convert string secret thành SecretKey object
     * Migration: Giữ nguyên logic từ monolithic app
     * 
     * @return SecretKey object
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
