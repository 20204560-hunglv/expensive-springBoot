#!/bin/bash

# Why: Build script cho tất cả microservices
# Design decision: Centralized build process để đảm bảo consistency
# Business requirement: Cần build process nhanh chóng và reliable

set -e

echo "🚀 Building Expensive Project Microservices..."

# Why: Clean và build parent project
echo "📦 Building parent project..."
./mvnw clean install -DskipTests

# Why: Build từng service
echo "🔨 Building service-discovery..."
./mvnw clean package -pl service-discovery -am -DskipTests

echo "🔨 Building config-service..."
./mvnw clean package -pl config-service -am -DskipTests

echo "🔨 Building api-gateway..."
./mvnw clean package -pl api-gateway -am -DskipTests

echo "🔨 Building auth-service..."
./mvnw clean package -pl auth-service -am -DskipTests

echo "🔨 Building user-service..."
./mvnw clean package -pl user-service -am -DskipTests

echo "🔨 Building expense-service..."
./mvnw clean package -pl expense-service -am -DskipTests

echo "🔨 Building category-service..."
./mvnw clean package -pl category-service -am -DskipTests

echo "✅ All services built successfully!"
echo "🐳 Ready for Docker build..."
