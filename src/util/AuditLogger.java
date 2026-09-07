package util;

import dao.AuditLogDAO;
import dao.AuditLogDAOImpl;
import model.AuditLog;
import model.User;

public class AuditLogger {

    private static final AuditLogDAO auditLogDAO = new AuditLogDAOImpl();

    private AuditLogger() {
    }

    public static void log(User user, String action) {
        if (user == null) {
            log(0, "SYSTEM", action);
            return;
        }
        log(user.getId(), user.getRole(), action);
    }

    public static void log(int userId, String userRole, String action) {
        try {
            auditLogDAO.addLog(new AuditLog(userId, userRole, action));
        } catch (Exception e) {
            System.err.println("Failed to write audit log: " + e.getMessage());
        }
    }
}