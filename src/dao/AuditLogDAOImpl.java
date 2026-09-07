package dao;

import model.AuditLog;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuditLogDAOImpl implements AuditLogDAO {

    @Override
    public boolean addLog(AuditLog log) {
        String sql = "INSERT INTO audit_logs (user_id, user_role, action) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, log.getUserId());
            ps.setString(2, log.getUserRole());
            ps.setString(3, log.getAction());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<AuditLog> getAllLogs() {
        List<AuditLog> list = new ArrayList<>();
        String sql = "SELECT * FROM audit_logs ORDER BY timestamp DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new AuditLog(
                        rs.getInt("log_id"),
                        rs.getInt("user_id"),
                        rs.getString("user_role"),
                        rs.getString("action"),
                        rs.getTimestamp("timestamp")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}