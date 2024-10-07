Housing Notification Project
📖 Description
This project automates the notification process for users based on their preferred housing locations. It scrapes housing availability from various sources and notifies users when a match is found.

🏗️ Project Overview
User Registration (Angular Front-End): Users register through an Angular application, providing their email, phone number, and selecting their preferred locations for housing notifications.
Back-End Logic (Spring Boot): Handles user registration, saves data in an SQL database, and listens to Kafka for new housing data.
Data Extraction (Python Script): Extracts housing information from websites such as Leboncoin and MesServices (CROUS) and sends it to Kafka.
Apache Kafka: Facilitates message exchange between Python and Spring Boot services for seamless communication.
Notification Service: Notifies users via email or SMS when new housing opportunities become available.

![image](https://github.com/user-attachments/assets/70faf413-91b2-47da-898a-6256548bf82d)
