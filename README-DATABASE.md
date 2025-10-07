# Hướng Dẫn Setup Database cho Spring Boot Application

## Tổng Quan Kiến Trúc Database

Project sử dụng **MySQL** làm production database và **H2** cho development/testing.

### Cấu Trúc Database

```
expensive_db/
├── users            # Bảng người dùng
├── categories       # Danh mục chi tiêu  
├── expenses         # Chi tiêu chính
└── budgets         # Ngân sách theo tháng
```

## Bước 1: Cài Đặt MySQL

### macOS (Homebrew)
```bash
# Install MySQL
brew install mysql

# Start MySQL service
brew services start mysql

# Secure installation
mysql_secure_installation
```

### Ubuntu/Debian
```bash
# Update package list
sudo apt update

# Install MySQL
sudo apt install mysql-server

# Start MySQL service
sudo systemctl start mysql
sudo systemctl enable mysql

# Secure installation
sudo mysql_secure_installation
```

### Windows
1. Download MySQL từ [official website](https://dev.mysql.com/downloads/mysql/)
2. Chạy installer và follow setup wizard
3. Chọn "Developer Default" configuration
4. Set root password

## Bước 2: Tạo Database và User

```bash
# Connect to MySQL as root
mysql -u root -p

# Run setup script
source /path/to/your/project/backend/setup-mysql.sql
```

Hoặc copy-paste nội dung file `setup-mysql.sql` vào MySQL console.

## Bước 3: Cập Nhật Configuration

### Update Password trong application-prod.properties

```properties
# Thay đổi password trong file này:
# backend/src/main/resources/application-prod.properties

spring.datasource.password=your_actual_password_here
```

## Bước 4: Chạy Application

### Development Mode (H2 Database)
```bash
cd backend
./mvnw spring-boot:run
```

Access H2 Console: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:expensivedb`
- Username: `sa`
- Password: `password`

### Production Mode (MySQL Database)
```bash
cd backend
./mvnw spring-boot:run -Dspring.profiles.active=prod
```

## Kiểm Tra Database Schema

Sau khi chạy application lần đầu, tables sẽ được tạo tự động. Verify bằng cách:

```sql
USE expensive_db;
SHOW TABLES;

-- Check table structure
DESCRIBE users;
DESCRIBE categories;
DESCRIBE expenses;
DESCRIBE budgets;
```

## Các Profile Environment

| Profile | Database | Use Case |
|---------|----------|----------|
| `dev` (default) | H2 Memory | Development |
| `prod` | MySQL | Production |
| `test` | H2 Memory | Unit Testing |

### Chuyển đổi Profile:

```bash
# Development
./mvnw spring-boot:run

# Production  
./mvnw spring-boot:run -Dspring.profiles.active=prod

# Testing
./mvnw test
```

## Database Schema Chi Tiết

### Users Table
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Categories Table
```sql
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    color_code VARCHAR(7), -- #RRGGBB format
    icon_name VARCHAR(50),
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE KEY unique_user_category (user_id, name)
);
```

### Expenses Table
```sql
CREATE TABLE expenses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(10,2) NOT NULL,
    description VARCHAR(255) NOT NULL,
    notes TEXT,
    expense_date DATE NOT NULL,
    location VARCHAR(255),
    receipt_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);
```

### Budgets Table
```sql
CREATE TABLE budgets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(12,2) NOT NULL,
    budget_month INT NOT NULL, -- 1-12
    budget_year INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    category_id BIGINT, -- NULL for total budget
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id),
    UNIQUE KEY unique_user_category_budget (user_id, category_id, budget_year, budget_month)
);
```

## Performance Optimization

### Indexes Được Tạo Tự Động:
- Primary keys (id columns)
- Foreign keys  
- Unique constraints

### Recommended Manual Indexes:
```sql
-- For expense queries by date range
CREATE INDEX idx_expenses_user_date ON expenses(user_id, expense_date);

-- For expense search by description
CREATE FULLTEXT INDEX idx_expenses_description ON expenses(description);

-- For budget queries
CREATE INDEX idx_budgets_user_month_year ON budgets(user_id, budget_year, budget_month);
```

## Troubleshooting

### Connection Issues
1. **"Access denied for user"**: Check username/password trong application-prod.properties
2. **"Unknown database"**: Run setup-mysql.sql script để tạo database
3. **Connection timeout**: Check MySQL service status

### Schema Issues
1. **Table doesn't exist**: Set `spring.jpa.hibernate.ddl-auto=create` cho lần đầu chạy
2. **Column not found**: Clear target/ folder và rebuild project

### Performance Issues  
1. Enable SQL logging: `spring.jpa.show-sql=true`
2. Check slow query log trong MySQL
3. Add appropriate indexes

## Security Best Practices

1. **Database User Privileges**: Application user chỉ có SELECT, INSERT, UPDATE, DELETE
2. **Password**: Sử dụng strong password và store trong environment variables
3. **Connection**: Use SSL trong production environment
4. **Backup**: Setup regular database backups

## Backup và Recovery

### Manual Backup
```bash
mysqldump -u expensive_user -p expensive_db > backup.sql
```

### Restore từ Backup
```bash
mysql -u expensive_user -p expensive_db < backup.sql
```

## Migration Strategy

Khi có schema changes:
1. Backup current database
2. Set `spring.jpa.hibernate.ddl-auto=update`
3. Test changes trong development
4. Apply to production với downtime window

## Monitoring Database

### Query Performance
```sql
-- Show slow queries
SHOW VARIABLES LIKE 'slow_query_log';
SHOW VARIABLES LIKE 'long_query_time';

-- Show process list
SHOW PROCESSLIST;
```

### Storage Usage
```sql
-- Database size
SELECT 
    table_schema AS 'Database',
    ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS 'Size (MB)'
FROM information_schema.tables 
WHERE table_schema = 'expensive_db'
GROUP BY table_schema;
```
