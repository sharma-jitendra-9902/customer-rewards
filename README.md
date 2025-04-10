# Customer Rewards Program (Spring Boot)

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

✅ Example Response
json

{
  "status": "SUCCESS",
  "message": "Rewards fetched successfully",
  "data": [
    {
      "customerId": "C001",
      "monthlyRewards": [
        { "month": "January 2025", "points": 90 }
      ],
      "totalPoints": 90
    }
  ]
}

---

🛠 Author

Created by : Jitendra Sharma
