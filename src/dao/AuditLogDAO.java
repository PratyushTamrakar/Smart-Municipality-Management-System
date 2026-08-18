package dao;

import model.AuditLog;
import java.util.List;

public interface AuditLogDAO {
    boolean logAction(AuditLog log);
    List<AuditLog> getAllLogs();
}