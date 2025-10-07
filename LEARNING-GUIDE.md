# 🎓 Microservices Learning Guide - Expensive Project

## 📚 Mục Tiêu Học Tập

Project này được thiết kế để giúp bạn hiểu và thực hành kiến trúc microservices với Spring Boot. Mỗi phần đều có giải thích chi tiết về **TẠI SAO** làm như vậy.

## 🏗️ Kiến Trúc Tổng Quan

### **Tại Sao Chia Thành Microservices?**

1. **Scalability**: Mỗi service có thể scale độc lập
2. **Maintainability**: Code được tách biệt, dễ maintain
3. **Technology Diversity**: Có thể dùng tech stack khác nhau
4. **Team Autonomy**: Mỗi team work trên service riêng
5. **Fault Isolation**: Lỗi ở 1 service không ảnh hưởng service khác

### **Sơ Đồ Kiến Trúc**

```
┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Mobile App    │
│   (React/Vue)   │    │   (React Native)│
└─────────┬───────┘    └─────────┬───────┘
          │                      │
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

## 🔍 Phân Tích Từng Service

### 1. **Service Discovery (Eureka Server)**

**Tại sao cần Service Discovery?**
- Trong microservices, services cần tìm thấy nhau
- IP addresses có thể thay đổi khi deploy
- Load balancing tự động

**Code Example:**
```java
@SpringBootApplication
@EnableEurekaServer  // Why: Enable Eureka Server
public class ServiceDiscoveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceDiscoveryApplication.class, args);
    }
}
```

**Key Learning Points:**
- `@EnableEurekaServer`: Biến application thành Eureka Server
- Port 8761: Default port cho Eureka
- Service registry: Lưu trữ thông tin các service

### 2. **Config Service**

**Tại sao cần Config Service?**
- Centralized configuration management
- Environment-specific configs (dev, test, prod)
- Dynamic configuration updates

**Code Example:**
```java
@SpringBootApplication
@EnableConfigServer  // Why: Enable Config Server
@EnableEurekaClient  // Why: Register với Eureka
public class ConfigServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServiceApplication.class, args);
    }
}
```

**Key Learning Points:**
- `@EnableConfigServer`: Enable configuration server
- Git backend: Lưu config trong Git repository
- Environment profiles: dev, test, prod

### 3. **API Gateway**

**Tại sao cần API Gateway?**
- Single entry point cho tất cả requests
- Authentication & authorization
- Load balancing
- Rate limiting
- CORS handling

**Code Example:**
```java
@SpringBootApplication
@EnableEurekaClient  // Why: Register với Eureka để discover services
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
```

**Gateway Configuration:**
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: auth-service
          uri: lb://auth-service  # Why: lb:// = load balancer
          predicates:
            - Path=/api/auth/**
          filters:
            - StripPrefix=2
```

**Key Learning Points:**
- `lb://`: Load balancer prefix
- Route predicates: Điều kiện để match route
- Filters: Xử lý request/response

### 4. **Auth Service**

**Tại sao tách riêng Auth Service?**
- Security isolation
- Independent scaling
- Centralized authentication logic

**Code Structure:**
```
auth-service/
├── controller/
│   └── AuthController.java     # Why: REST endpoints
├── service/
│   ├── AuthService.java        # Why: Business logic
│   └── JwtService.java         # Why: JWT token handling
├── repository/
│   └── UserRepository.java     # Why: Database operations
└── config/
    └── SecurityConfig.java     # Why: Security configuration
```

**Key Learning Points:**
- JWT token generation & validation
- Password hashing với BCrypt
- Stateless authentication

### 5. **User Service**

**Tại sao tách User Service khỏi Auth Service?**
- Separation of concerns
- User profile management vs authentication
- Different scaling requirements

**Code Example:**
```java
@Service
@Transactional
public class UserService {
    
    public UserProfileResponse getUserProfile(Long userId) {
        // Why: Business logic cho user profile
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return mapToUserProfileResponse(userOpt.get());
    }
}
```

**Key Learning Points:**
- DTO mapping pattern
- Exception handling
- Transaction management

### 6. **Expense Service**

**Tại sao Expense Service là core service?**
- Most complex business logic
- High traffic service
- Needs independent scaling

**Service-to-Service Communication:**
```java
@FeignClient(name = "user-service", url = "http://localhost:8082")
public interface UserServiceClient {
    
    @GetMapping("/api/users/{userId}")
    UserProfileResponse getUserProfile(@PathVariable("userId") Long userId);
}
```

**Key Learning Points:**
- Feign Client: Declarative REST client
- Service discovery integration
- Circuit breaker pattern

### 7. **Category Service**

**Tại sao Category Service đơn giản?**
- Read-heavy workload
- Less frequent updates
- Can be cached easily

