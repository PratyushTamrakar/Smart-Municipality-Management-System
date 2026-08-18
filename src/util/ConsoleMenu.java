package util;

import dao.CertificateDAO;
import dao.CertificateDAOImpl;
import dao.ComplaintDAO;
import dao.ComplaintDAOImpl;
import dao.TaxDAO;
import dao.TaxDAOImpl;
import dao.UserDAO;
import dao.UserDAOImpl;
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
        System.out.println("5. Pay Municipal Tax");
        System.out.println("6. View Tax Payment History");
        System.out.println("0. Logout");
        System.out.print("Select choice: ");

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
        System.out.println("4. Process Certificate Application");
        System.out.println("5. View All System Tax Receipts");
        System.out.println("0. Logout");
        System.out.print("Select choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> viewAllComplaints();
            case 2 -> updateComplaintStatus();
            case 3 -> viewAllCertificates();
            case 4 -> processCertificateApplication();
            case 5 -> viewAllTaxReceipts();
            case 0 -> {
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
        int wardNumber = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Category: 1. ROAD  2. WATER  3. WASTE  4. ELECTRICITY");
        System.out.print("Select category: ");
        int catChoice = scanner.nextInt();
        scanner.nextLine();

        ComplaintCategory category = switch (catChoice) {
            case 1 -> ComplaintCategory.ROAD;
            case 2 -> ComplaintCategory.WATER;
            case 3 -> ComplaintCategory.WASTE;
            case 4 -> ComplaintCategory.ELECTRICITY;
            default -> ComplaintCategory.ROAD;
        };

        Complaint complaint = new Complaint(
                loggedInUser.getUserId(),
                category,
                title,
                description,
                wardNumber
        );

        if (complaintDAO.createComplaint(complaint)) {
            System.out.println("✅ Complaint submitted successfully! Complaint ID: " + complaint.getComplaintId());
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

        CertificateApplication app = new CertificateApplication(
                loggedInUser.getUserId(),
                certType,
                applicantName,
                details
        );

        if (certificateDAO.applyForCertificate(app)) {
            System.out.println("✅ Certificate application submitted successfully! Application ID: " + app.getApplicationId());
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
        System.out.println("\nSelect Tax Type:");
        System.out.println("1. Property Tax");
        System.out.println("2. Business Tax");
        System.out.println("3. Vehicle Tax");
        System.out.print("Choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        String taxType = switch (choice) {
            case 1 -> "PROPERTY_TAX";
            case 2 -> "BUSINESS_TAX";
            case 3 -> "VEHICLE_TAX";
            default -> "PROPERTY_TAX";
        };

        System.out.print("Enter Tax Amount to Pay: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        TaxPayment payment = new TaxPayment(loggedInUser.getUserId(), taxType, amount);

        if (taxDAO.payTax(payment)) {
            System.out.println("✅ Tax payment successful! Receipt / Payment ID: " + payment.getPaymentId());
        } else {
            System.out.println("❌ Failed to process tax payment.");
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
                        " | Amount: Rs. " + p.getAmount() + " | Status: " + p.getPaymentStatus() +
                        " | Date: " + p.getPaymentDate());
            }
        }
    }

    private void viewAllComplaints() {
        List<Complaint> complaints = complaintDAO.getAllComplaints();
        if (complaints.isEmpty()) {
            System.out.println("No complaints registered in system.");
        } else {
            System.out.println("\n--- ALL COMPLAINTS SYSTEM-WIDE ---");
            for (Complaint c : complaints) {
                System.out.println("ID: " + c.getComplaintId() + " | Citizen ID: " + c.getCitizenId() +
                        " | Ward: " + c.getWardNumber() + " | Category: " + c.getCategory() +
                        " | Status: " + c.getStatus() + " | Title: " + c.getTitle());
            }
        }
    }

    private void updateComplaintStatus() {
        System.out.print("Enter Complaint ID to update: ");
        if (!scanner.hasNextInt()) {
            System.out.println("❌ Invalid input! Please enter a numeric Complaint ID (e.g., 1).");
            scanner.nextLine();
            return;
        }
        int complaintId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Select New Status: 1. IN_PROGRESS  2. RESOLVED  3. REJECTED");
        System.out.print("Choice: ");
        if (!scanner.hasNextInt()) {
            System.out.println("❌ Invalid status choice!");
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
            System.out.println("✅ Complaint status updated successfully to " + newStatus);
        } else {
            System.out.println("❌ Failed to update status. Please check Complaint ID.");
        }
    }

    private void viewAllCertificates() {
        List<CertificateApplication> list = certificateDAO.getAllApplications();
        if (list.isEmpty()) {
            System.out.println("No certificate applications in the system.");
        } else {
            System.out.println("\n--- ALL CERTIFICATE APPLICATIONS ---");
            for (CertificateApplication app : list) {
                System.out.println("ID: " + app.getApplicationId() + " | Citizen ID: " + app.getCitizenId() +
                        " | Type: " + app.getCertificateType() + " | Name: " + app.getApplicantName() +
                        " | Status: " + app.getStatus());
            }
        }
    }

    private void processCertificateApplication() {
        System.out.print("Enter Application ID to process: ");
        int appId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Select Action: 1. APPROVE  2. REJECT");
        System.out.print("Choice: ");
        int actionChoice = scanner.nextInt();
        scanner.nextLine();

        String status = (actionChoice == 1) ? "APPROVED" : "REJECTED";

        if (certificateDAO.updateApplicationStatus(appId, status)) {
            System.out.println("✅ Certificate application updated to " + status);
        } else {
            System.out.println("❌ Failed to update application status.");
        }
    }

    private void viewAllTaxReceipts() {
        List<TaxPayment> list = taxDAO.getAllPayments();
        if (list.isEmpty()) {
            System.out.println("No tax receipts recorded in the system.");
        } else {
            System.out.println("\n--- ALL SYSTEM-WIDE TAX RECEIPTS ---");
            for (TaxPayment p : list) {
                System.out.println("Receipt ID: " + p.getPaymentId() + " | Citizen ID: " + p.getCitizenId() +
                        " | Type: " + p.getTaxType() + " | Amount: Rs. " + p.getAmount() +
                        " | Status: " + p.getPaymentStatus() + " | Date: " + p.getPaymentDate());
            }
        }
    }
}