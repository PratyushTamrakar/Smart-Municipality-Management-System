import dao.ComplaintDAO;
import dao.ComplaintDAOImpl;
import dao.UserDAO;
import dao.UserDAOImpl;
import database.DatabaseConnection;
import model.Complaint;
import model.Role;
import model.User;
import model.enums.ComplaintCategory;
import model.enums.ComplaintStatus;
import util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Smart Municipality Management System ===");

        UserDAO userDAO = new UserDAOImpl();
        ComplaintDAO complaintDAO = new ComplaintDAOImpl();

        // 1. Ensure User exists
        String testEmail = "citizen@example.com";
        User user = userDAO.findByEmail(testEmail).orElseGet(() -> {
            User newUser = new User("Test Citizen", testEmail, PasswordUtil.hashPassword("password123"), "9800000000", Role.CITIZEN);
            userDAO.registerUser(newUser);
            System.out.println("✅ Registered new User ID: " + newUser.getUserId());
            return newUser;
        });

        // 2. Ensure Citizen Profile exists in 'citizens' table
        int citizenId = ensureCitizenProfileExists(user.getUserId());

        // 3. File a new complaint using valid citizenId
        Complaint newComplaint = new Complaint(
                citizenId,
                ComplaintCategory.ROAD,
                "Potholes on Main Street",
                "Large potholes near Ward 4 office causing severe traffic delay.",
                4
        );

        boolean created = complaintDAO.createComplaint(newComplaint);
        if (created) {
            System.out.println("✅ Complaint Filed Successfully! Ticket ID: " + newComplaint.getComplaintId());
        }

        // 4. Fetch and display all complaints
        System.out.println("\n--- All System Complaints ---");
        complaintDAO.getAllComplaints().forEach(c ->
                System.out.println("Ticket #" + c.getComplaintId() + " [" + c.getCategory() + "] - " + c.getTitle() + " | Status: " + c.getStatus())
        );

        // 5. Update status to IN_PROGRESS
        if (newComplaint.getComplaintId() > 0) {
            boolean updated = complaintDAO.updateComplaintStatus(newComplaint.getComplaintId(), ComplaintStatus.IN_PROGRESS);
            if (updated) {
                System.out.println("\n✅ Ticket #" + newComplaint.getComplaintId() + " updated to IN_PROGRESS");
            }
        }
    }

    // Helper method to insert citizen record if missing
    private static int ensureCitizenProfileExists(int userId) {
        String selectQuery = "SELECT citizen_id FROM citizens WHERE user_id = ?";
        String insertQuery = "INSERT INTO citizens (user_id, citizenship_number, ward_number, address) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if already exists
            try (PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("citizen_id");
                    }
                }
            }

            // Insert new citizen record
            try (PreparedStatement stmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, userId);
                stmt.setString(2, "CIT-100" + userId);
                stmt.setInt(3, 4);
                stmt.setString(4, "Kathmandu Ward 4");
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int cId = rs.getInt(1);
                        System.out.println("✅ Created Citizen Profile ID: " + cId);
                        return cId;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error handling citizen profile: " + e.getMessage());
        }
        return 1;
    }
}