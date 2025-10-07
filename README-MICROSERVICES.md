# Expensive Project - Microservices Architecture

## 🏗️ Kiến Trúc Tổng Quan

Project này được tái cấu trúc từ monolithic application thành microservices architecture sử dụng Spring Boot 3+ và Spring Cloud.

### 📊 Sơ Đồ Kiến Trúc

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Mobile App    │    │   Third Party   │
│   (React/Vue)   │    │   (React Native)│    │   Services      │
└─────────┬───────┘    └─────────┬───────┘    └─────────┬───────┘
          │                      │                      │
          └──────────────────────┼──────────────────────┘
                                 │
                    ┌─────────────▼─────────────┐
                    │      API Gateway          │
                    │   (Spring Cloud Gateway)  │
                    │   Port: 8080              │
                    └─────────────┬─────────────┘
                                  │
        ┌─────────────────────────┼─────────────────────────┐
        │                         │                         │
┌───────▼───────┐    ┌───────────▼───────────┐    ┌───────▼───────┐
│ Auth Service  │    │    User Service       │    │ Expense Service│
│ Port: 8081    │    │    Port: 8082         │    │ Port: 8083    │
│ - Login       │    │ - Profile Management  │    │ - CRUD Expenses│
│ - Register    │    │ - User Preferences    │    │ - Reports      │
│ - JWT Token   │    │ - Settings            │    │ - Analytics    │
└───────┬───────┘    └───────────┬───────────┘    └───────┬───────┘
        │                        │                        │
        └────────────────────────┼────────────────────────┘
                                 │
                    ┌─────────────▼─────────────┐
                    │   Category Service        │
                    │   Port: 8084              │
                    │ - CRUD Categories         │
                    │ - Category Analytics      │
                    └─────────────┬─────────────┘
                                  │
        ┌─────────────────────────┼─────────────────────────┐
        │                         │                         │
┌───────▼───────┐    ┌───────────▼───────────┐    ┌───────▼───────┐
│ Service       │    │   Config Service      │    │   Database    │
│ Discovery     │    │   Port: 8888          │    │   (MySQL)     │
│ (Eureka)      │    │ - Centralized Config  │    │   Port: 3306  │
│ Port: 8761    │    │ - Environment Config  │    │               │
└───────────────┘    └───────────────────────┘    └───────────────┘
```

## 🚀 Các Service

### 1. **Service Discovery (Eureka Server)**
- **Port**: 8761
- **Chức năng**: Service registry và discovery
- **URL**: http://localhost:8761

### 2. **Config Service**
- **Port**: 8888
- **Chức năng**: Centralized configuration management
- **URL**: http://localhost:8888

### 3. **API Gateway**
- **Port**: 8080
- **Chức năng**: Entry point, routing, load balancing, authentication
- **URL**: http://localhost:8080

### 4. **Auth Service**
- **Port**: 8081
- **Chức năng**: Authentication, authorization, JWT token management
- **Database**: MySQL (expensive_auth)

### 5. **User Service**
- **Port**: 8082
- **Chức năng**: User profile management, preferences, settings
- **Database**: MySQL (expensive_user)

### 6. **Expense Service**
- **Port**: 8083
- **Chức năng**: Expense CRUD, reporting, analytics
- **Database**: MySQL (expensive_expense)

### 7. **Category Service**
- **Port**: 8084
- **Chức năng**: Category management, category analytics
- **Database**: MySQL (expensive_category)

## 🛠️ Công Nghệ Sử Dụng

### Core Technologies
- **Java**: 21
- **Spring Boot**: 3.5.3
- **Spring Cloud**: 2024.0.0
- **Spring Security**: JWT authentication
- **Spring Data JPA**: Database operations
- **MySQL**: 8.0 (Database)

### Spring Cloud Components
- **Spring Cloud Gateway**: API Gateway
- **Netflix Eureka**: Service Discovery
- **Spring Cloud Config**: Configuration Management
- **OpenFeign**: Service-to-service communication
- **Spring Cloud LoadBalancer**: Load balancing

### Infrastructure
- **Docker**: Containerization
- **Docker Compose**: Local development orchestration
- **Maven**: Build tool
- **Actuator**: Health checks và monitoring

## 🚀 Quick Start

### Prerequisites
- Java 21+
- Maven 3.9+
- Docker & Docker Compose
- MySQL 8.0+

### 1. Clone Repository
```bash
git clone <repository-url>
cd expensive-project-backend
```

### 2. Setup Database
```bash
# Start MySQL
docker run -d --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password mysql:8.0

# Create databases
mysql -u root -p -e "CREATE DATABASE expensive_auth;"
mysql -u root -p -e "CREATE DATABASE expensive_user;"
mysql -u root -p -e "CREATE DATABASE expensive_expense;"
mysql -u root -p -e "CREATE DATABASE expensive_category;"
```

### 3. Build và Deploy
```bash
# Build all services
./scripts/build.sh

