package dao;

import model.AuditLog;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditLogDAOImpl implements AuditLogDAO {

    @Override
    public boolean logAction(AuditLog log) {
        String sql = "INSERT INTO audit_logs (user_id, action_type, description) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, log.getUserId());
            stmt.setString(2, log.getActionType());
            stmt.setString(3, log.getDescription());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<AuditLog> getAllLogs() {
        List<AuditLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM audit_logs ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                AuditLog log = new AuditLog();
                log.setLogId(rs.getInt("log_id"));
                log.setUserId(rs.getInt("user_id"));
                log.setActionType(rs.getString("action_type"));
                log.setDescription(rs.getString("description"));
                log.setCreatedAt(rs.getTimestamp("created_at"));
                logs.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }
}