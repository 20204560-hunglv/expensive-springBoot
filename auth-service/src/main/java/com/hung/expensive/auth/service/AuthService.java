package com.hung.expensive.auth.service;

import com.hung.expensive.auth.dto.AuthResponse;
import com.hung.expensive.auth.dto.LoginRequest;
import com.hung.expensive.auth.dto.RegisterRequest;
import com.hung.expensive.auth.entity.User;
import com.hung.expensive.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Auth Service đơn giản - không có JWT, Security phức tạp
 */
@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Đăng ký user mới
     */
    public AuthResponse register(RegisterRequest request) {
        // Kiểm tra username đã tồn tại chưa
        if (userRepository.existsByUsername(request.getUsername())) {
            return AuthResponse.error("Username đã tồn tại");
        }
        
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(request.getEmail())) {
            return AuthResponse.error("Email đã tồn tại");
        }
        
        // Tạo user mới (password chưa hash - đơn giản cho học tập)
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // TODO: Hash password trong tương lai
        
        // Lưu vào database
        User savedUser = userRepository.save(user);
        
        return AuthResponse.success(
            "Đăng ký thành công", 
            savedUser.getId(), 
            savedUser.getUsername(), 
            savedUser.getEmail()
        );
    }
    
    /**
     * Đăng nhập
     */
    public AuthResponse login(LoginRequest request) {
        // Tìm user theo username
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        
        if (userOpt.isEmpty()) {
            return AuthResponse.error("Username không tồn tại");
        }
        
        User user = userOpt.get();
        
        // Kiểm tra password (đơn giản - chưa hash)
        if (!user.getPassword().equals(request.getPassword())) {
            return AuthResponse.error("Password không đúng");
        }
        
        return AuthResponse.success(
            "Đăng nhập thành công", 
            user.getId(), 
            user.getUsername(), 
            user.getEmail()
        );
    }
}