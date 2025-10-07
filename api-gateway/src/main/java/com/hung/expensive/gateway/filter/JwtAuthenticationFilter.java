package com.hung.expensive.gateway.filter;

import com.hung.expensive.gateway.service.JwtService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Why: JWT Authentication Filter cho API Gateway
 * Design decision: Custom Gateway Filter để validate JWT tokens
 * Business requirement: Secure protected endpoints, validate tokens
 */
@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            // Why: Check if request has Authorization header
            String authHeader = request.getHeaders().getFirst("Authorization");
            
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return handleUnauthorized(exchange, "Missing or invalid Authorization header");
            }

            try {
                // Why: Extract JWT token
                String token = authHeader.substring(7); // Remove "Bearer " prefix
                
                // Why: Validate token với Auth Service
                if (jwtService.validateToken(token)) {
                    // Why: Add user info to request headers for downstream services
                    String username = jwtService.extractUsername(token);
                    ServerHttpRequest modifiedRequest = request.mutate()
                            .header("X-User-Id", username)
                            .header("X-User-Name", username)
                            .build();
                    
                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                } else {
                    return handleUnauthorized(exchange, "Invalid token");
                }
            } catch (Exception e) {
                return handleUnauthorized(exchange, "Token validation failed: " + e.getMessage());
            }
        };
    }

    /**
     * Why: Handle unauthorized requests
     * Business rule: Return 401 status với error message
     */
    private Mono<Void> handleUnauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json");
        
        String body = String.format("{\"error\":\"Unauthorized\",\"message\":\"%s\"}", message);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    /**
     * Why: Configuration class cho filter
     */
    public static class Config {
        // Why: Configuration properties nếu cần
    }
}
