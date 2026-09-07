CREATE DATABASE IF NOT EXISTS kasthamandap_municipality;
USE kasthamandap_municipality;

CREATE TABLE IF NOT EXISTS users (
                                     id INT PRIMARY KEY AUTO_INCREMENT,
                                     full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('CITIZEN', 'OFFICER') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS complaints (
                                          id INT PRIMARY KEY AUTO_INCREMENT,
                                          citizen_id INT NOT NULL,
                                          citizen_name VARCHAR(100),
    category VARCHAR(50) NOT NULL,       -- Electricity / Water / Waste / Dispute with Neighbours
    description TEXT NOT NULL,
    ward_no VARCHAR(20),
    house_no VARCHAR(20),
    status ENUM('PENDING', 'IN_PROGRESS', 'RESOLVED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (citizen_id) REFERENCES users(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS certificate_applications (
                                                        application_id INT PRIMARY KEY AUTO_INCREMENT,
                                                        citizen_id INT NOT NULL,
                                                        certificate_type VARCHAR(30) NOT NULL,   -- Birth Certificate / Death Certificate
    person_name VARCHAR(100) NOT NULL,
    relation VARCHAR(50) NOT NULL,
    citizenship_number VARCHAR(50) NULL,     -- required for Death
    hospital_reg_number VARCHAR(50) NULL,    -- required for Birth
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (citizen_id) REFERENCES users(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS payments (
                                        payment_id INT PRIMARY KEY AUTO_INCREMENT,
                                        citizen_id INT NOT NULL,
                                        citizen_name VARCHAR(100),
    payment_type VARCHAR(50) NOT NULL,   -- Waste Management Fee / Vehicle Tax / Business Tax / Property Tax
    details TEXT,
    ward_no VARCHAR(20),
    house_no VARCHAR(20),
    months INT NULL,
    vehicle_cc INT NULL,
    business_type VARCHAR(100) NULL,
    business_tier VARCHAR(30) NULL,
    property_value DOUBLE NULL,
    amount DOUBLE NOT NULL,
    gateway VARCHAR(30) NOT NULL,        -- eSewa / Khalti / Connect IPS
    payment_status VARCHAR(20) DEFAULT 'PAID',
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (citizen_id) REFERENCES users(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS audit_logs (
                                          log_id INT PRIMARY KEY AUTO_INCREMENT,
                                          user_id INT NOT NULL,
                                          user_role VARCHAR(50) NOT NULL,
    action VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Sample officer account for testing (password: officer123)
INSERT INTO users (full_name, email, password, role)
VALUES ('Officer Sharma', 'officer@kasthamandap.gov.np', 'officer123', 'OFFICER');