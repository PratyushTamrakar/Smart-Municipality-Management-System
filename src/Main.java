import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Smart Municipality Management System ===");

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Successfully connected to MySQL Database!");
            }
        } catch (SQLException e) {
            System.err.println("❌ Database Connection Failed: " + e.getMessage());
            System.err.println("Make sure MySQL Server is running and 'smart_municipality_db' exists.");
        }
    }
}