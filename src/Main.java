import dao.CertificateDAO;
import dao.CertificateDAOImpl;
import dao.UserDAO;
import dao.UserDAOImpl;
import database.DatabaseConnection;
import model.CertificateApplication;
import model.Role;
import model.User;
import model.enums.CertificateType;
import model.enums.RequestStatus;
import util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Smart Municipality Management System ===");

        UserDAO userDAO = new UserDAOImpl();
        CertificateDAO certificateDAO = new CertificateDAOImpl();

        // 1. Ensure User & Citizen Profile exist
        String testEmail = "citizen@example.com";
        User user = userDAO.findByEmail(testEmail).orElseGet(() -> {
            User newUser = new User("Test Citizen", testEmail, PasswordUtil.hashPassword("password123"), "9800000000", Role.CITIZEN);
            userDAO.registerUser(newUser);
            return newUser;
        });

        int citizenId = ensureCitizenProfileExists(user.getUserId());

        // 2. Submit Certificate Application
        CertificateApplication app = new CertificateApplication(
                citizenId,
                CertificateType.RESIDENCE,
                "Requesting official residence certificate for ward 4 household registration."
        );

        boolean applied = certificateDAO.applyForCertificate(app);
        if (applied) {
            System.out.println("✅ Certificate Application Submitted! Application ID: " + app.getApplicationId());
        }

        // 3. List all certificate applications
        System.out.println("\n--- All Certificate Applications ---");
        certificateDAO.getAllApplications().forEach(a ->
                System.out.println("App #" + a.getApplicationId() + " [" + a.getCertificateType() + "] - Status: " + a.getStatus() + " | Details: " + a.getDetails())
        );
    }

    private static int ensureCitizenProfileExists(int userId) {
        String selectQuery = "SELECT citizen_id FROM citizens WHERE user_id = ?";
        String insertQuery = "INSERT INTO citizens (user_id, citizenship_number, ward_number, address) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) return rs.getInt("citizen_id");
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, userId);
                stmt.setString(2, "CIT-100" + userId);
                stmt.setInt(3, 4);
                stmt.setString(4, "Kathmandu Ward 4");
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error handling citizen profile: " + e.getMessage());
        }
        return 1;
    }
}