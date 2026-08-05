-- V2__seed_default_categories.sql
-- Automatically seed essential categories for new users upon registration or system initialization

-- Insert default categories for existing users if missing
INSERT INTO categories (name, type, user_id)
SELECT cat.name, cat.type, u.id
FROM users u
CROSS JOIN (
    VALUES 
        ('Salary', 'INCOME'),
        ('Freelance / Business', 'INCOME'),
        ('Investments', 'INCOME'),
        ('Groceries & Food', 'EXPENSE'),
        ('Rent & Housing', 'EXPENSE'),
        ('Utilities & Bills', 'EXPENSE'),
        ('Transportation & Fuel', 'EXPENSE'),
        ('Dining Out & Entertainment', 'EXPENSE'),
        ('Shopping & Lifestyle', 'EXPENSE'),
        ('Healthcare & Medical', 'EXPENSE'),
        ('Education & Learning', 'EXPENSE')
) AS cat(name, type)
WHERE NOT EXISTS (
    SELECT 1 FROM categories c 
    WHERE c.name = cat.name AND c.user_id = u.id
);
