package com.hung.expensive.config;

import com.hung.expensive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Why: Security Configuration cho Spring Security
 * Design decision: Stateless authentication với JWT
 * Context: Configure security rules và authentication providers
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Why: Security Filter Chain để configure security rules
     * Context: Define which endpoints cần authentication
     * Security: Disable CSRF vì dùng JWT stateless
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Why: Disable CSRF vì dùng JWT stateless authentication
            .csrf(csrf -> csrf.disable())
            
            // Why: Configure authorization rules
            .authorizeHttpRequests(auth -> auth
                // Why: Public endpoints không cần authentication
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll() // Why: H2 console cho development
                .requestMatchers("/").permitAll()
                .requestMatchers("/static/**").permitAll()
                
                // Why: Tất cả endpoints khác cần authentication
                .anyRequest().authenticated()
            )
            
            // Why: Stateless session management cho JWT
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Why: Add JWT filter trước UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    /**
     * Why: Authentication Provider để authenticate users
     * Context: Configure DaoAuthenticationProvider với UserDetailsService và PasswordEncoder
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    /**
     * Why: UserDetailsService để load user details từ database
     * Context: Custom implementation để load User entity
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User không tồn tại"));
    }
    
    /**
     * Why: Password Encoder để hash passwords
     * Context: BCrypt là industry standard cho password hashing
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Why: Authentication Manager để handle authentication
     * Context: Required cho authentication trong AuthService
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    /**
     * Why: JwtAuthenticationFilter bean để tránh circular dependency
     * Context: Tạo filter instance với dependencies được inject
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}
