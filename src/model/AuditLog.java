package model;

import java.sql.Timestamp;

public class AuditLog {

    private int logId;
    private int userId;
    private String userRole;
    private String action;
    private Timestamp timestamp;

    public AuditLog() {
    }

    public AuditLog(int userId, String userRole, String action) {
        this.userId = userId;
        this.userRole = userRole;
        this.action = action;
    }

    public AuditLog(int logId, int userId, String userRole, String action, Timestamp timestamp) {
        this.logId = logId;
        this.userId = userId;
        this.userRole = userRole;
        this.action = action;
        this.timestamp = timestamp;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}