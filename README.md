# 🏛️ Smart Municipality Management System

**A Digital Solution for Kasthamandap Municipality**

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-0078D7?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
![XAMPP](https://img.shields.io/badge/XAMPP-FB7A24?style=for-the-badge&logo=xampp&logoColor=white)

---

## 📖 About The Project

The **Smart Municipality Management System** is a desktop application designed to digitize and streamline the daily operations of the Kasthamandap Municipality. It bridges the gap between citizens and municipal officers by providing a seamless, paperless platform for filing complaints, applying for certificates, and paying local taxes.

Built as a core **Object-Oriented Programming (OOP)** project, it strictly follows the **DAO (Data Access Object)** design pattern to ensure a clean separation between the user interface and database logic.

---

## ✨ Key Features

### 🧑🤝‍🧑 Citizen Portal
- **Complaint Management:** File and track complaints across 4 categories (Electricity, Water, Waste, Disputes).
- **Certificate Applications:** Apply for Birth and Death certificates with dynamic, type-specific forms.
- **Tax & Fee Payments:** Pay Waste Management, Vehicle, Business, and Property taxes.
- **Live Auto-Calculation:** Real-time calculation of tax amounts based on user input.
- **Instant Receipts:** Automatic generation of `.txt` payment receipts upon successful transaction.

### 🛡️ Officer Dashboard
- **Complaint Resolution:** View all citizen complaints and update their status (Pending ➡️ In Progress ➡️ Resolved).
- **Certificate Processing:** Approve or Reject birth/death certificate applications.
- **Financial Tracking:** View a complete ledger of all tax and fee payments.
- **Audit Logging:** Secure, real-time tracking of all system actions and user logins.

---

## 🛠️ Tech Stack & Architecture

| Layer      | Technology                          |
| :--------- | :---------------------------------- |
| Frontend   | JavaFX (styled with custom CSS)     |
| Backend    | Core Java (OOP Principles)          |
| Database   | MySQL (managed via XAMPP)           |
| Pattern    | DAO (Data Access Object) + MVC-inspired |

---

## 👥 Team Babbal

Developed by **Team Babbal** — **KFA Business School and IT**
**Submitted To:** Mr. Sanju Shrestha

| Name               | Role / Module Responsibility                        |
| :----------------- | :-------------------------------------------------- |
| **Ruben Bhattarai**    | Project Lead, Database Architect & Backend Structure |
| **Aava Shrestha**      | Authentication, Security & Audit Logging             |
| **Nishan Subedi**      | Citizen Services, UI Development & Complaint Module  |
| **Pratyush Tamrakar**  | Financial Module, Auto-Calculation & Officer Console |

---

## 🚀 Getting Started (How to Run)

### Prerequisites
- Java JDK 11 or higher
- JavaFX SDK (added to your IDE's module path / classpath)
- XAMPP (for the MySQL database)
- IntelliJ IDEA, NetBeans, or Eclipse

### Installation & Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/PratyushTamrakar/Smart-Municipality-Management-System
   ```

2. **Set up the database:**
    - Start **MySQL** in the XAMPP Control Panel.
    - Open `http://localhost/phpmyadmin` in your browser.
    - Click the **SQL** tab, paste the full contents of `schema.sql`, and click **Go**.

3. **Configure the database connection:**
    - Open `src/util/DatabaseConnection.java`.
    - Update `USER` and `PASSWORD` to match your local MySQL credentials.
      *(Note: the default XAMPP root password is usually blank `""`.)*
   ```java
   private static final String USER = "root";
   private static final String PASSWORD = ""; // change if you set a custom password
   ```

4. **Run the application:**
    - Run **`MainLauncher.java`** (not `Main.java`) to avoid JavaFX module-path issues.

---

## 🔑 Default Credentials

An officer account is pre-seeded by `schema.sql` for testing:

| Role    | Email                          | Password     |
| :------ | :----------------------------- | :----------- |
| Officer | `officer@kasthamandap.gov.np`  | `officer123` |

> Citizens can self-register using the **"New citizen? Register here"** button on the login screen.

---

## 📂 Project Structure

```text
Smart-Municipality-Management-System/
└── src/
    ├── enums/          # Type-safe enumerations (Roles, Statuses, Categories)
    ├── model/          # POJO classes representing database entities
    ├── dao/            # Data Access Objects (Interfaces & Implementations)
    ├── util/           # DB Connection, Audit Logger, Receipt Generator
    ├── gui/            # JavaFX views (Login, Citizen & Officer Dashboards)
    ├── Main.java       # JavaFX Application entry point
    ├── MainLauncher.java # Launcher to bypass JavaFX module-path issues
    ├── schema.sql      # Database schema + seed data
    └── style.css       # Application-wide JavaFX stylesheet
```

---

## 🎓 OOP Concepts Applied

- **Encapsulation** — Private model attributes secured via getters and setters.
- **Abstraction** — DAO interfaces hide complex SQL logic from the GUI layer.
- **Polymorphism** — Constructor overloading in models; `toString()` overriding in enums.
- **Inheritance** — Interface extension (`MunicipalDAO`) and JavaFX `Application` inheritance.

---

## 📸 Screenshots

<!-- Uncomment and add your images after creating a /screenshots folder -->
<!-- ![Login Screen](screenshots/login.png) -->
<!-- ![Citizen Dashboard](screenshots/citizen.png) -->
<!-- ![Officer Dashboard](screenshots/officer.png) -->

---

## 🔮 Future Enhancements

- Integration with real payment gateways (eSewa, Khalti, Connect IPS APIs).
- SMS and email notifications for complaint status updates.
- Upgrade from `.txt` receipts to professional PDF generation.
- BCrypt password hashing for enhanced security.

---

## 📄 License

This project was created for academic purposes at **KFA Business School and IT**.