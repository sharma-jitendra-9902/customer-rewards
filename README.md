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

- INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (1, 'C001', 120.0, '2024-12-10');
- INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (2, 'C001', 120.0, '2025-01-10');
- INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (3, 'C001', 75.0, '2025-01-20');
- INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (4, 'C001', 99.0, '2025-02-25');
  
- INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (5, 'C002', 200.0, '2025-02-05');
- INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (6, 'C002', 130.0, '2025-03-15');
  
- INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (7, 'C003', 45.0, '2025-03-30');
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
            "customerId": "C001",
            "monthlyRewards": [
                {
                    "month": "JANUARY 2025",
                    "points": 115
                },
                {
                    "month": "FEBRUARY 2025",
                    "points": 49
                }
            ],
            "totalPoints": 164
        },
        {
            "customerId": "C002",
            "monthlyRewards": [
                {
                    "month": "FEBRUARY 2025",
                    "points": 250
                },
                {
                    "month": "MARCH 2025",
                    "points": 110
                }
            ],
            "totalPoints": 360
        },
        {
            "customerId": "C003",
            "monthlyRewards": [
                {
                    "month": "MARCH 2025",
                    "points": 0
                },
                {
                    "month": "APRIL 2025",
                    "points": 231
                }
            ],
            "totalPoints": 231
        },
        {
            "customerId": "C004",
            "monthlyRewards": [
                {
                    "month": "APRIL 2025",
                    "points": 431
                }
            ],
            "totalPoints": 431
        }
    ]
}

**Sample Request**

GET api/customer/reward/1001?startDate=2025-01-01&endDate=2025-03-31

**Sample Response**

{ 
    "status": "SUCCESS",
    "message": "Customer details fetched successfully",
    "data": {
        "customerId": "C001",
        "startDate": "2025-01-01",
        "endDate": "2025-03-31",
        "monthlyRewards": [
            {
                "month": "JANUARY 2025",
                "points": 115
            },
            {
                "month": "FEBRUARY 2025",
                "points": 49
            }
        ],
        "totalPoints": 164,
        "transactions": [
            {
                "id": 1,
                "customerId": "C001",
                "amount": 120.0,
                "transactionDate": "2025-01-10"
            },
            {
                "id": 2,
                "customerId": "C001",
                "amount": 75.0,
                "transactionDate": "2025-01-20"
            },
            {
                "id": 4,
                "customerId": "C001",
                "amount": 99.0,
                "transactionDate": "2025-02-25"
            }
        ]
    }
}

---

🛠 Author

Created by : Jitendra Sharma