**Code Example:**
```java
@Service
public class CategoryService {
    
    public List<CategoryResponse> getDefaultCategories() {
        // Why: Create default categories nếu chưa có
        createDefaultCategoriesIfNotExists();
        
        List<Category> categories = categoryRepository.findByIsDefaultTrue();
        return categories.stream()
                .map(this::mapToCategoryResponse)
                .collect(Collectors.toList());
    }
}
```

## 🛠️ Best Practices Được Áp Dụng

### 1. **Clean Architecture**

```
Controller Layer    → REST endpoints
Service Layer      → Business logic  
Repository Layer   → Data access
Entity Layer       → Database mapping
```

### 2. **DTO Pattern**

**Tại sao dùng DTO?**
- Separate API contract từ internal entities
- Version control cho API
- Security (hide sensitive data)

```java
// Why: Request DTO cho input validation
public class ExpenseRequest {
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;
    
    @NotBlank
    private String description;
}

// Why: Response DTO cho output format
public class ExpenseResponse {
    private Long id;
    private BigDecimal amount;
    private String description;
    // ... other fields
}
```

### 3. **Exception Handling**

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
    }
}
```

### 4. **Database Per Service**

**Tại sao mỗi service có database riêng?**
- Data isolation
- Independent scaling
- Technology diversity
- Team autonomy

### 5. **Configuration Management**

```yaml
# application.yml
spring:
  application:
    name: expense-service
  cloud:
    config:
      uri: http://localhost:8888
      name: expense-service
      profile: dev
```

## 🚀 Cách Chạy Project

### 1. **Prerequisites**
```bash
# Check Java version
java -version  # Should be 21+

# Check Maven
mvn -version

# Check Docker
docker --version
docker-compose --version
```

### 2. **Quick Start**
```bash
# Clone project
git clone <repository-url>
cd expensive-project-backend

# Build all services
./scripts/build.sh

# Start với Docker Compose
./scripts/deploy.sh

# Check health
./scripts/health-check.sh
```

### 3. **Manual Development**
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

## 🧪 Testing APIs

### 1. **Register User**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

### 2. **Login**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "testuser",
    "password": "password123"
  }'
```

### 3. **Get User Profile**
```bash
curl -X GET http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer <your-jwt-token>"
```

### 4. **Create Expense**
```bash
curl -X POST http://localhost:8080/api/expenses \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -d '{
    "amount": 100.50,
    "description": "Lunch at restaurant",
    "expenseDate": "2024-01-15",
    "userId": 1,
    "categoryId": 1
  }'
```

## 📊 Monitoring & Debugging

### 1. **Service Discovery Dashboard**
- URL: http://localhost:8761
- Xem tất cả registered services

### 2. **Health Checks**
```bash
# Check all services
curl http://localhost:8080/actuator/health
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
curl http://localhost:8084/actuator/health
```

### 3. **Gateway Routes**
```bash
curl http://localhost:8080/actuator/gateway/routes
```

## 🔧 Common Issues & Solutions

### 1. **Service không start**
```bash
# Check logs
docker-compose logs -f [service-name]

# Check database connection
docker-compose logs mysql
```

### 2. **Gateway không route**
```bash
# Check Eureka registration
curl http://localhost:8761/eureka/apps

# Check service health
curl http://localhost:8081/actuator/health
```

### 3. **JWT validation fail**
```bash
# Check JWT secret configuration
# Check Auth Service logs
docker-compose logs auth-service
```

## 🎯 Learning Exercises

### 1. **Beginner Level**
- [ ] Hiểu cách các service communicate với nhau
- [ ] Test tất cả API endpoints
- [ ] Thêm validation cho DTOs
- [ ] Implement error handling

### 2. **Intermediate Level**
- [ ] Thêm caching với Redis
- [ ] Implement circuit breaker
- [ ] Add monitoring với Prometheus
- [ ] Implement distributed tracing

### 3. **Advanced Level**
- [ ] Add event-driven architecture
- [ ] Implement CQRS pattern
- [ ] Add message queues (Kafka/RabbitMQ)
- [ ] Implement saga pattern

## 📚 Resources

### **Spring Cloud Documentation**
- [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)
- [Spring Cloud Config](https://spring.io/projects/spring-cloud-config)
- [Netflix Eureka](https://spring.io/projects/spring-cloud-netflix)

### **Microservices Patterns**
- [Microservices.io](https://microservices.io/)
- [Martin Fowler's Blog](https://martinfowler.com/articles/microservices.html)

### **Docker & Kubernetes**
- [Docker Documentation](https://docs.docker.com/)
- [Kubernetes Documentation](https://kubernetes.io/docs/)

## 🤝 Contributing

1. Fork repository
2. Create feature branch
3. Implement changes với proper comments
4. Add tests
5. Submit pull request

## 📄 License

This project is licensed under the MIT License.

---

**Happy Learning! 🎉**

Nếu có thắc mắc, hãy đọc comments trong code - mỗi phần đều có giải thích chi tiết về **TẠI SAO** làm như vậy.
