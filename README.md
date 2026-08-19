# 🏛️ Smart Municipality Management System

### Kasthamandap Municipality

A full-stack, desktop-based **Smart Municipality Management System** developed using **Java, JavaFX, and MySQL**.

The system digitizes essential municipal services by allowing citizens to register complaints, calculate and pay municipal taxes, track activities, and receive digital receipts. Municipal officers can manage complaints, monitor their status, and maintain an audit trail through a centralized administrative dashboard.

---

## 📌 Overview

The **Smart Municipality Management System** is designed to provide a simple and efficient digital platform connecting **citizens and municipal officers**.

### 👤 Citizen Side

Citizens can:

* Create and manage their accounts
* Log in securely
* Submit ward-based municipal complaints
* Calculate and pay different types of taxes
* View payment history
* Generate digital payment receipts
* Track their submitted complaints

### 🏢 Officer Side

Municipal officers can:

* Access an administrative dashboard
* View complaints from different wards
* Update complaint statuses
* Monitor municipal activities
* Track system actions through audit logs

---

## ✨ Key Features

### 👤 Citizen Portal

#### 🔐 Account Authentication

* Citizen registration
* Login authentication
* Role-based access

#### 📝 Complaint Management

Citizens can submit complaints related to:

* 🚧 Roads
* 💧 Water
* 🗑️ Waste Management
* ⚡ Electricity
* 🏙️ Other municipal services

Each complaint is associated with the relevant ward and can be tracked through its current status.

#### 💰 Smart Tax Payment

The system supports automated calculation of:

* Property Tax
* Business Tax
* Vehicle Tax

Tax amounts are calculated dynamically using predefined municipal rate rules.

#### 🧾 Digital Receipts

After successful tax processing, the system automatically generates a local `.txt` receipt containing payment information.

#### 📊 Activity Tracking

Citizens can view their personal tax payment history directly from the dashboard.

---

### 🏢 Officer Admin Portal

#### 📊 Centralized Dashboard

Officers can view municipal complaints using an interactive JavaFX `TableView`.

#### 🔄 Complaint Status Management

Complaint progress can be updated between:

```text
PENDING
   ↓
IN_PROGRESS
   ↓
RESOLVED
```

Changes are reflected dynamically within the application.

#### 📝 Audit Trail

Important system actions are recorded through audit logs to improve:

* Accountability
* Security
* Monitoring
* System transparency

---

# 🧠 OOP & Software Design

This project was developed with a strong focus on **Object-Oriented Programming principles** and modular software architecture.

### 🔒 Encapsulation

Model classes such as:

```text
User
Complaint
TaxPayment
AuditLog
```

use private attributes with controlled access through getters and setters.

### 🧩 Abstraction

DAO interfaces separate database operations from the rest of the application.

Examples:

```text
UserDAO
ComplaintDAO
TaxDAO
AuditLogDAO
```

This prevents SQL/database implementation details from being tightly coupled with business logic.

### 🔁 Polymorphism

DAO implementations provide different concrete implementations of common interfaces.

Examples:

```text
UserDAOImpl
ComplaintDAOImpl
TaxDAOImpl
AuditLogDAOImpl
```

### 🧬 Inheritance

The application uses inheritance where appropriate to share common functionality between related classes.

### 🎯 Separation of Concerns

Different responsibilities are separated into dedicated packages:

```text
GUI          → User interface
DAO          → Database operations
Model        → Data entities
Service      → Business logic
Util         → Utility functionality
```

This makes the system easier to maintain, test, and extend.

---

# 🏗️ Project Architecture

```text
                    ┌───────────────────────┐
                    │       JavaFX UI       │
                    │  Citizen / Officer    │
                    └───────────┬───────────┘
                                │
                                ▼
                    ┌───────────────────────┐
                    │       Services        │
                    │   Business Logic      │
                    └───────────┬───────────┘
                                │
                                ▼
                    ┌───────────────────────┐
                    │         DAO           │
                    │ Database Operations   │
                    └───────────┬───────────┘
                                │
                                ▼
                    ┌───────────────────────┐
                    │        MySQL          │
                    │       Database        │
                    └───────────────────────┘
```

---

# 📁 Project Structure

```text
Smart-Municipality-Management-System/
│
├── src/
│   ├── dao/
│   │   ├── UserDAO.java
│   │   ├── UserDAOImpl.java
│   │   ├── ComplaintDAO.java
│   │   ├── ComplaintDAOImpl.java
│   │   ├── TaxDAO.java
│   │   ├── TaxDAOImpl.java
│   │   └── AuditLogDAO.java
│   │
│   ├── gui/
│   │   ├── LoginView.java
│   │   ├── CitizenDashboardView.java
│   │   └── OfficerDashboardView.java
│   │
│   ├── model/
│   │   ├── User.java
│   │   ├── Complaint.java
│   │   ├── TaxPayment.java
│   │   ├── AuditLog.java
│   │   └── enums/
│   │
│   ├── service/
│   │   └── TaxCalculatorService.java
│   │
│   ├── util/
│   │   ├── DatabaseConnection.java
│   │   └── FileExporter.java
│   │
│   ├── Main.java
│   └── MainLauncher.java
│
├── sql/
│   └── schema.sql
│
├── README.md
└── ...
```

