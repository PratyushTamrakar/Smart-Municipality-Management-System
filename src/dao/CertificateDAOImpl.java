package dao;

import model.Certificate;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CertificateDAOImpl implements CertificateDAO {

    @Override
    public boolean applyCertificate(Certificate certificate) {
        String sql = "INSERT INTO certificate_applications (citizen_id, certificate_type, person_name, " +
                "relation, citizenship_number, hospital_reg_number, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, certificate.getCitizenId());
            ps.setString(2, certificate.getCertificateType());
            ps.setString(3, certificate.getPersonName());
            ps.setString(4, certificate.getRelation());
            ps.setString(5, certificate.getCitizenshipNumber());
            ps.setString(6, certificate.getHospitalRegNumber());
            ps.setString(7, certificate.getStatus() == null ? "PENDING" : certificate.getStatus());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        certificate.setApplicationId(keys.getInt(1));
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
    public List<Certificate> getApplicationsByCitizen(int citizenId) {
        List<Certificate> list = new ArrayList<>();
        String sql = "SELECT * FROM certificate_applications WHERE citizen_id = ? ORDER BY created_at DESC";
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
    public List<Certificate> getAllApplications() {
        List<Certificate> list = new ArrayList<>();
        String sql = "SELECT * FROM certificate_applications ORDER BY created_at DESC";
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
    public boolean updateCertificateStatus(int applicationId, String status) {
        String sql = "UPDATE certificate_applications SET status = ? WHERE application_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, applicationId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Certificate mapRow(ResultSet rs) throws SQLException {
        Timestamp createdAt = rs.getTimestamp("created_at");
        return new Certificate(
                rs.getInt("application_id"),
                rs.getInt("citizen_id"),
                rs.getString("certificate_type"),
                rs.getString("person_name"),
                rs.getString("relation"),
                rs.getString("citizenship_number"),
                rs.getString("hospital_reg_number"),
                rs.getString("status"),
                createdAt
        );
    }
}