package dao;

import model.AuditLog;

import java.util.List;

public interface AuditLogDAO extends MunicipalDAO {

    boolean addLog(AuditLog log);

    List<AuditLog> getAllLogs();
}