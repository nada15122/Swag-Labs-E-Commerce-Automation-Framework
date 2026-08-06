# 🧪 Swag Labs (SauceDemo) E-Commerce Automation Framework

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Selenium](https://img.shields.io/badge/Selenium-43B02A?style=for-the-badge&logo=selenium&logoColor=white)
![TestNG](https://img.shields.io/badge/TestNG-FF6F00?style=for-the-badge&logo=testng&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Allure](https://img.shields.io/badge/Allure_Report-28A745?style=for-the-badge&logo=qameta&logoColor=white)

An end-to-end automated UI testing framework built for the **Swag Labs (SauceDemo)** e-commerce web application. Designed using the **Page Object Model (POM)** design pattern with **Selenium WebDriver**, **TestNG**, **Maven**, and **Allure Reports**.

---

## 🚀 Key Features

* **Design Pattern:** Page Object Model (POM) for clean abstraction, maintainability, and code reusability.
* **Synchronization:** Robust Explicit Waits applied across page elements to prevent synchronization and race-condition flakiness.
* **Test Reporting:** Full **Allure Report** integration with dynamic execution steps (`@Step`) and automated step-by-step screenshots.
* **Headless & Browser Options:** Chrome options configured to eliminate browser popups (e.g., password change prompts) and ensure clean execution environments.

---

## 🛠 Tech Stack & Tools

* **Programming Language:** Java 11+
* **Automation Tool:** Selenium WebDriver (v4.x)
* **Test Runner Framework:** TestNG
* **Build Tool:** Apache Maven
* **Reporting:** Allure Framework

---

## 📋 Test Suites & Coverage

The automated test suite covers 14 primary end-to-end user workflows:

1. **User Authentication:** Valid logins, invalid credentials, locked-out user handling, and session logouts.
2. **Product Catalog & Inventory:** Item listing verification, sorting (Name A-Z/Z-A, Price Low-High/High-Low), and details page checks.
3. **Cart Operations:** Adding/removing single & multiple items, cart badge count updates, dynamic cart persistence across pages.
4. **Checkout Workflow:** Form input validations, empty cart handling, and order confirmation.
5. **Footer & Social Links:** Redirection verification for external links (LinkedIn, Facebook, X/Twitter).

---

## 📊 Test Execution & Allure Reporting

### Prerequisites
* **Java Development Kit (JDK 11+)**
* **Apache Maven** installed and added to PATH
* **Allure CLI** installed locally

### Running Tests Locally
To execute all automated tests via Maven, run:
```bash
mvn clean test


