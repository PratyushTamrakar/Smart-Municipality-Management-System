package dao;

import database.DatabaseConnection;
import model.Role;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {

    @Override
    public boolean registerUser(User user) {
        String query = "INSERT INTO users (full_name, email, password_hash, phone_number, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHash());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setString(5, user.getRole().name());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setUserId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error registering user: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error finding user by email: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(int userId) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error finding user by ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error retrieving all users: " + e.getMessage());
        }
        return users;
    }

    @Override
    public boolean updateUser(User user) {
        String query = "UPDATE users SET full_name = ?, phone_number = ?, role = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getPhoneNumber());
            stmt.setString(3, user.getRole().name());
            stmt.setInt(4, user.getUserId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error updating user: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteUser(int userId) {
        String query = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error deleting user: " + e.getMessage());
        }
        return false;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("phone_number"),
                Role.valueOf(rs.getString("role")),
                rs.getTimestamp("created_at")
        );
    }
}