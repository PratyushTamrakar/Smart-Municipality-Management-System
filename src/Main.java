import dao.TaxDAO;
import dao.TaxDAOImpl;
import dao.UserDAO;
import dao.UserDAOImpl;
import database.DatabaseConnection;
import model.Role;
import model.TaxPayment;
import model.User;
import model.enums.TaxType;
import util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Smart Municipality Management System ===");

        UserDAO userDAO = new UserDAOImpl();
        TaxDAO taxDAO = new TaxDAOImpl();

        // 1. Ensure User & Citizen Profile exist
        String testEmail = "citizen@example.com";
        User user = userDAO.findByEmail(testEmail).orElseGet(() -> {
            User newUser = new User("Test Citizen", testEmail, PasswordUtil.hashPassword("password123"), "9800000000", Role.CITIZEN);
            userDAO.registerUser(newUser);
            return newUser;
        });

        int citizenId = ensureCitizenProfileExists(user.getUserId());

        // 2. Record Property Tax Payment
        String txnRef = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        TaxPayment propertyTax = new TaxPayment(citizenId, TaxType.PROPERTY_TAX, 2500.00, txnRef);

        if (taxDAO.recordTaxPayment(propertyTax)) {
            System.out.println("✅ Property Tax Payment Recorded! Transaction Ref: " + propertyTax.getTransactionRef());
        }

        // 3. Record Business Tax Payment
        String txnRef2 = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        TaxPayment businessTax = new TaxPayment(citizenId, TaxType.BUSINESS_TAX, 5000.00, txnRef2);
        taxDAO.recordTaxPayment(businessTax);

        // 4. Fetch revenue total
        System.out.println("\n--- Revenue Dashboard ---");
        System.out.println("💰 Total Revenue Collected: NPR " + taxDAO.getTotalRevenueCollected());
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