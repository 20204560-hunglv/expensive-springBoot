-- MySQL Database Setup Script for Expensive Tracker
-- Why: Setup complete database structure với proper indexes và constraints

-- Create database
DROP DATABASE IF EXISTS expensive_db;
CREATE DATABASE expensive_db 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

-- Use the database
USE expensive_db;

-- Create user for application
-- Why: Separate user với limited privileges cho security
DROP USER IF EXISTS 'expensive_user'@'localhost';
CREATE USER 'expensive_user'@'localhost' IDENTIFIED BY 'expense_password_2024';

-- Grant necessary privileges
GRANT SELECT, INSERT, UPDATE, DELETE ON expensive_db.* TO 'expensive_user'@'localhost';
FLUSH PRIVILEGES;

-- Note: Tables sẽ được tạo tự động bởi Hibernate khi chạy application
-- Tuy nhiên, bạn có thể tạo indexes manually để optimize performance:

-- Create indexes after tables are created (run these after first startup)
/*
-- Indexes cho performance optimization

-- Users table indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);

-- Categories table indexes  
CREATE INDEX idx_categories_user_id ON categories(user_id);
CREATE INDEX idx_categories_name ON categories(name);
CREATE INDEX idx_categories_is_default ON categories(is_default);

-- Expenses table indexes - Why: Optimize common queries
CREATE INDEX idx_expenses_user_id ON expenses(user_id);
CREATE INDEX idx_expenses_category_id ON expenses(category_id);
CREATE INDEX idx_expenses_expense_date ON expenses(expense_date);
CREATE INDEX idx_expenses_user_date ON expenses(user_id, expense_date);
CREATE INDEX idx_expenses_user_category ON expenses(user_id, category_id);
CREATE INDEX idx_expenses_created_at ON expenses(created_at);

-- Composite index for pagination queries
CREATE INDEX idx_expenses_user_date_created ON expenses(user_id, expense_date DESC, created_at DESC);

-- Budgets table indexes
CREATE INDEX idx_budgets_user_id ON budgets(user_id);
CREATE INDEX idx_budgets_category_id ON budgets(category_id);
CREATE INDEX idx_budgets_month_year ON budgets(budget_year, budget_month);
CREATE INDEX idx_budgets_user_month_year ON budgets(user_id, budget_year, budget_month);

-- Full-text search index cho expense descriptions
CREATE FULLTEXT INDEX idx_expenses_description_fulltext ON expenses(description);
*/

-- Show database info
SELECT 'Database expensive_db created successfully!' as Status;
SELECT 'User expensive_user created with necessary privileges' as User_Status;
SELECT 'Remember to update application-prod.properties with correct password' as Note;