---

# 🛠️ Tech Stack

| Technology           | Purpose                    |
| -------------------- | -------------------------- |
| ☕ Java 17+           | Core programming language  |
| 🎨 JavaFX 17+        | Desktop GUI                |
| 🗄️ MySQL            | Database                   |
| 🔌 MySQL Connector/J | Java-MySQL connectivity    |
| 💻 IntelliJ IDEA     | Development environment    |
| 📄 TXT Export        | Digital receipt generation |

---

# 🚀 Getting Started

## 1️⃣ Clone the Repository

```bash
git clone https://github.com/YOUR-USERNAME/YOUR-REPOSITORY.git
cd YOUR-REPOSITORY
```

Replace the repository URL with your actual GitHub repository URL.

---

## 2️⃣ Set Up the Database

Open **MySQL Workbench**, phpMyAdmin, or another MySQL client.

Create the database and tables using:

```text
sql/schema.sql
```

The application expects the database:

```text
kasthamandap_db
```

---

## 3️⃣ Configure Database Connection

Open:

```text
src/util/DatabaseConnection.java
```

Update your MySQL credentials:

```java
private static final String URL =
        "jdbc:mysql://localhost:3306/kasthamandap_db";

private static final String USER = "root";

private static final String PASSWORD =
        "your_password";
```

⚠️ **Important:** Do not commit real database passwords to GitHub.

For a real deployment, environment variables or a configuration file excluded through `.gitignore` should be used.

---

## 4️⃣ Run the Application

Open the project in **IntelliJ IDEA**.

Navigate to:

```text
src/MainLauncher.java
```

Then:

```text
Right Click → Run 'MainLauncher.main()'
```

---

# 🔑 Demo Credentials

| Role       | Email                         | Password                   |
| ---------- | ----------------------------- | -------------------------- |
| 🏢 Officer | `officer@kasthamandap.gov.np` | `officer123`               |
| 👤 Citizen | Register through UI           | Chosen during registration |

> ⚠️ These credentials are intended only for demonstration/development purposes.

---

# 📸 Screenshots

Add screenshots of your application here to make the repository more attractive.

Example:

```markdown
## 📸 Screenshots

### Login
![Login Screen](screenshots/login.png)

### Citizen Dashboard
![Citizen Dashboard](screenshots/citizen-dashboard.png)

### Officer Dashboard
![Officer Dashboard](screenshots/officer-dashboard.png)

### Tax Payment
![Tax Payment](screenshots/tax-payment.png)
```

Recommended folder:

```text
screenshots/
├── login.png
├── citizen-dashboard.png
├── officer-dashboard.png
└── tax-payment.png
```

---

# 🔐 Security Considerations

The current project is designed primarily as an academic/software engineering project.

For production deployment, additional security measures should be implemented, including:

* Password hashing
* Environment-based database credentials
* Input validation and sanitization
* Prepared statements for all database operations
* Session management
* Role-based authorization
* Secure logging
* HTTPS/API security if converted to a web architecture

---

# 🔮 Future Improvements

Possible future upgrades include:

* 🌐 Web-based version
* 📱 Mobile application
* 🗺️ GIS/map-based complaint tracking
* 📧 Email notifications
* 🔔 Real-time complaint notifications
* 💳 Online payment gateway integration
* 📊 Advanced analytics dashboard
* 🧑‍💼 Multiple officer roles and permissions
* 🔐 Stronger authentication and password hashing
* ☁️ Cloud database deployment
* 📄 PDF receipt generation
* 🌍 Multi-municipality support

---

# 🎯 Project Objectives

The main objectives of this project are to:

1. Digitize common municipal services.
2. Reduce manual paperwork.
3. Improve communication between citizens and officers.
4. Provide centralized complaint management.
5. Automate municipal tax calculations.
6. Maintain transparent activity records.
7. Demonstrate practical implementation of OOP principles.
8. Apply database management concepts in a real-world system.

---

# 👨‍💻 Development Focus

This project demonstrates practical implementation of:

```text
Java
│
├── Object-Oriented Programming
├── JavaFX GUI Development
├── JDBC
├── MySQL Database Management
├── DAO Architecture
├── Business Logic Separation
├── File Handling
├── Authentication
└── CRUD Operations
```

---

# 📚 Academic Project

This project was developed as part of an academic **Java Object-Oriented Programming / Software Development project** with a focus on applying programming concepts to a real-world municipal management scenario.

---

## ⭐ If You Like This Project

Give the repository a ⭐ on GitHub!

Feel free to explore, fork, and improve the project.

---

**Built with ☕ Java + 🎨 JavaFX + 🗄️ MySQL**
