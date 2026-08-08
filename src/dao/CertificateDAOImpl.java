package dao;

import database.DatabaseConnection;
import model.CertificateApplication;
import model.enums.CertificateType;
import model.enums.RequestStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CertificateDAOImpl implements CertificateDAO {

    @Override
    public boolean applyForCertificate(CertificateApplication application) {
        String query = "INSERT INTO certificate_applications (citizen_id, certificate_type, details, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, application.getCitizenId());
            stmt.setString(2, application.getCertificateType().name());
            stmt.setString(3, application.getDetails());
            stmt.setString(4, application.getStatus().name());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        application.setApplicationId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error submitting certificate application: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<CertificateApplication> getApplicationById(int applicationId) {
        String query = "SELECT * FROM certificate_applications WHERE application_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, applicationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToApplication(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching application by ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<CertificateApplication> getApplicationsByCitizenId(int citizenId) {
        List<CertificateApplication> list = new ArrayList<>();
        String query = "SELECT * FROM certificate_applications WHERE citizen_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, citizenId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToApplication(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching citizen applications: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<CertificateApplication> getAllApplications() {
        List<CertificateApplication> list = new ArrayList<>();
        String query = "SELECT * FROM certificate_applications";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(mapResultSetToApplication(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching all certificate applications: " + e.getMessage());
        }
        return list;
    }

    @Override
    public boolean updateApplicationStatus(int applicationId, RequestStatus status, int officerId) {
        String query = "UPDATE certificate_applications SET status = ?, reviewed_by_officer_id = ? WHERE application_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status.name());
            stmt.setInt(2, officerId);
            stmt.setInt(3, applicationId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error updating application status: " + e.getMessage());
        }
        return false;
    }

    private CertificateApplication mapResultSetToApplication(ResultSet rs) throws SQLException {
        Integer officerId = rs.getInt("reviewed_by_officer_id");
        if (rs.wasNull()) officerId = null;

        return new CertificateApplication(
                rs.getInt("application_id"),
                rs.getInt("citizen_id"),
                CertificateType.valueOf(rs.getString("certificate_type")),
                rs.getString("details"),
                RequestStatus.valueOf(rs.getString("status")),
                rs.getTimestamp("applied_date"),
                officerId
        );
    }
}