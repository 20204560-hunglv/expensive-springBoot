package com.hung.expensive.auth.service;

import com.hung.expensive.dto.AuthResponse;
import com.hung.expensive.dto.LoginRequest;
import com.hung.expensive.dto.RegisterRequest;
import com.hung.expensive.entity.User;
import com.hung.expensive.auth.repository.UserRepository;
import com.hung.expensive.auth.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Why: Auth Service chứa business logic cho authentication
 * Design decision: Service layer pattern để tách biệt business logic
 * Business requirement: Handle registration, login, token validation
 */
@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Why: Constructor injection - best practice cho dependency injection
    public AuthService(UserRepository userRepository, 
                      PasswordEncoder passwordEncoder, 
                      JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Why: Register new user với validation và password hashing
     * Business rule: Check duplicate email/username, hash password, generate token
     */
    public AuthResponse register(RegisterRequest request) {
        // Why: Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Why: Create new user với hashed password
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Why: Hash password trước khi lưu
        
        User savedUser = userRepository.save(user);

        // Why: Generate JWT token cho user mới
        String token = jwtService.generateToken(savedUser.getUsername());

        return AuthResponse.builder()
                .token(token)
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .userId(savedUser.getId())
                .id(savedUser.getId())
                .build();
    }

    /**
     * Why: Login existing user với credential validation
     * Business rule: Validate password, generate new token
     */
    public AuthResponse login(LoginRequest request) {
        // Why: Find user by username hoặc email
        Optional<User> userOpt = userRepository.findByUsername(request.getUsernameOrEmail())
                .or(() -> userRepository.findByEmail(request.getUsernameOrEmail()));

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid credentials");
        }

        User user = userOpt.get();

        // Why: Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Why: Check if user is enabled
        if (!user.getEnabled()) {
            throw new RuntimeException("Account is disabled");
        }

        // Why: Generate JWT token
        String token = jwtService.generateToken(user.getUsername());

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .email(user.getEmail())
                .userId(user.getId())
                .id(user.getId())
                .build();
    }

    /**
     * Why: Validate JWT token và return user info
     * Business rule: Check token validity, return user details
     */
    public AuthResponse validateToken(String token) {
        try {
            // Why: Extract username từ token
            String username = jwtService.extractUsername(token);
            
            // Why: Check if token is valid và user exists
            if (jwtService.isTokenValid(token, username)) {
                Optional<User> userOpt = userRepository.findByUsername(username);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    return AuthResponse.builder()
                            .token(token)
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .userId(user.getId())
                            .id(user.getId())
                            .build();
                }
            }
            
            throw new RuntimeException("Invalid token");
        } catch (Exception e) {
            throw new RuntimeException("Token validation failed: " + e.getMessage());
        }
    }
}