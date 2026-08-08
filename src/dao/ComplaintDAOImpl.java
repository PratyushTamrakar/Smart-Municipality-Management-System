package dao;

import database.DatabaseConnection;
import model.Complaint;
import model.enums.ComplaintCategory;
import model.enums.ComplaintStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComplaintDAOImpl implements ComplaintDAO {

    @Override
    public boolean createComplaint(Complaint complaint) {
        String query = "INSERT INTO complaints (citizen_id, category, title, description, location_ward, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, complaint.getCitizenId());
            stmt.setString(2, complaint.getCategory().name());
            stmt.setString(3, complaint.getTitle());
            stmt.setString(4, complaint.getDescription());
            stmt.setInt(5, complaint.getLocationWard());
            stmt.setString(6, complaint.getStatus().name());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        complaint.setComplaintId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error creating complaint: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<Complaint> getComplaintById(int complaintId) {
        String query = "SELECT * FROM complaints WHERE complaint_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, complaintId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToComplaint(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching complaint by ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Complaint> getComplaintsByCitizenId(int citizenId) {
        List<Complaint> list = new ArrayList<>();
        String query = "SELECT * FROM complaints WHERE citizen_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, citizenId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToComplaint(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching citizen complaints: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Complaint> getAllComplaints() {
        List<Complaint> list = new ArrayList<>();
        String query = "SELECT * FROM complaints";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(mapResultSetToComplaint(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching all complaints: " + e.getMessage());
        }
        return list;
    }

    @Override
    public boolean updateComplaintStatus(int complaintId, ComplaintStatus status) {
        String query = "UPDATE complaints SET status = ? WHERE complaint_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status.name());
            stmt.setInt(2, complaintId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error updating complaint status: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean assignComplaintToEmployee(int complaintId, int employeeId) {
        String query = "UPDATE complaints SET assigned_employee_id = ?, status = ? WHERE complaint_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, employeeId);
            stmt.setString(2, ComplaintStatus.ASSIGNED.name());
            stmt.setInt(3, complaintId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error assigning complaint: " + e.getMessage());
        }
        return false;
    }

    private Complaint mapResultSetToComplaint(ResultSet rs) throws SQLException {
        Integer empId = rs.getInt("assigned_employee_id");
        if (rs.wasNull()) empId = null;

        return new Complaint(
                rs.getInt("complaint_id"),
                rs.getInt("citizen_id"),
                ComplaintCategory.valueOf(rs.getString("category")),
                rs.getString("title"),
                rs.getString("description"),
                rs.getInt("location_ward"),
                ComplaintStatus.valueOf(rs.getString("status")),
                empId,
                rs.getTimestamp("created_at")
        );
    }
}