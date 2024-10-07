Housing Notification Project
📖 Description
This project automates the notification process for users based on their preferred housing locations. It scrapes housing availability from various sources and notifies users when a match is found.

🏗️ Project Overview
User Registration (Angular Front-End): Users register through an Angular application, providing their email, phone number, and selecting their preferred locations for housing notifications.
Back-End Logic (Spring Boot): Handles user registration, saves data in an SQL database, and listens to Kafka for new housing data.
Data Extraction (Python Script): Extracts housing information from websites such as Leboncoin and MesServices (CROUS) and sends it to Kafka.
Apache Kafka: Facilitates message exchange between Python and Spring Boot services for seamless communication.
Notification Service: Notifies users via email or SMS when new housing opportunities become available.
⚙️ Technologies Used
Front-End: Angular
Back-End: Spring Boot
Database: SQL Database (e.g., MySQL)
Data Extraction: Python
Messaging: Apache Kafka
Notification: Email & SMS Services
🚀 How It Works
Users register via an Angular web interface, providing the required information.
User data is saved in an SQL database via Spring Boot.
The Python script scrapes available housing data from Leboncoin and MesServices (CROUS).
Extracted data is produced to Kafka and saved as a CSV file.
Spring Boot listens for Kafka messages and matches users based on their preferences.
Users are notified about matching housing opportunities via email or SMS.
📋 Prerequisites
Node.js & Angular CLI: To run the front-end.
Java & Spring Boot: To run the backend.
Python 3.x: To run the data extraction script.
Apache Kafka: For inter-service communication.
SQL Database: For data storage.

![image](https://github.com/user-attachments/assets/70faf413-91b2-47da-898a-6256548bf82d)
