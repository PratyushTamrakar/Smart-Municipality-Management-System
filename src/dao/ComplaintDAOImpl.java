package dao;

import util.DatabaseConnection;
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
        String query = "INSERT INTO complaints (citizen_id, category, title, description, ward_number, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, complaint.getCitizenId());
            stmt.setString(2, complaint.getCategory().name());
            stmt.setString(3, complaint.getTitle());
            stmt.setString(4, complaint.getDescription());
            stmt.setInt(5, complaint.getWardNumber());
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
            System.err.println("❌ Error fetching complaint: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Complaint> getComplaintsByCitizenId(int citizenId) {
        List<Complaint> complaints = new ArrayList<>();
        String query = "SELECT * FROM complaints WHERE citizen_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, citizenId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    complaints.add(mapResultSetToComplaint(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching citizen complaints: " + e.getMessage());
        }
        return complaints;
    }

    @Override
    public List<Complaint> getAllComplaints() {
        List<Complaint> complaints = new ArrayList<>();
        String query = "SELECT * FROM complaints";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                complaints.add(mapResultSetToComplaint(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching all complaints: " + e.getMessage());
        }
        return complaints;
    }

    @Override
    public boolean updateComplaintStatus(int complaintId, ComplaintStatus newStatus) {
        String query = "UPDATE complaints SET status = ? WHERE complaint_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newStatus.name());
            stmt.setInt(2, complaintId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error updating complaint status: " + e.getMessage());
        }
        return false;
    }

    private Complaint mapResultSetToComplaint(ResultSet rs) throws SQLException {
        Complaint c = new Complaint();
        c.setComplaintId(rs.getInt("complaint_id"));
        c.setCitizenId(rs.getInt("citizen_id"));
        c.setCategory(ComplaintCategory.valueOf(rs.getString("category")));
        c.setTitle(rs.getString("title"));
        c.setDescription(rs.getString("description"));
        c.setWardNumber(rs.getInt("ward_number"));
        c.setStatus(ComplaintStatus.valueOf(rs.getString("status")));

        Timestamp timestamp = rs.getTimestamp("created_at");
        if (timestamp != null) {
            c.setCreatedAt(timestamp.toLocalDateTime());
        }
        return c;
    }
}