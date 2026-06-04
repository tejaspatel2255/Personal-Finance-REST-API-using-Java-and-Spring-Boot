# ApexFinance - Personal Finance REST API & Dashboard

A professional, secure, and high-performance Personal Finance Tracker built using **Java 17**, **Spring Boot 3.x**, and **PostgreSQL**. Features robust stateless **JWT Authentication**, custom **SQL-optimized database queries**, real-time **budget synchronization**, and a premium **single-page visual dashboard**.

---

## 🌟 Key Features

*   **Secure JWT Authentication**: Sign up and login endpoints generating signed JSON Web Tokens for secure session management.
*   **Transaction Management**: Create, view, update, and delete income/expense transactions.
*   **Interactive Category Management**: Create and organize custom income and expense categories.
*   **Real-time Budget Synchronization**: Define monthly limits for expense categories. Budgets dynamically compute utilization percentages and flag status warnings (`Close to Limit`, `Exceeded`) instantly as transactions are added, edited, or deleted.
*   **Database-Level Aggregation**: Implements native SQL queries for lightning-fast monthly totals calculation, skipping heavy Java heap processing.
*   **Embedded Web Dashboard**: A built-in, responsive single-page web app styled with rich aesthetics (dark mode, glassmorphism, responsive charts/progress-bars).

---

## 🛠️ Tech Stack & Libraries

*   **Backend**: Java 17, Spring Boot 3.x (Spring Web, Spring Security, Spring Data JPA)
*   **Database**: PostgreSQL
*   **Documentation**: Springdoc OpenAPI (Swagger UI)
*   **Security**: JSON Web Tokens (JJWT), BCrypt Password Hashing
*   **Frontend**: HTML5, Vanilla CSS3 (Glassmorphism design system), Vanilla JavaScript (AJAX Fetch API)
*   **Build Tool**: Maven

---

## 🚀 Getting Started (From Scratch)

Follow these steps to set up and run the project on any computer:

### 1. Prerequisites
Ensure you have the following installed on your machine:
*   **Java Development Kit (JDK) 17**: Required to compile and run Java code. (We recommend [Eclipse Temurin JDK 17](https://adoptium.net/)).
*   **PostgreSQL Database**: Required to store transactions, users, and budgets.
*   **Git**: Required to clone the project.

### 2. Clone the Repository
Open your terminal (PowerShell, Command Prompt, or Bash) and run:
```bash
git clone <your-repository-url>
cd personal-finance-rest-api
```

### 3. Create the Database
1. Open **pgAdmin 4** (or connect via your PostgreSQL CLI).
2. Right-click **Databases** and select **Create ➔ Database...**.
3. Set the name to **`personal_finance`** and click **Save**.
*(Alternatively, run `CREATE DATABASE personal_finance;` in your PostgreSQL Query Tool).*

### 4. Database Password Configuration
By default, the project expects your PostgreSQL database password to be `123456` with username `postgres`. 
*   If your password is different, open the file `src/main/resources/application.properties` and change `123456` to your PostgreSQL password:
    ```properties
    spring.datasource.password=${DB_PASSWORD:your_actual_password_here}
    ```

### 5. Run the Application
From your project's root folder, run:
```bash
mvn spring-boot:run
```
*(If Maven is not installed globally on your machine, you can run `.maven\apache-maven-3.9.6\bin\mvn.cmd spring-boot:run` on Windows or `./mvnw spring-boot:run` on macOS/Linux).*

Once the console logs show `Started PersonalFinanceRestApiApplication`, open your web browser and go to:
*   **Interactive Web Dashboard UI**: [http://localhost:8080/](http://localhost:8080/)
*   **Swagger API Documentation**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🖥️ How to Use the Dashboard (Step-by-Step Guide)

You do not need to use Swagger or Postman to interact with this API. Follow this walkthrough to test the application using the built-in UI:

1. **Open the Webpage**: Go to [http://localhost:8080/](http://localhost:8080/) in your browser. You will be greeted by a dark-mode Login/Registration panel.
2. **Register a Account**:
    *   Click on the **Register** tab.
    *   Enter a username (e.g. `tejas`), email, and a secure password. Click **Create Account**.
3. **Sign In**:
    *   Switch back to the **Sign In** tab.
    *   Enter the username and password you just registered, and click **Login**.
    *   *Note: Your session token is stored locally, so reloading the webpage will not log you out!*
4. **Create Categories**:
    *   In the **Setup & Budgeting** panel on the bottom-left, enter a category name (e.g. `Food`) and select the type as `Expense`. Click **Create Category**.
    *   Add an income category as well (e.g. `Salary` with type `Income`).
5. **Set a Spending Limit (Budget)**:
    *   Under the **Limit Category** dropdown, choose your newly created `Food` category.
    *   Enter a Monthly Limit (e.g. `500`) and click **Set Limit**.
    *   *You will immediately see your active budget progress bar appear in the right panel with 0% utilization.*
6. **Log a Transaction**:
    *   In the **Add Transaction** panel on the top-left, enter an amount (e.g. `120.00`), a description (e.g. `Weekly Groceries`), and select the `Food` category.
    *   Click **Record Transaction**.
7. **Watch the Magic Happen**:
    *   The **Monthly Expense** card will update to show `$120.00`.
    *   The **Current Balance** card will recalculate.
    *   Your **Budget Allocation** progress bar will dynamically fill up to show `24.0% Used` and show the remaining balance.
    *   *If you add another transaction that exceeds your limit, the progress bar will turn red to warn you!*
8. **Delete Transactions**:
    *   If you make a mistake, click the `×` button next to any transaction in the **Monthly Transactions Log**. The stats and budget limits will instantly update.

---

## 📊 API Architecture & Endpoints

All endpoints (except `/api/auth/**`) require the header:
`Authorization: Bearer <your_jwt_token>`

### 🔑 Authentication (`/api/auth`)
*   `POST /api/auth/register` - Create a new user account.
*   `POST /api/auth/login` - Authenticate and retrieve a JWT access token.

### 📁 Category Management (`/api/categories`)
*   `POST /api/categories` - Create a custom category (`INCOME` or `EXPENSE`).
*   `GET /api/categories` - Retrieve user-owned categories.
*   `DELETE /api/categories/{id}` - Delete a category.

### 💸 Transaction Logging (`/api/transactions`)
*   `POST /api/transactions` - Log a new transaction (automatically updates budget progress).
*   `GET /api/transactions` - Filter and retrieve monthly transactions list.
*   `PUT /api/transactions/{id}` - Modify transaction details (recalculates affected budgets).
*   `DELETE /api/transactions/{id}` - Delete a transaction (adjusts budgets).

### 🎯 Monthly Budgeting (`/api/budgets`)
*   `POST /api/budgets` - Set/Upsert a monthly spending limit for a specific category.
*   `GET /api/budgets` - Retrieve budgets for the current month with spent amounts, remaining balance, and utilization percentage.

### 📈 Summaries (`/api/summary`)
*   `GET /api/summary?month=X&year=Y` - Fetch monthly summary stats (total income, total expense, current balance).
