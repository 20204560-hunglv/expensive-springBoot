# Why: Multi-stage build để tối ưu image size
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Why: Set working directory
WORKDIR /app

# Why: Copy parent POM và shared module trước
COPY pom.xml .
COPY shared/pom.xml ./shared/
COPY auth-service/pom.xml ./auth-service/

# Why: Download dependencies (cached layer)
RUN mvn dependency:go-offline -B

# Why: Copy source code
COPY shared/src ./shared/src
COPY auth-service/src ./auth-service/src

# Why: Build application
RUN mvn clean package -DskipTests

# Why: Runtime stage với lightweight JRE
FROM eclipse-temurin:21-jre-alpine

# Why: Install curl cho health check
RUN apk add --no-cache curl

# Why: Create non-root user cho security
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Why: Set working directory
WORKDIR /app

# Why: Copy built JAR
COPY --from=build /app/auth-service/target/*.jar app.jar

# Why: Change ownership
RUN chown -R appuser:appgroup /app

# Why: Switch to non-root user
USER appuser

# Why: Expose port
EXPOSE 8081

# Why: Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8081/actuator/health || exit 1

# Why: Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
