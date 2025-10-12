-- Why: Setup MySQL database cho auth-service
-- Design decision: Tách riêng database cho từng service để isolation

-- Tạo database cho auth service
CREATE DATABASE IF NOT EXISTS expensive_auth CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Tạo user riêng cho auth service (security best practice)
CREATE USER IF NOT EXISTS 'expensive_user'@'localhost' IDENTIFIED BY 'expensive_password';
CREATE USER IF NOT EXISTS 'expensive_user'@'%' IDENTIFIED BY 'expensive_password';

-- Grant permissions cho user
GRANT ALL PRIVILEGES ON expensive_auth.* TO 'expensive_user'@'localhost';
GRANT ALL PRIVILEGES ON expensive_auth.* TO 'expensive_user'@'%';

-- Flush privileges để apply changes
FLUSH PRIVILEGES;

-- Show created database
SHOW DATABASES LIKE 'expensive_auth';