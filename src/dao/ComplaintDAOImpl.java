package dao;

import model.Complaint;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDAOImpl implements ComplaintDAO {

    @Override
    public boolean addComplaint(Complaint complaint) {
        String sql = "INSERT INTO complaints (citizen_id, citizen_name, category, description, ward_no, " +
                "house_no, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, complaint.getCitizenId());
            ps.setString(2, complaint.getCitizenName());
            ps.setString(3, complaint.getCategory());
            ps.setString(4, complaint.getDescription());
            ps.setString(5, complaint.getWardNo());
            ps.setString(6, complaint.getHouseNo());
            ps.setString(7, complaint.getStatus() == null ? "PENDING" : complaint.getStatus());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        complaint.setId(keys.getInt(1));
                    }
                }
            }
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Complaint> getComplaintsByCitizen(int citizenId) {
        List<Complaint> list = new ArrayList<>();
        String sql = "SELECT * FROM complaints WHERE citizen_id = ? ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, citizenId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Complaint> getAllComplaints() {
        List<Complaint> list = new ArrayList<>();
        String sql = "SELECT * FROM complaints ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean updateComplaintStatus(int complaintId, String status) {
        String sql = "UPDATE complaints SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, complaintId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Complaint mapRow(ResultSet rs) throws SQLException {
        Timestamp createdAt = rs.getTimestamp("created_at");
        return new Complaint(
                rs.getInt("id"),
                rs.getInt("citizen_id"),
                rs.getString("citizen_name"),
                rs.getString("category"),
                rs.getString("description"),
                rs.getString("ward_no"),
                rs.getString("house_no"),
                rs.getString("status"),
                createdAt
        );
    }
}