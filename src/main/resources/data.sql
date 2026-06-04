INSERT INTO users (username, email, password, created_at)
VALUES ('system', 'system@financeapi.local', '$2a$10$7Q4uJ7X3n.8IuQ0A8o9W2u5sJ5sD4w7qNq7mJ9kM9u7s8vGmNqj8W', NOW())
ON CONFLICT DO NOTHING;

INSERT INTO categories (name, type, user_id)
SELECT 'Food', 'EXPENSE', u.id FROM users u WHERE u.username = 'system' ON CONFLICT DO NOTHING;

INSERT INTO categories (name, type, user_id)
SELECT 'Salary', 'INCOME', u.id FROM users u WHERE u.username = 'system' ON CONFLICT DO NOTHING;

INSERT INTO categories (name, type, user_id)
SELECT 'Rent', 'EXPENSE', u.id FROM users u WHERE u.username = 'system' ON CONFLICT DO NOTHING;
