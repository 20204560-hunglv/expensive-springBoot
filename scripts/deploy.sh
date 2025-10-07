#!/bin/bash

# Why: Deployment script cho development environment
# Design decision: Automated deployment để giảm manual errors
# Business requirement: Cần deployment process nhanh chóng và consistent

set -e

echo "🚀 Deploying Expensive Project Microservices..."

# Why: Build all services
echo "📦 Building all services..."
./scripts/build.sh

# Why: Stop existing containers
echo "🛑 Stopping existing containers..."
docker-compose down

# Why: Remove old images
echo "🧹 Cleaning up old images..."
docker system prune -f

# Why: Build và start all services
echo "🐳 Building and starting Docker containers..."
docker-compose up --build -d

# Why: Wait for services to be ready
echo "⏳ Waiting for services to start..."
sleep 30

# Why: Check service health
echo "🏥 Checking service health..."
./scripts/health-check.sh

echo "✅ Deployment completed successfully!"
echo "🌐 Services available at:"
echo "   - API Gateway: http://localhost:8080"
echo "   - Service Discovery: http://localhost:8761"
echo "   - Config Service: http://localhost:8888"
echo "   - Auth Service: http://localhost:8081"
echo "   - User Service: http://localhost:8082"
echo "   - Expense Service: http://localhost:8083"
echo "   - Category Service: http://localhost:8084"
