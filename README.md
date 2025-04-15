# Customer Rewards Program (Spring Boot)

A simple Spring Boot application to calculate customer reward points based on recent transactions (purchase).

---

## 🔗 API Details

Method	Endpoint	Description
- GET     /actuator **actuator endpoint**
- GET	  /api/rewards	**Get all customer rewards**
- GET	  /api/customer/reward/{customerId}	**Get rewards for a specific customer**
- GET	  /api/customer/reward/{customerId}?startDate=yyyy-MM-dd&endDate=yyyy-MM-dd	**Get rewards for a specific customer via filtering date range**
- POST    /api/store/customer/data **store customer data**

---

## 🧩 Features

- Calculate reward points based on purchase amount
- View rewards for all customers
- Filter rewards by customer and date range
- RESTful API
- Uses mock data
- SonarQube integration ready

---

## 🚀 Technologies Used

- Java 21
- Spring Boot
- Spring RESTful API
- Spring Security
- Spring AOP
- Maven
- JUnit & Mockito
- Lombok
- SonarQube (Optional)
- MYSQL (database)

---

## 📦 How to Run

### Run via IDE
1. Clone the project
2. Open in IntelliJ / Spring Tool Suite 4
3. Run `CustomerRewardsApplication.java`

### Run via Terminal

mvn spring-boot:run

---

✅ Stubbed data

### 📦 Sample Data for Testing (SQL Inserts)

- Customer C001 - Transactions in Jan, Feb, Mar (diverse amounts)
```sql
INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES 
(1, 'C001', 120.0, '2025-01-15'),
(2, 'C001', 200.0, '2025-02-10'),
(3, 'C001', 130.0, '2025-03-05');

- Customer C002 - One near $50, one near $100, one over $100

INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES 
(4, 'C002', 45.0, '2025-01-10'),
(5, 'C002', 75.0, '2025-02-18'),
(6, 'C002', 110.0, '2025-03-20');

- Customer C003 - All transactions under or at $50 (no rewards)

INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES 
(7, 'C003', 30.0, '2025-01-25'),
(8, 'C003', 50.0, '2025-02-12'),
(9, 'C003', 45.0, '2025-03-03');

- Customer C004 - One big transaction in Feb only

INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES 
(10, 'C004', 250.0, '2025-02-14');

- Out-of-range transactions for C001 (should be excluded in Jan–Mar queries)

INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES 
(11, 'C001', 180.0, '2024-12-20'),
(12, 'C001', 160.0, '2025-04-01');

---

### Sample Request

```bash
GET /api/rewards

**Sample Response**

{
    "status": "SUCCESS",
    "message": "Rewards fetched successfully",
    "data": [
        {
            "customerId": "C001",
            "monthlyRewards": [
                {
                    "month": "JANUARY 2025",
                    "points": 90
                },
                {
                    "month": "FEBRUARY 2025",
                    "points": 250
                },
                {
                    "month": "MARCH 2025",
                    "points": 110
                },
                {
                    "month": "APRIL 2025",
                    "points": 170
                }
            ],
            "totalPoints": 620
        },
        {
            "customerId": "C002",
            "monthlyRewards": [
                {
                    "month": "FEBRUARY 2025",
                    "points": 25
                },
                {
                    "month": "MARCH 2025",
                    "points": 70
                }
            ],
            "totalPoints": 95
        },
        {
            "customerId": "C003",
            "monthlyRewards": [
                {
                    "month": "FEBRUARY 2025",
                    "points": 0
                }
            ],
            "totalPoints": 0
        },
        {
            "customerId": "C004",
            "monthlyRewards": [
                {
                    "month": "FEBRUARY 2025",
                    "points": 350
                }
            ],
            "totalPoints": 350
        }
    ]
}

---

**Sample Request**

GET /api/customer/reward/C002?startDate=2025-01-01&endDate=2025-03-31

**Sample Response**

{
    "status": "SUCCESS",
    "message": "Customer details fetched successfully",
    "data": {
        "customerId": "C002",
        "startDate": "2025-01-01",
        "endDate": "2025-03-31",
        "monthlyRewards": [
            {
                "month": "JANUARY 2025",
                "points": 0
            },
            {
                "month": "FEBRUARY 2025",
                "points": 25
            },
            {
                "month": "MARCH 2025",
                "points": 70
            }
        ],
        "totalPoints": 95,
        "transactions": [
            {
                "id": 4,
                "customerId": "C002",
                "amount": 45.0,
                "transactionDate": "2025-01-10"
            },
            {
                "id": 5,
                "customerId": "C002",
                "amount": 75.0,
                "transactionDate": "2025-02-18"
            },
            {
                "id": 6,
                "customerId": "C002",
                "amount": 110.0,
                "transactionDate": "2025-03-20"
            }
        ]
    }
}

### 🧪 API Testing with `curl`

#### 🔍 Health Check - Actuator `/health`

curl --location 'http://localhost:8081/actuator/health' \
--header 'Authorization: Basic YWRtaW46YWRtaW4xMjM=' \
--header 'Cookie: JSESSIONID=702C876A6BB7A1CCE4B6C265191E20CC'

ℹ️ Info Check - Actuator /info

curl --location 'http://localhost:8081/actuator/info' \
--header 'Authorization: Basic YWRtaW46YWRtaW4xMjM=' \
--header 'Cookie: JSESSIONID=702C876A6BB7A1CCE4B6C265191E20CC'

🧾 All Transactions for Last 3 Months (All Customers)

curl --location 'http://localhost:8081/api/rewards' \
--header 'Authorization: Basic YWRtaW46YWRtaW4xMjM=' \
--header 'Cookie: JSESSIONID=702C876A6BB7A1CCE4B6C265191E20CC'

📅 Transactions in Jan, Feb, Mar 2025 (Customer C001)

curl --location 'http://localhost:8081/api/customer/reward/C001?startDate=2025-01-01&endDate=2025-03-31' \
--header 'Authorization: Basic YWRtaW46YWRtaW4xMjM=' \
--header 'Cookie: JSESSIONID=702C876A6BB7A1CCE4B6C265191E20CC'

📅 Transactions in Jan, Feb, Mar, Apr 2025 (Customer C001 - no explicit date, uses default logic)

curl --location 'http://localhost:8081/api/customer/reward/C001' \
--header 'Authorization: Basic YWRtaW46YWRtaW4xMjM=' \
--header 'Cookie: JSESSIONID=702C876A6BB7A1CCE4B6C265191E20CC'

📅 Transactions in Jan, Feb 2025 (Customer C001)

curl --location 'http://localhost:8081/api/customer/reward/C001?startDate=2025-01-01&endDate=2025-02-28' \
--header 'Authorization: Basic YWRtaW46YWRtaW4xMjM=' \
--header 'Cookie: JSESSIONID=702C876A6BB7A1CCE4B6C265191E20CC'

📅 Transactions in Dec 2024 to Apr 2025 (Customer C001 - full range)

curl --location 'http://localhost:8081/api/customer/reward/C001?startDate=2024-12-01&endDate=2025-04-15' \
--header 'Authorization: Basic YWRtaW46YWRtaW4xMjM=' \
--header 'Cookie: JSESSIONID=702C876A6BB7A1CCE4B6C265191E20CC'

❌ Customer Not Found (C005)

curl --location 'http://localhost:8081/api/customer/reward/C005?startDate=2024-01-01&endDate=2025-04-15' \
--header 'Authorization: Basic YWRtaW46YWRtaW4xMjM=' \
--header 'Cookie: JSESSIONID=702C876A6BB7A1CCE4B6C265191E20CC'

---

🛠 Author

Created by : Jitendra Sharma
