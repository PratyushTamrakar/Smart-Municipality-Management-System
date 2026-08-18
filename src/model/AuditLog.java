package model;

import java.sql.Timestamp;

public class AuditLog {
    private int logId;
    private int userId;
    private String actionType;
    private String description;
    private Timestamp createdAt;

    public AuditLog() {}

    public AuditLog(int userId, String actionType, String description) {
        this.userId = userId;
        this.actionType = actionType;
        this.description = description;
    }

    // Getters and Setters
    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}