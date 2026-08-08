CREATE DATABASE IF NOT EXISTS smart_municipality_db;
USE smart_municipality_db;

-- 1. Centralized Users Table
CREATE TABLE users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       full_name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       phone_number VARCHAR(15) NOT NULL,
                       role ENUM('CITIZEN', 'EMPLOYEE', 'OFFICER', 'FIELD_WORKER', 'ADMIN') NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Citizens Profile Table
CREATE TABLE citizens (
                          citizen_id INT AUTO_INCREMENT PRIMARY KEY,
                          user_id INT UNIQUE NOT NULL,
                          citizenship_number VARCHAR(50) UNIQUE NOT NULL,
                          ward_number INT NOT NULL,
                          address VARCHAR(150) NOT NULL,
                          FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 3. Employees Profile Table
CREATE TABLE employees (
                           employee_id INT AUTO_INCREMENT PRIMARY KEY,
                           user_id INT UNIQUE NOT NULL,
                           department VARCHAR(100) NOT NULL,
                           designation VARCHAR(100) NOT NULL,
                           FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 4. Complaints Management Table
CREATE TABLE complaints (
                            complaint_id INT AUTO_INCREMENT PRIMARY KEY,
                            citizen_id INT NOT NULL,
                            category ENUM('GARBAGE', 'WATER', 'DRAINAGE', 'ROAD', 'STREETLIGHT', 'OTHER') NOT NULL,
                            title VARCHAR(150) NOT NULL,
                            description TEXT NOT NULL,
                            location_ward INT NOT NULL,
                            status ENUM('PENDING', 'ASSIGNED', 'IN_PROGRESS', 'RESOLVED', 'REJECTED') DEFAULT 'PENDING',
                            assigned_employee_id INT DEFAULT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (citizen_id) REFERENCES citizens(citizen_id),
                            FOREIGN KEY (assigned_employee_id) REFERENCES employees(employee_id)
);

-- 5. Certificate Applications Table
CREATE TABLE certificate_applications (
                                          application_id INT AUTO_INCREMENT PRIMARY KEY,
                                          citizen_id INT NOT NULL,
                                          certificate_type ENUM('BIRTH', 'DEATH', 'RESIDENCE', 'BUSINESS_REGISTRATION') NOT NULL,
                                          details TEXT DEFAULT NULL,
                                          status ENUM('SUBMITTED', 'UNDER_REVIEW', 'APPROVED', 'REJECTED') DEFAULT 'SUBMITTED',
                                          applied_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          reviewed_by_officer_id INT DEFAULT NULL,
                                          FOREIGN KEY (citizen_id) REFERENCES citizens(citizen_id),
                                          FOREIGN KEY (reviewed_by_officer_id) REFERENCES employees(employee_id)
);

-- 6. Property Tax Records Table
CREATE TABLE property_taxes (
                                tax_id INT AUTO_INCREMENT PRIMARY KEY,
                                citizen_id INT NOT NULL,
                                property_number VARCHAR(50) NOT NULL,
                                taxable_amount DECIMAL(10, 2) NOT NULL,
                                fiscal_year VARCHAR(20) NOT NULL,
                                status ENUM('UNPAID', 'PAID') DEFAULT 'UNPAID',
                                payment_date DATE DEFAULT NULL,
                                FOREIGN KEY (citizen_id) REFERENCES citizens(citizen_id)
);

-- 7. Public Notices Table
CREATE TABLE public_notices (
                                notice_id INT AUTO_INCREMENT PRIMARY KEY,
                                title VARCHAR(200) NOT NULL,
                                content TEXT NOT NULL,
                                posted_by INT NOT NULL,
                                posted_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (posted_by) REFERENCES employees(employee_id)
);

CREATE TABLE IF NOT EXISTS tax_payments (
                                            payment_id INT AUTO_INCREMENT PRIMARY KEY,
                                            citizen_id INT NOT NULL,
                                            tax_type VARCHAR(50) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    transaction_ref VARCHAR(100) NOT NULL UNIQUE,
    status VARCHAR(20) DEFAULT 'COMPLETED',
    paid_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (citizen_id) REFERENCES citizens(citizen_id) ON DELETE CASCADE
    );