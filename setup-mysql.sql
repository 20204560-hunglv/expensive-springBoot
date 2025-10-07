-- Why: MySQL setup script cho microservices architecture
-- Design decision: Separate databases cho từng service (database per service pattern)
-- Business requirement: Cần isolated databases để đảm bảo data independence

-- Create databases cho từng service
CREATE DATABASE IF NOT EXISTS expensive_auth;
CREATE DATABASE IF NOT EXISTS expensive_user;
CREATE DATABASE IF NOT EXISTS expensive_expense;
CREATE DATABASE IF NOT EXISTS expensive_category;

-- Why: Create user cho application access
CREATE USER IF NOT EXISTS 'expensive_user'@'%' IDENTIFIED BY 'expensive_password';
GRANT ALL PRIVILEGES ON expensive_auth.* TO 'expensive_user'@'%';
GRANT ALL PRIVILEGES ON expensive_user.* TO 'expensive_user'@'%';
GRANT ALL PRIVILEGES ON expensive_expense.* TO 'expensive_user'@'%';
GRANT ALL PRIVILEGES ON expensive_category.* TO 'expensive_user'@'%';

-- Why: Flush privileges để apply changes
FLUSH PRIVILEGES;

-- Why: Use auth database và create tables
USE expensive_auth;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    phone_number VARCHAR(20),
    address VARCHAR(100),
    city VARCHAR(50),
    country VARCHAR(50),
    postal_code VARCHAR(10),
    is_enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Why: Use user database và create tables
USE expensive_user;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    phone_number VARCHAR(20),
    address VARCHAR(100),
    city VARCHAR(50),
    country VARCHAR(50),
    postal_code VARCHAR(10),
    is_enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Why: Use expense database và create tables
USE expensive_expense;

CREATE TABLE IF NOT EXISTS expenses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(10,2) NOT NULL,
    description VARCHAR(255) NOT NULL,
    notes TEXT,
    expense_date DATE NOT NULL,
    location VARCHAR(100),
    receipt_url VARCHAR(255),
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Why: Use category database và create tables
USE expensive_category;

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    color VARCHAR(7),
    icon VARCHAR(50),
    is_default BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Why: Insert default categories
INSERT IGNORE INTO categories (name, description, color, icon, is_default) VALUES
('Ăn uống', 'Chi phí ăn uống hàng ngày', '#FF6B6B', 'restaurant', TRUE),
('Đi lại', 'Chi phí đi lại, xăng xe', '#4ECDC4', 'car', TRUE),
('Học tập', 'Chi phí học tập, sách vở', '#45B7D1', 'school', TRUE),
('Giải trí', 'Chi phí giải trí, xem phim', '#96CEB4', 'movie', TRUE),
('Sức khỏe', 'Chi phí y tế, thuốc men', '#FFEAA7', 'medical', TRUE),
('Mua sắm', 'Chi phí mua sắm, shopping', '#DDA0DD', 'shopping', TRUE),
('Hóa đơn', 'Tiền điện, nước, internet', '#98D8C8', 'bill', TRUE),
('Khác', 'Các chi phí khác', '#F7DC6F', 'other', TRUE);