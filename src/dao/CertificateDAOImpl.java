package dao;

import model.CertificateApplication;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CertificateDAOImpl implements CertificateDAO {

    @Override
    public boolean applyForCertificate(CertificateApplication app) {
        String sql = "INSERT INTO certificate_applications (citizen_id, certificate_type, applicant_name, details, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, app.getCitizenId());
            stmt.setString(2, app.getCertificateType());
            stmt.setString(3, app.getApplicantName());
            stmt.setString(4, app.getDetails());
            stmt.setString(5, app.getStatus());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        app.setApplicationId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<CertificateApplication> getApplicationsByCitizenId(int citizenId) {
        List<CertificateApplication> list = new ArrayList<>();
        String sql = "SELECT * FROM certificate_applications WHERE citizen_id = ? ORDER BY applied_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, citizenId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToApplication(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<CertificateApplication> getAllApplications() {
        List<CertificateApplication> list = new ArrayList<>();
        String sql = "SELECT * FROM certificate_applications ORDER BY applied_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapResultSetToApplication(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean updateApplicationStatus(int applicationId, String status) {
        String sql = "UPDATE certificate_applications SET status = ? WHERE application_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, applicationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private CertificateApplication mapResultSetToApplication(ResultSet rs) throws SQLException {
        CertificateApplication app = new CertificateApplication();
        app.setApplicationId(rs.getInt("application_id"));
        app.setCitizenId(rs.getInt("citizen_id"));
        app.setCertificateType(rs.getString("certificate_type"));
        app.setApplicantName(rs.getString("applicant_name"));
        app.setDetails(rs.getString("details"));
        app.setStatus(rs.getString("status"));
        app.setAppliedAt(rs.getTimestamp("applied_at"));
        return app;
    }
}