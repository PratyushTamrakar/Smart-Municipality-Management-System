package util;

import dao.AuditLogDAO;
import dao.AuditLogDAOImpl;
import dao.CertificateDAO;
import dao.CertificateDAOImpl;
import dao.ComplaintDAO;
import dao.ComplaintDAOImpl;
import dao.TaxDAO;
import dao.TaxDAOImpl;
import dao.UserDAO;
import dao.UserDAOImpl;
import model.AuditLog;
import model.CertificateApplication;
import model.Complaint;
import model.TaxPayment;
import model.User;
import model.enums.ComplaintCategory;
import model.enums.ComplaintStatus;
import model.enums.Role;

import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private final UserDAO userDAO = new UserDAOImpl();
    private final ComplaintDAO complaintDAO = new ComplaintDAOImpl();
    private final CertificateDAO certificateDAO = new CertificateDAOImpl();
    private final TaxDAO taxDAO = new TaxDAOImpl();
    private final AuditLogDAO auditLogDAO = new AuditLogDAOImpl();
    private final Scanner scanner = new Scanner(System.in);
    private User loggedInUser = null;

    public void start() {
        while (true) {
            if (loggedInUser == null) {
                showAuthMenu();
            } else if (loggedInUser.getRole() == Role.CITIZEN) {
                showCitizenMenu();
            } else {
                showOfficerMenu();
            }
        }
    }

    private void showAuthMenu() {
        System.out.println("\n==========================================");
        System.out.println("  SMART MUNICIPALITY MANAGEMENT SYSTEM  ");
        System.out.println("==========================================");
        System.out.println("1. Login");
        System.out.println("2. Register Citizen Account");
        System.out.println("0. Exit");
        System.out.print("Select choice: ");

        if (!scanner.hasNextInt()) {
            System.out.println("❌ Invalid input!");
            scanner.nextLine();
            return;
        }
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> login();
            case 2 -> register();
            case 0 -> {
                System.out.println("Thank you for using Smart Municipality System. Goodbye!");
                System.exit(0);
            }
            default -> System.out.println("❌ Invalid choice!");
        }
    }

    private void login() {
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        var userOpt = userDAO.login(email, password);
        if (userOpt.isPresent()) {
            loggedInUser = userOpt.get();
            System.out.println("✅ Login successful! Welcome, " + loggedInUser.getFullName() + " (" + loggedInUser.getRole() + ")");
            auditLogDAO.logAction(new AuditLog(loggedInUser.getUserId(), "USER_LOGIN", "User logged into system."));
        } else {
            System.out.println("❌ Invalid credentials!");
        }
    }

    private void register() {
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        User user = new User();
        user.setFullName(name);
        user.setEmail(email);
        user.setPassword(password);

        if (userDAO.registerUser(user)) {
            System.out.println("✅ Registration successful! Please log in.");
            auditLogDAO.logAction(new AuditLog(user.getUserId(), "USER_REGISTER", "New citizen account registered."));
        } else {
            System.out.println("❌ Registration failed!");
        }
    }

    private void showCitizenMenu() {
        System.out.println("\n--- CITIZEN PANEL: " + loggedInUser.getFullName() + " ---");
        System.out.println("1. File a Complaint");
        System.out.println("2. View My Complaints");
        System.out.println("3. Apply for Certificate");
        System.out.println("4. View My Certificate Applications");
        System.out.println("5. Pay Municipal Tax (Automated Calculator)");
        System.out.println("6. View Tax Payment History");
        System.out.println("0. Logout");
        System.out.print("Select choice: ");

        if (!scanner.hasNextInt()) {
            System.out.println("❌ Invalid choice!");
            scanner.nextLine();
            return;
        }
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> fileComplaint();
            case 2 -> viewMyComplaints();
            case 3 -> applyForCertificate();
            case 4 -> viewMyCertificates();
            case 5 -> payTax();
            case 6 -> viewMyTaxHistory();
            case 0 -> {
                auditLogDAO.logAction(new AuditLog(loggedInUser.getUserId(), "USER_LOGOUT", "Citizen logged out."));
                loggedInUser = null;
                System.out.println("Logged out successfully.");
            }
            default -> System.out.println("❌ Invalid choice!");
        }
    }

    private void showOfficerMenu() {
        System.out.println("\n--- OFFICER / ADMIN PANEL: " + loggedInUser.getFullName() + " ---");
        System.out.println("1. View All Complaints");
        System.out.println("2. Update Complaint Status");
        System.out.println("3. View All Certificate Applications");
        System.out.println("4. Process Certificate Application (Approve & Export)");
        System.out.println("5. View All System Tax Receipts");
        System.out.println("6. View System Audit Trail");
        System.out.println("0. Logout");
        System.out.print("Select choice: ");

        if (!scanner.hasNextInt()) {
            System.out.println("❌ Invalid choice!");
            scanner.nextLine();
            return;
        }
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> viewAllComplaints();
            case 2 -> updateComplaintStatus();
            case 3 -> viewAllCertificates();
            case 4 -> processCertificateApplication();
            case 5 -> viewAllTaxReceipts();
            case 6 -> viewAuditLogs();
            case 0 -> {
                auditLogDAO.logAction(new AuditLog(loggedInUser.getUserId(), "USER_LOGOUT", "Officer logged out."));
                loggedInUser = null;
                System.out.println("Logged out successfully.");
            }
            default -> System.out.println("❌ Invalid choice!");
        }
    }

    private void fileComplaint() {
        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Ward Number: ");
        if (!scanner.hasNextInt()) {
            System.out.println("❌ Invalid Ward Number!");
            scanner.nextLine();
            return;
        }
        int wardNumber = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Category: 1. ROAD  2. WATER  3. WASTE  4. ELECTRICITY");
        System.out.print("Select category: ");
        if (!scanner.hasNextInt()) {
            System.out.println("❌ Invalid category!");
            scanner.nextLine();
            return;
        }
        int catChoice = scanner.nextInt();
        scanner.nextLine();

        ComplaintCategory category = switch (catChoice) {
            case 1 -> ComplaintCategory.ROAD;
            case 2 -> ComplaintCategory.WATER;
            case 3 -> ComplaintCategory.WASTE;
            case 4 -> ComplaintCategory.ELECTRICITY;
            default -> ComplaintCategory.ROAD;
        };

        Complaint complaint = new Complaint(loggedInUser.getUserId(), category, title, description, wardNumber);

        if (complaintDAO.createComplaint(complaint)) {
            System.out.println("✅ Complaint submitted successfully! ID: " + complaint.getComplaintId());
            auditLogDAO.logAction(new AuditLog(loggedInUser.getUserId(), "FILE_COMPLAINT", "Filed complaint ID " + complaint.getComplaintId()));
        } else {
            System.out.println("❌ Error creating complaint.");
        }
    }

    private void viewMyComplaints() {
        List<Complaint> complaints = complaintDAO.getComplaintsByCitizenId(loggedInUser.getUserId());
        if (complaints.isEmpty()) {
            System.out.println("No complaints found.");
        } else {
            System.out.println("\n--- YOUR COMPLAINTS ---");
            for (Complaint c : complaints) {
                System.out.println("ID: " + c.getComplaintId() + " | Title: " + c.getTitle() +
                        " | Category: " + c.getCategory() + " | Status: " + c.getStatus());
            }
        }
    }

    private void applyForCertificate() {
        System.out.println("\nSelect Certificate Type:");
        System.out.println("1. Birth Certificate");
        System.out.println("2. Death Certificate");
        System.out.println("3. Relationship Verification");
        System.out.print("Choice: ");
        if (!scanner.hasNextInt()) {
            System.out.println("❌ Invalid choice!");
            scanner.nextLine();
            return;
        }
        int choice = scanner.nextInt();
        scanner.nextLine();

        String certType = switch (choice) {
            case 1 -> "BIRTH_CERTIFICATE";
            case 2 -> "DEATH_CERTIFICATE";
            case 3 -> "RELATIONSHIP_VERIFICATION";
            default -> "BIRTH_CERTIFICATE";
        };

        System.out.print("Applicant Full Name: ");
        String applicantName = scanner.nextLine();

        System.out.print("Additional Details / Remarks: ");
        String details = scanner.nextLine();

        CertificateApplication app = new CertificateApplication(loggedInUser.getUserId(), certType, applicantName, details);

        if (certificateDAO.applyForCertificate(app)) {
            System.out.println("✅ Certificate application submitted! ID: " + app.getApplicationId());
            auditLogDAO.logAction(new AuditLog(loggedInUser.getUserId(), "APPLY_CERTIFICATE", "Applied for " + certType + " (App ID: " + app.getApplicationId() + ")"));
        } else {
            System.out.println("❌ Failed to submit certificate application.");
        }
    }

    private void viewMyCertificates() {
        List<CertificateApplication> list = certificateDAO.getApplicationsByCitizenId(loggedInUser.getUserId());
        if (list.isEmpty()) {
            System.out.println("No certificate applications found.");
        } else {
            System.out.println("\n--- YOUR CERTIFICATE APPLICATIONS ---");
            for (CertificateApplication app : list) {
                System.out.println("ID: " + app.getApplicationId() + " | Type: " + app.getCertificateType() +
                        " | Applicant: " + app.getApplicantName() + " | Status: " + app.getStatus());
            }
        }
    }

    private void payTax() {
        System.out.println("\n--- MUNICIPAL TAX CALCULATOR & PAYMENT ---");
        System.out.println("1. Property Tax (Based on Area & Ward)");
        System.out.println("2. Business Tax (Based on Tier)");
        System.out.println("3. Vehicle Tax (Based on Engine CC)");
        System.out.print("Select choice: ");

        if (!scanner.hasNextInt()) {
            System.out.println("❌ Invalid choice!");
            scanner.nextLine();
            return;
        }
        int choice = scanner.nextInt();
        scanner.nextLine();

        String taxType = "";
        double computedAmount = 0.0;

        switch (choice) {
            case 1 -> {
                taxType = "PROPERTY_TAX";
                System.out.print("Enter Property Area (in sq. ft.): ");
                double sqFt = scanner.nextDouble();
                System.out.print("Enter Ward Number (1-10): ");
                int ward = scanner.nextInt();
                scanner.nextLine();
                computedAmount = TaxCalculatorService.calculatePropertyTax(sqFt, ward);
            }
            case 2 -> {
                taxType = "BUSINESS_TAX";
                System.out.println("Select Business Category: 1. Small Retail  2. Medium Enterprise  3. Large Corporation");
                int tier = scanner.nextInt();
                scanner.nextLine();
                computedAmount = TaxCalculatorService.calculateBusinessTax(tier);
            }
            case 3 -> {
                taxType = "VEHICLE_TAX";
                System.out.print("Enter Vehicle Engine CC (e.g., 150): ");
                int cc = scanner.nextInt();
                scanner.nextLine();
                computedAmount = TaxCalculatorService.calculateVehicleTax(cc);
            }
            default -> {
                System.out.println("❌ Invalid tax option.");
                return;
            }
        }

        System.out.printf("Calculated Tax Due: Rs. %.2f\n", computedAmount);
        System.out.print("Proceed to Pay? (1. Yes / 2. No): ");
        int confirm = scanner.nextInt();
        scanner.nextLine();

        if (confirm == 1) {
            TaxPayment payment = new TaxPayment(loggedInUser.getUserId(), taxType, computedAmount);
            if (taxDAO.payTax(payment)) {
                System.out.println("✅ Tax payment successful! Receipt ID: " + payment.getPaymentId());
                FileExporter.exportTaxReceipt(payment, loggedInUser);
                System.out.println("📄 Official Receipt exported to exports/Tax_Receipt_" + payment.getPaymentId() + ".txt");
                auditLogDAO.logAction(new AuditLog(loggedInUser.getUserId(), "PAY_TAX", "Paid Rs. " + computedAmount + " for " + taxType));
            } else {
                System.out.println("❌ Payment failed.");
            }
        } else {
            System.out.println("Payment cancelled.");
        }
    }

    private void viewMyTaxHistory() {
        List<TaxPayment> list = taxDAO.getPaymentsByCitizenId(loggedInUser.getUserId());
        if (list.isEmpty()) {
            System.out.println("No tax payment records found.");
        } else {
            System.out.println("\n--- YOUR TAX PAYMENT HISTORY ---");
            for (TaxPayment p : list) {
                System.out.println("Receipt ID: " + p.getPaymentId() + " | Type: " + p.getTaxType() +
                        " | Amount: Rs. " + p.getAmount() + " | Date: " + p.getPaymentDate());
            }
        }
    }

    private void viewAllComplaints() {
        List<Complaint> complaints = complaintDAO.getAllComplaints();
        if (complaints.isEmpty()) {
            System.out.println("No complaints registered.");
        } else {
            System.out.println("\n--- ALL COMPLAINTS ---");
            for (Complaint c : complaints) {
                System.out.println("ID: " + c.getComplaintId() + " | Ward: " + c.getWardNumber() +
                        " | Category: " + c.getCategory() + " | Status: " + c.getStatus() + " | Title: " + c.getTitle());
            }
        }
    }

    private void updateComplaintStatus() {
        System.out.print("Enter Complaint ID to update: ");
        if (!scanner.hasNextInt()) {
            System.out.println("❌ Invalid Complaint ID!");
            scanner.nextLine();
            return;
        }
        int complaintId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Select Status: 1. IN_PROGRESS  2. RESOLVED  3. REJECTED");
        System.out.print("Choice: ");
        if (!scanner.hasNextInt()) {
            System.out.println("❌ Invalid status!");
            scanner.nextLine();
            return;
        }
        int statusChoice = scanner.nextInt();
        scanner.nextLine();

        ComplaintStatus newStatus = switch (statusChoice) {
            case 1 -> ComplaintStatus.IN_PROGRESS;
            case 2 -> ComplaintStatus.RESOLVED;
            case 3 -> ComplaintStatus.REJECTED;
            default -> ComplaintStatus.PENDING;
        };

        if (complaintDAO.updateComplaintStatus(complaintId, newStatus)) {
            System.out.println("✅ Complaint status updated to " + newStatus);
            auditLogDAO.logAction(new AuditLog(loggedInUser.getUserId(), "UPDATE_COMPLAINT", "Updated Complaint ID " + complaintId + " status to " + newStatus));
        } else {
            System.out.println("❌ Failed to update status.");
        }
    }

    private void viewAllCertificates() {
        List<CertificateApplication> list = certificateDAO.getAllApplications();
        if (list.isEmpty()) {
            System.out.println("No certificate applications.");
        } else {
            System.out.println("\n--- ALL CERTIFICATE APPLICATIONS ---");
            for (CertificateApplication app : list) {
                System.out.println("ID: " + app.getApplicationId() + " | Citizen ID: " + app.getCitizenId() +
                        " | Type: " + app.getCertificateType() + " | Name: " + app.getApplicantName() + " | Status: " + app.getStatus());
            }
        }
    }

    private void processCertificateApplication() {
        System.out.print("Enter Application ID to process: ");
        if (!scanner.hasNextInt()) {
            System.out.println("❌ Invalid Application ID!");
            scanner.nextLine();
            return;
        }
        int appId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Select Action: 1. APPROVE  2. REJECT");
        System.out.print("Choice: ");
        int actionChoice = scanner.nextInt();
        scanner.nextLine();

        String status = (actionChoice == 1) ? "APPROVED" : "REJECTED";

        if (certificateDAO.updateApplicationStatus(appId, status)) {
            System.out.println("✅ Certificate application " + status);
            auditLogDAO.logAction(new AuditLog(loggedInUser.getUserId(), "PROCESS_CERTIFICATE", "Set Certificate App ID " + appId + " to " + status));

            if ("APPROVED".equals(status)) {
                // Fetch details and generate downloadable certificate document
                for (CertificateApplication app : certificateDAO.getAllApplications()) {
                    if (app.getApplicationId() == appId) {
                        userDAO.getUserById(app.getCitizenId()).ifPresent(citizen -> {
                            FileExporter.exportApprovedCertificate(app, citizen);
                            System.out.println("📄 Downloadable Certificate generated at exports/Certificate_" + appId + ".txt");
                        });
                        break;
                    }
                }
            }
        } else {
            System.out.println("❌ Failed to process application.");
        }
    }

    private void viewAllTaxReceipts() {
        List<TaxPayment> list = taxDAO.getAllPayments();
        if (list.isEmpty()) {
            System.out.println("No tax receipts found.");
        } else {
            System.out.println("\n--- SYSTEM TAX RECEIPTS ---");
            for (TaxPayment p : list) {
                System.out.println("Receipt ID: " + p.getPaymentId() + " | Citizen ID: " + p.getCitizenId() +
                        " | Type: " + p.getTaxType() + " | Amount: Rs. " + p.getAmount() + " | Date: " + p.getPaymentDate());
            }
        }
    }

    private void viewAuditLogs() {
        List<AuditLog> logs = auditLogDAO.getAllLogs();
        if (logs.isEmpty()) {
            System.out.println("No audit logs recorded.");
        } else {
            System.out.println("\n--- SYSTEM AUDIT TRAIL ---");
            for (AuditLog log : logs) {
                System.out.println("Log ID: " + log.getLogId() + " | User ID: " + log.getUserId() +
                        " | Action: " + log.getActionType() + " | Description: " + log.getDescription() + " | Time: " + log.getCreatedAt());
            }
        }
    }
}