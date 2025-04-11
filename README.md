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

- INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (1, 'C001', 120.0, '2024-12-15');
- INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (2, 'C002', 120.0, '2025-01-20');
- INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (3, 'C003', 120.0, '2025-01-17');
- INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (4, 'C004', 120.0, '2025-02-25');
  
- INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (5, 'C002', 200.0, '2025-02-05');
- INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (6, 'C002', 130.0, '2025-03-15');
  
- INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (7, 'C001', 45.0, '2025-03-30');
- INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (8, 'C003', 190.5, '2024-04-10');

- INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (9, 'C004', 290.5, '2024-04-10');

---

**Sample Request**

GET api/rewards

**Sample Response**

{
    "status": "SUCCESS",
    "message": "Rewards fetched successfully",
    "data": [
        {
            "customerId": "C002",
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
                }
            ],
            "totalPoints": 450
        },
        {
            "customerId": "C003",
            "monthlyRewards": [
                {
                    "month": "JANUARY 2025",
                    "points": 90
                }
            ],
            "totalPoints": 90
        },
        {
            "customerId": "C004",
            "monthlyRewards": [
                {
                    "month": "FEBRUARY 2025",
                    "points": 90
                }
            ],
            "totalPoints": 90
        }
    ]
}

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
                "points": 90
            },
            {
                "month": "FEBRUARY 2025",
                "points": 250
            },
            {
                "month": "MARCH 2025",
                "points": 110
            }
        ],
        "totalPoints": 450,
        "transactions": [
            {
                "id": 2,
                "customerId": "C002",
                "amount": 120.0,
                "transactionDate": "2025-01-20"
            },
            {
                "id": 5,
                "customerId": "C002",
                "amount": 200.0,
                "transactionDate": "2025-02-05"
            },
            {
                "id": 6,
                "customerId": "C002",
                "amount": 130.0,
                "transactionDate": "2025-03-15"
            }
        ]
    }
}

---

🛠 Author

Created by : Jitendra Sharma
