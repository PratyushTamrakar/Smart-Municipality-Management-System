DROP DATABASE IF EXISTS smart_municipality_db;
CREATE DATABASE smart_municipality_db;
USE smart_municipality_db;

CREATE TABLE users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       full_name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) DEFAULT 'CITIZEN'
);

CREATE TABLE citizens (
                          citizen_id INT PRIMARY KEY,
                          full_name VARCHAR(100) NOT NULL,
                          email VARCHAR(100) UNIQUE NOT NULL,
                          phone VARCHAR(20),
                          ward_number INT DEFAULT 1,
                          FOREIGN KEY (citizen_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE complaints (
                            complaint_id INT AUTO_INCREMENT PRIMARY KEY,
                            citizen_id INT NOT NULL,
                            category VARCHAR(50) NOT NULL,
                            title VARCHAR(150) NOT NULL,
                            description TEXT NOT NULL,
                            ward_number INT NOT NULL,
                            status VARCHAR(20) DEFAULT 'PENDING',
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (citizen_id) REFERENCES users(user_id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS certificate_applications (
                                                        application_id INT AUTO_INCREMENT PRIMARY KEY,
                                                        citizen_id INT NOT NULL,
                                                        certificate_type VARCHAR(50) NOT NULL,
    applicant_name VARCHAR(100) NOT NULL,
    details TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (citizen_id) REFERENCES users(user_id) ON DELETE CASCADE
    );


CREATE TABLE IF NOT EXISTS tax_payments (
                                            payment_id INT AUTO_INCREMENT PRIMARY KEY,
                                            citizen_id INT NOT NULL,
                                            tax_type VARCHAR(50) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_status VARCHAR(20) DEFAULT 'PAID',
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (citizen_id) REFERENCES users(user_id) ON DELETE CASCADE
    );
USE smart_municipality_db;

-- 1. Create Audit Logs table for system traceability
CREATE TABLE IF NOT EXISTS audit_logs (
                                          log_id INT AUTO_INCREMENT PRIMARY KEY,
                                          user_id INT NOT NULL,
                                          action_type VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
    );

-- 2. Create Notifications table for Citizen updates
CREATE TABLE IF NOT EXISTS notifications (
                                             notification_id INT AUTO_INCREMENT PRIMARY KEY,
                                             user_id INT NOT NULL,
                                             message TEXT NOT NULL,
                                             is_read BOOLEAN DEFAULT FALSE,
                                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                             FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
    );

-- 3. Modify Users table password column to accommodate BCrypt hash strings (60 chars)
ALTER TABLE users MODIFY COLUMN password VARCHAR(255) NOT NULL;