package dao;

import model.TaxPayment;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaxDAOImpl implements TaxDAO {

    @Override
    public boolean payTax(TaxPayment payment) {
        String sql = "INSERT INTO tax_payments (citizen_id, tax_type, amount, payment_status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, payment.getCitizenId());
            stmt.setString(2, payment.getTaxType());
            stmt.setDouble(3, payment.getAmount());
            stmt.setString(4, payment.getPaymentStatus());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        payment.setPaymentId(rs.getInt(1));
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
    public List<TaxPayment> getPaymentsByCitizenId(int citizenId) {
        List<TaxPayment> list = new ArrayList<>();
        String sql = "SELECT * FROM tax_payments WHERE citizen_id = ? ORDER BY payment_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, citizenId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToTaxPayment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<TaxPayment> getAllPayments() {
        List<TaxPayment> list = new ArrayList<>();
        String sql = "SELECT * FROM tax_payments ORDER BY payment_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapResultSetToTaxPayment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private TaxPayment mapResultSetToTaxPayment(ResultSet rs) throws SQLException {
        TaxPayment payment = new TaxPayment();
        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setCitizenId(rs.getInt("citizen_id"));
        payment.setTaxType(rs.getString("tax_type"));
        payment.setAmount(rs.getDouble("amount"));
        payment.setPaymentStatus(rs.getString("payment_status"));
        payment.setPaymentDate(rs.getTimestamp("payment_date"));
        return payment;
    }
}