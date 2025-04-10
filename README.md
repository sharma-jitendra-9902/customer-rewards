![image](https://github.com/user-attachments/assets/75a3d2f9-3305-4337-be04-e53560e35553)# Customer Rewards Program (Spring Boot)

A simple Spring Boot application to calculate customer reward points based on recent transactions (purchase).

---

## 🔗 API Details

Method	Endpoint	Description
- GET     /health **health endpoint**
- GET	    /rewards	**Get all customer rewards**
- GET	    /customer/reward/{customerId}	**Get rewards for a specific customer**
- GET	    /customer/reward/{customerId}?startDate=yyyy-MM-dd&endDate=yyyy-MM-dd	**Get rewards for a specific customer via filtering date range**
- POST    /store/customer/data **store customer data**

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

✅ Stubbed database data

INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (1, 'C001', 120.0, '2025-01-10');
INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (2, 'C001', 75.0, '2025-01-20');
INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (3, 'C002', 200.0, '2025-02-05');
INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (4, 'C001', 99.0, '2025-02-25');
INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (5, 'C002', 130.0, '2025-03-15');
INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (6, 'C003', 45.0, '2025-03-30');
INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (7, 'C001', 120.0, '2024-12-10');
INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (7, 'C003', 190.5, '2024-04-10');
INSERT INTO transaction (id, customerId, amount, transactionDate) VALUES (7, 'C004', 290.5, '2024-04-10');

---

✅ Example Response

json

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

---

🛠 Author

Created by : Jitendra Sharma
