package dao;

import database.DatabaseConnection;
import model.TaxPayment;
import model.enums.PaymentStatus;
import model.enums.TaxType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaxDAOImpl implements TaxDAO {

    @Override
    public boolean recordTaxPayment(TaxPayment payment) {
        String query = "INSERT INTO tax_payments (citizen_id, tax_type, amount, transaction_ref, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, payment.getCitizenId());
            stmt.setString(2, payment.getTaxType().name());
            stmt.setDouble(3, payment.getAmount());
            stmt.setString(4, payment.getTransactionRef());
            stmt.setString(5, payment.getStatus().name());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        payment.setPaymentId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error recording tax payment: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<TaxPayment> getPaymentById(int paymentId) {
        String query = "SELECT * FROM tax_payments WHERE payment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, paymentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToTaxPayment(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching payment by ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<TaxPayment> getPaymentsByCitizenId(int citizenId) {
        List<TaxPayment> list = new ArrayList<>();
        String query = "SELECT * FROM tax_payments WHERE citizen_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, citizenId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToTaxPayment(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching citizen tax payments: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<TaxPayment> getAllPayments() {
        List<TaxPayment> list = new ArrayList<>();
        String query = "SELECT * FROM tax_payments";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(mapResultSetToTaxPayment(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching all tax payments: " + e.getMessage());
        }
        return list;
    }

    @Override
    public double getTotalRevenueCollected() {
        String query = "SELECT SUM(amount) AS total FROM tax_payments WHERE status = 'COMPLETED'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error calculating total revenue: " + e.getMessage());
        }
        return 0.0;
    }

    private TaxPayment mapResultSetToTaxPayment(ResultSet rs) throws SQLException {
        return new TaxPayment(
                rs.getInt("payment_id"),
                rs.getInt("citizen_id"),
                TaxType.valueOf(rs.getString("tax_type")),
                rs.getDouble("amount"),
                rs.getString("transaction_ref"),
                PaymentStatus.valueOf(rs.getString("status")),
                rs.getTimestamp("paid_at")
        );
    }
}