-- V1__initial_schema.sql
-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create categories table
CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_category_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create transactions table
CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    amount NUMERIC(15, 2) NOT NULL,
    description VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_transaction_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_transaction_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
);

-- Create budgets table
CREATE TABLE IF NOT EXISTS budgets (
    id BIGSERIAL PRIMARY KEY,
    monthly_limit NUMERIC(15, 2) NOT NULL,
    month INT NOT NULL,
    year INT NOT NULL,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_budget_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_budget_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
    CONSTRAINT uq_budget_user_category_period UNIQUE (user_id, category_id, month, year)
);

-- Seed default system user and base categories
INSERT INTO users (username, email, password, created_at)
VALUES ('system', 'system@financeapi.local', '$2a$10$7Q4uJ7X3n.8IuQ0A8o9W2u5sJ5sD4w7qNq7mJ9kM9u7s8vGmNqj8W', NOW())
ON CONFLICT (username) DO NOTHING;

INSERT INTO categories (name, type, user_id)
SELECT 'Food', 'EXPENSE', u.id FROM users u WHERE u.username = 'system'
AND NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Food' AND user_id = u.id);

INSERT INTO categories (name, type, user_id)
SELECT 'Salary', 'INCOME', u.id FROM users u WHERE u.username = 'system'
AND NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Salary' AND user_id = u.id);

INSERT INTO categories (name, type, user_id)
SELECT 'Rent', 'EXPENSE', u.id FROM users u WHERE u.username = 'system'
AND NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Rent' AND user_id = u.id);
