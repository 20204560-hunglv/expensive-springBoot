#!/bin/bash

# Why: Health check script cho tất cả services
# Design decision: Automated health monitoring để đảm bảo services running properly
# Business requirement: Cần monitoring để detect issues early

set -e

echo "🏥 Checking service health..."

# Why: Function để check service health
check_service() {
    local service_name=$1
    local url=$2
    local max_attempts=10
    local attempt=1
    
    echo "🔍 Checking $service_name..."
    
    while [ $attempt -le $max_attempts ]; do
        if curl -f -s "$url" > /dev/null; then
            echo "✅ $service_name is healthy"
            return 0
        fi
        
        echo "⏳ $service_name not ready yet (attempt $attempt/$max_attempts)..."
        sleep 5
        ((attempt++))
    done
    
    echo "❌ $service_name failed health check after $max_attempts attempts"
    return 1
}

# Why: Check tất cả services
check_service "Service Discovery" "http://localhost:8761/actuator/health"
check_service "Config Service" "http://localhost:8888/actuator/health"
check_service "API Gateway" "http://localhost:8080/actuator/health"
check_service "Auth Service" "http://localhost:8081/actuator/health"
check_service "User Service" "http://localhost:8082/actuator/health"
check_service "Expense Service" "http://localhost:8083/actuator/health"
check_service "Category Service" "http://localhost:8084/actuator/health"

echo "🎉 All services are healthy!"
