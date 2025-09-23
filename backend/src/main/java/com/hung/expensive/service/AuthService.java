package com.hung.expensive.service;

import com.hung.expensive.dto.AuthResponse;
import com.hung.expensive.dto.LoginRequest;
import com.hung.expensive.dto.RegisterRequest;
import com.hung.expensive.entity.User;
import com.hung.expensive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Why: Service layer chứa business logic cho authentication
 * Design decision: Tách biệt business logic khỏi Controller
 * Context: Handle user registration, login và JWT token generation
 */
@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    /**
     * Why: Register user mới với validation và password encoding
     * Business rule: Check duplicate username/email trước khi tạo
     * Security: Hash password trước khi lưu vào database
     * 
     * @param request RegisterRequest từ client
     * @return AuthResponse với JWT token
     * @throws RuntimeException nếu username/email đã tồn tại
     */
    public AuthResponse register(RegisterRequest request) {
        // Why: Check duplicate username trước khi tạo user
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        }
        
        // Why: Check duplicate email trước khi tạo user
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }
        
        // Why: Tạo user mới với password đã được hash
        User user = new User(
            request.getUsername(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword())
        );
        
        // Why: Lưu user vào database
        User savedUser = userRepository.save(user);
        
        // Why: Generate JWT token cho user mới
        String token = jwtService.generateToken(savedUser.getUsername());
        
        // Why: Return response với token và user info
        return new AuthResponse(
            token,
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getEmail()
        );
    }
    
    /**
     * Why: Login user với authentication và JWT token generation
     * Business rule: Validate credentials trước khi generate token
     * Security: Sử dụng Spring Security AuthenticationManager
     * 
     * @param request LoginRequest từ client
     * @return AuthResponse với JWT token
     * @throws RuntimeException nếu credentials không hợp lệ
     */
    public AuthResponse login(LoginRequest request) {
        // Why: Authenticate user với Spring Security
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );
        
        // Why: Set authentication vào SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Why: Lấy user details từ authentication
        User user = (User) authentication.getPrincipal();
        
        // Why: Generate JWT token
        String token = jwtService.generateToken(user.getUsername());
        
        // Why: Return response với token và user info
        return new AuthResponse(
            token,
            user.getId(),
            user.getUsername(),
            user.getEmail()
        );
    }
}

