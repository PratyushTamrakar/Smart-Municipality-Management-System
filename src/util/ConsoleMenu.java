package util;

import dao.*;
import model.*;
import model.enums.*;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class ConsoleMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final UserDAO userDAO = new UserDAOImpl();
    private final ComplaintDAO complaintDAO = new ComplaintDAOImpl();
    private final CertificateDAO certificateDAO = new CertificateDAOImpl();
    private final TaxDAO taxDAO = new TaxDAOImpl();

    private User currentUser = null;

    public void start() {
        while (true) {
            System.out.println("\n=========================================");
            System.out.println("  SMART MUNICIPALITY MANAGEMENT SYSTEM   ");
            System.out.println("=========================================");

            if (currentUser == null) {
                System.out.println("1. Register Account");
                System.out.println("2. Login");
                System.out.println("0. Exit");
                System.out.print("Select an option: ");

                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1" -> handleRegister();
                    case "2" -> handleLogin();
                    case "0" -> {
                        System.out.println("Exiting system. Goodbye!");
                        return;
                    }
                    default -> System.out.println("❌ Invalid choice. Try again.");
                }
            } else {
                showDashboard();
            }
        }
    }

    private void handleRegister() {
        System.out.println("\n--- Register User ---");
        System.out.print("Full Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String pass = scanner.nextLine().trim();
        System.out.print("Phone Number: ");
        String phone = scanner.nextLine().trim();

        System.out.println("Select Role: 1. CITIZEN  2. OFFICER  3. ADMIN");
        System.out.print("Choice: ");
        String roleChoice = scanner.nextLine().trim();
        Role role = switch (roleChoice) {
            case "2" -> Role.OFFICER;
            case "3" -> Role.ADMIN;
            default -> Role.CITIZEN;
        };

        User user = new User(name, email, PasswordUtil.hashPassword(pass), phone, role);
        if (userDAO.registerUser(user)) {
            System.out.println("✅ Registration successful! User ID: " + user.getUserId());
        }
    }

    private void handleLogin() {
        System.out.println("\n--- User Login ---");
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String pass = scanner.nextLine().trim();

        Optional<User> userOpt = userDAO.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (PasswordUtil.verifyPassword(pass, user.getPasswordHash())) {
                currentUser = user;
                System.out.println("✅ Welcome back, " + user.getFullName() + " (" + user.getRole() + ")");
            } else {
                System.out.println("❌ Invalid password!");
            }
        } else {
            System.out.println("❌ User with this email does not exist!");
        }
    }

    private void showDashboard() {
        System.out.println("\n--- LOGGED IN AS: " + currentUser.getFullName() + " [" + currentUser.getRole() + "] ---");

        if (currentUser.getRole() == Role.CITIZEN) {
            showCitizenMenu();
        } else {
            showOfficerMenu();
        }
    }

    private void showCitizenMenu() {
        System.out.println("1. File a Complaint");
        System.out.println("2. View My Complaints");
        System.out.println("3. Apply for Certificate");
        System.out.println("4. View My Certificate Applications");
        System.out.println("5. Pay Municipal Tax");
        System.out.println("6. View Tax History");
        System.out.println("0. Logout");
        System.out.print("Select choice: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> fileComplaint();
            case "2" -> viewMyComplaints();
            case "3" -> applyCertificate();
            case "4" -> viewMyCertificates();
            case "5" -> payTax();
            case "6" -> viewMyTaxes();
            case "0" -> logout();
            default -> System.out.println("❌ Invalid option.");
        }
    }

    private void showOfficerMenu() {
        System.out.println("1. View All Complaints");
        System.out.println("2. View All Certificate Applications");
        System.out.println("3. View Total Municipal Revenue");
        System.out.println("0. Logout");
        System.out.print("Select choice: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> {
                System.out.println("\n--- System Complaints ---");
                complaintDAO.getAllComplaints().forEach(c ->
                        System.out.println("Ticket #" + c.getComplaintId() + " [" + c.getCategory() + "] " + c.getTitle() + " | Status: " + c.getStatus())
                );
            }
            case "2" -> {
                System.out.println("\n--- System Certificate Applications ---");
                certificateDAO.getAllApplications().forEach(a ->
                        System.out.println("App #" + a.getApplicationId() + " [" + a.getCertificateType() + "] Citizen ID: " + a.getCitizenId() + " | Status: " + a.getStatus())
                );
            }
            case "3" -> System.out.println("\n💰 Total Revenue Collected: NPR " + taxDAO.getTotalRevenueCollected());
            case "0" -> logout();
            default -> System.out.println("❌ Invalid option.");
        }
    }

    private void fileComplaint() {
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Description: ");
        String desc = scanner.nextLine().trim();
        System.out.print("Ward Number: ");
        int ward = Integer.parseInt(scanner.nextLine().trim());

        System.out.println("Category: 1. ROAD  2. WATER  3. WASTE  4. ELECTRICITY");
        String catChoice = scanner.nextLine().trim();
        ComplaintCategory cat = switch (catChoice) {
            case "2" -> ComplaintCategory.WATER;
            case "3" -> ComplaintCategory.WASTE;
            case "4" -> ComplaintCategory.ELECTRICITY;
            default -> ComplaintCategory.ROAD;
        };

        // Assumes citizen_id corresponds to user_id for CLI testing
        Complaint c = new Complaint(currentUser.getUserId(), cat, title, desc, ward);
        if (complaintDAO.createComplaint(c)) {
            System.out.println("✅ Complaint filed! Ticket ID: " + c.getComplaintId());
        }
    }

    private void viewMyComplaints() {
        System.out.println("\n--- My Complaints ---");
        List<Complaint> list = complaintDAO.getComplaintsByCitizenId(currentUser.getUserId());
        if (list.isEmpty()) {
            System.out.println("No complaints found.");
        } else {
            list.forEach(c -> System.out.println("Ticket #" + c.getComplaintId() + " [" + c.getCategory() + "] " + c.getTitle() + " | Status: " + c.getStatus()));
        }
    }

    private void applyCertificate() {
        System.out.print("Details / Purpose: ");
        String details = scanner.nextLine().trim();

        System.out.println("Certificate Type: 1. BIRTH  2. DEATH  3. RESIDENCE  4. BUSINESS_REGISTRATION");
        String typeChoice = scanner.nextLine().trim();
        CertificateType type = switch (typeChoice) {
            case "2" -> CertificateType.DEATH;
            case "3" -> CertificateType.RESIDENCE;
            case "4" -> CertificateType.BUSINESS_REGISTRATION;
            default -> CertificateType.BIRTH;
        };

        CertificateApplication app = new CertificateApplication(currentUser.getUserId(), type, details);
        if (certificateDAO.applyForCertificate(app)) {
            System.out.println("✅ Application submitted! Application ID: " + app.getApplicationId());
        }
    }

    private void viewMyCertificates() {
        System.out.println("\n--- My Certificate Applications ---");
        List<CertificateApplication> list = certificateDAO.getApplicationsByCitizenId(currentUser.getUserId());
        if (list.isEmpty()) {
            System.out.println("No applications found.");
        } else {
            list.forEach(a -> System.out.println("App #" + a.getApplicationId() + " [" + a.getCertificateType() + "] Status: " + a.getStatus()));
        }
    }

    private void payTax() {
        System.out.print("Amount (NPR): ");
        double amount = Double.parseDouble(scanner.nextLine().trim());

        System.out.println("Tax Type: 1. PROPERTY_TAX  2. BUSINESS_TAX  3. VEHICLE_TAX  4. WASTE_MANAGEMENT_FEE");
        String taxChoice = scanner.nextLine().trim();
        TaxType type = switch (taxChoice) {
            case "2" -> TaxType.BUSINESS_TAX;
            case "3" -> TaxType.VEHICLE_TAX;
            case "4" -> TaxType.WASTE_MANAGEMENT_FEE;
            default -> TaxType.PROPERTY_TAX;
        };

        String txnRef = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        TaxPayment payment = new TaxPayment(currentUser.getUserId(), type, amount, txnRef);

        if (taxDAO.recordTaxPayment(payment)) {
            System.out.println("✅ Payment Successful! Transaction Ref: " + txnRef);
        }
    }

    private void viewMyTaxes() {
        System.out.println("\n--- My Tax Payments ---");
        List<TaxPayment> list = taxDAO.getPaymentsByCitizenId(currentUser.getUserId());
        if (list.isEmpty()) {
            System.out.println("No tax payment history.");
        } else {
            list.forEach(p -> System.out.println("Txn #" + p.getTransactionRef() + " [" + p.getTaxType() + "] NPR " + p.getAmount() + " | Status: " + p.getStatus()));
        }
    }

    private void logout() {
        System.out.println("👋 Logged out successfully.");
        currentUser = null;
    }
}