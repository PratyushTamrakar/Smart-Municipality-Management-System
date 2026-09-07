package dao;

import model.Payment;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class TaxDAOImpl implements TaxDAO {

    @Override
    public boolean makePayment(Payment payment) {
        String sql = "INSERT INTO payments (citizen_id, citizen_name, payment_type, details, ward_no, " +
                "house_no, months, vehicle_cc, business_type, business_tier, property_value, amount, " +
                "gateway, payment_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, payment.getCitizenId());
            ps.setString(2, payment.getCitizenName());
            ps.setString(3, payment.getPaymentType());
            ps.setString(4, payment.getDetails());
            ps.setString(5, payment.getWardNo());
            ps.setString(6, payment.getHouseNo());

            if (payment.getMonths() != null) {
                ps.setInt(7, payment.getMonths());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            if (payment.getVehicleCC() != null) {
                ps.setInt(8, payment.getVehicleCC());
            } else {
                ps.setNull(8, Types.INTEGER);
            }

            ps.setString(9, payment.getBusinessType());
            ps.setString(10, payment.getBusinessTier());

            if (payment.getPropertyValue() != null) {
                ps.setDouble(11, payment.getPropertyValue());
            } else {
                ps.setNull(11, Types.DOUBLE);
            }

            ps.setDouble(12, payment.getAmount());
            ps.setString(13, payment.getGateway());
            ps.setString(14, payment.getPaymentStatus() == null ? "PAID" : payment.getPaymentStatus());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        payment.setPaymentId(keys.getInt(1));
                    }
                }
                try (PreparedStatement fetch = conn.prepareStatement(
                        "SELECT payment_date FROM payments WHERE payment_id = ?")) {
                    fetch.setInt(1, payment.getPaymentId());
                    try (ResultSet rs = fetch.executeQuery()) {
                        if (rs.next()) {
                            payment.setPaymentDate(rs.getTimestamp("payment_date"));
                        }
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
    public List<Payment> getPaymentsByCitizen(int citizenId) {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE citizen_id = ? ORDER BY payment_date DESC";
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
    public List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payments ORDER BY payment_date DESC";
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

    private Payment mapRow(ResultSet rs) throws SQLException {
        return new Payment(
                rs.getInt("payment_id"),
                rs.getInt("citizen_id"),
                rs.getString("citizen_name"),
                rs.getString("payment_type"),
                rs.getString("details"),
                rs.getString("ward_no"),
                rs.getString("house_no"),
                rs.getObject("months", Integer.class),
                rs.getObject("vehicle_cc", Integer.class),
                rs.getString("business_type"),
                rs.getString("business_tier"),
                rs.getObject("property_value", Double.class),
                rs.getDouble("amount"),
                rs.getString("gateway"),
                rs.getString("payment_status"),
                rs.getTimestamp("payment_date")
        );
    }
}