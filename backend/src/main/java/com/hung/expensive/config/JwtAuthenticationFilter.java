package com.hung.expensive.config;

import com.hung.expensive.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Why: JWT Authentication Filter để validate JWT token trong mỗi request
 * Design decision: OncePerRequestFilter để chỉ execute một lần per request
 * Security: Extract JWT từ Authorization header và validate
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        // Why: Extract JWT token từ Authorization header
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        
        // Why: Check Authorization header format (Bearer token)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Why: Extract token từ "Bearer " prefix
        jwt = authHeader.substring(7);
        
        try {
            // Why: Extract username từ JWT token
            username = jwtService.extractUsername(jwt);
            
            // Why: Check nếu user chưa được authenticate
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Why: Load user details từ database
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // Why: Validate JWT token với user details
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // Why: Create authentication token
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                    
                    // Why: Set authentication details
                    authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    
                    // Why: Set authentication vào SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Why: Log error và continue filter chain
            logger.error("Cannot set user authentication: {}", e);
        }
        
        // Why: Continue filter chain
        filterChain.doFilter(request, response);
    }
}

