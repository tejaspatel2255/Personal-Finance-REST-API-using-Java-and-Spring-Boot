# Personal Finance REST API

Java 17, Spring Boot 3.x, PostgreSQL, JWT auth, and Swagger UI.

## Setup

1. Create a PostgreSQL database named `personal_finance`.
2. Configure environment variables:
   - `DB_URL` like `jdbc:postgresql://localhost:5432/personal_finance`
   - `DB_USERNAME`
   - `DB_PASSWORD`
   - `JWT_SECRET` at least 32 characters long
   - `JWT_EXPIRATION_MS` optional, default `86400000`
3. Run the app:

```bash
mvn spring-boot:run
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

## API Authentication

All endpoints except `/api/auth/**` require:

```http
Authorization: Bearer <token>
```

## Sample Requests

### Register

```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "john",
  "email": "john@example.com",
  "password": "secret123"
}
```

### Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "john",
  "password": "secret123"
}
```

### Create Category

```http
POST /api/categories
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Groceries",
  "type": "EXPENSE"
}
```

### Get Categories

```http
GET /api/categories
Authorization: Bearer <token>
```

### Create Transaction

```http
POST /api/transactions
Authorization: Bearer <token>
Content-Type: application/json

{
  "amount": 120.50,
  "description": "Weekly groceries",
  "date": "2026-06-04",
  "categoryId": 1
}
```

### List Transactions

```http
GET /api/transactions?startDate=2026-06-01&endDate=2026-06-30
Authorization: Bearer <token>
```

### Update Transaction

```http
PUT /api/transactions/1
Authorization: Bearer <token>
Content-Type: application/json

{
  "amount": 130.00,
  "description": "Updated groceries",
  "date": "2026-06-04",
  "categoryId": 1
}
```

### Delete Transaction

```http
DELETE /api/transactions/1
Authorization: Bearer <token>
```

### Set Budget

```http
POST /api/budgets
Authorization: Bearer <token>
Content-Type: application/json

{
  "monthlyLimit": 1000,
  "categoryId": 1,
  "month": 6,
  "year": 2026
}
```

### Get Budgets

```http
GET /api/budgets
Authorization: Bearer <token>
```

### Summary

```http
GET /api/summary?month=6&year=2026
Authorization: Bearer <token>
```

## Notes

- Passwords are hashed with BCrypt.
- All data access is filtered by the authenticated user's id.
- Seed categories are intended for a `system` user if you add one for shared defaults; otherwise categories are user-owned and created after signup.