# Deploy với Docker Compose
./scripts/deploy.sh

# Check health
./scripts/health-check.sh
```

### 4. Manual Deployment (Development)
```bash
# Start services theo thứ tự
# 1. Service Discovery
cd service-discovery && mvn spring-boot:run

# 2. Config Service
cd config-service && mvn spring-boot:run

# 3. Auth Service
cd auth-service && mvn spring-boot:run

# 4. User Service
cd user-service && mvn spring-boot:run

# 5. Expense Service
cd expense-service && mvn spring-boot:run

# 6. Category Service
cd category-service && mvn spring-boot:run

# 7. API Gateway
cd api-gateway && mvn spring-boot:run
```

## 📡 API Endpoints

### API Gateway (Port 8080)
- **Auth**: `POST /api/auth/login`, `POST /api/auth/register`
- **Users**: `GET /api/users/profile`, `PUT /api/users/profile`
- **Expenses**: `GET /api/expenses`, `POST /api/expenses`
- **Categories**: `GET /api/categories`, `POST /api/categories`

### Service Discovery (Port 8761)
- **Dashboard**: http://localhost:8761
- **Health**: http://localhost:8761/actuator/health

### Config Service (Port 8888)
- **Health**: http://localhost:8888/actuator/health
- **Config**: http://localhost:8888/{service-name}/{profile}

## 🔧 Configuration

### Environment Variables
```bash
# Database
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_USERNAME=root
MYSQL_PASSWORD=password

# JWT
JWT_SECRET=mySecretKey123456789012345678901234567890
JWT_EXPIRATION=86400000

# Eureka
EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://localhost:8761/eureka/
```

### Profiles
- **dev**: Development environment
- **test**: Testing environment
- **prod**: Production environment
- **docker**: Docker environment

## 🐳 Docker Commands

```bash
# Build all images
docker-compose build

# Start all services
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs -f [service-name]

# Scale service
docker-compose up -d --scale expense-service=3
```

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run specific service tests
mvn test -pl auth-service

# Integration tests
mvn verify -P integration-tests
```

## 📊 Monitoring

### Health Checks
- Tất cả services có `/actuator/health` endpoint
- API Gateway có `/actuator/gateway/routes` để xem routing

### Logging
- Centralized logging với structured format
- Log levels có thể configure qua config service

## 🔒 Security

### Authentication Flow
1. Client gửi credentials đến `/api/auth/login`
2. Auth Service validate và trả về JWT token
3. Client sử dụng JWT token trong header `Authorization: Bearer <token>`
4. API Gateway validate JWT token trước khi route request

### JWT Configuration
- **Secret**: Configurable qua environment variable
- **Expiration**: 24 hours (configurable)
- **Algorithm**: HS256

## 🚀 Deployment

### Development
```bash
./scripts/deploy.sh
```

### Production
```bash
# Build production images
docker-compose -f docker-compose.prod.yml build

# Deploy to production
docker-compose -f docker-compose.prod.yml up -d
```

## 📈 Scaling

### Horizontal Scaling
```bash
# Scale expense service
docker-compose up -d --scale expense-service=3

# Scale với load balancer
docker-compose up -d --scale user-service=2 --scale expense-service=3
```

### Database Scaling
- Mỗi service có database riêng
- Có thể scale database độc lập
- Consider read replicas cho reporting services

## 🛠️ Development

### Adding New Service
1. Tạo module mới trong parent POM
2. Add dependency vào shared module
3. Implement service với Spring Boot
4. Add Eureka Client annotation
5. Update API Gateway routes
6. Add Docker configuration

### Code Structure
```
service-name/
├── pom.xml
├── Dockerfile
└── src/main/java/com/hung/expensive/service/
    ├── ServiceApplication.java
    ├── controller/
    ├── service/
    ├── repository/
    └── config/
```

## 🐛 Troubleshooting

### Common Issues
1. **Service không start**: Check database connection
2. **Gateway không route**: Check Eureka registration
3. **JWT validation fail**: Check secret key configuration
4. **Database connection**: Check MySQL service và credentials

### Debug Commands
```bash
# Check service logs
docker-compose logs -f [service-name]

# Check service health
curl http://localhost:[port]/actuator/health

# Check Eureka registration
curl http://localhost:8761/eureka/apps
```

## 📚 References

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [Docker Documentation](https://docs.docker.com/)
- [Microservices Patterns](https://microservices.io/patterns/)

## 🤝 Contributing

1. Fork repository
2. Create feature branch
3. Implement changes
4. Add tests
5. Submit pull request

## 📄 License

This project is licensed under the MIT License.
