package com.hung.expensive.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * Why: REST API endpoint cho home page
 * Context: Return JSON response thay vì static file
 */
@RestController
public class HomeController {
    
    /**
     * Why: API health check và basic info
     * Context: Frontend có thể consume API này
     */
    @GetMapping("/")
    public Map<String, Object> home() {
        return Map.of(
            "message", "Expensive Tracker API",
            "version", "1.0.0",
            "status", "running",
            "timestamp", System.currentTimeMillis()
        );
    }
}
