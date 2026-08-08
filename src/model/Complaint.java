package model;

import model.enums.ComplaintCategory;
import model.enums.ComplaintStatus;
import java.sql.Timestamp;

public class Complaint {
    private int complaintId;
    private int citizenId;
    private ComplaintCategory category;
    private String title;
    private String description;
    private int locationWard;
    private ComplaintStatus status;
    private Integer assignedEmployeeId; // Nullable if not assigned yet
    private Timestamp createdAt;

    // Default Constructor
    public Complaint() {}

    // Constructor for creating new complaint
    public Complaint(int citizenId, ComplaintCategory category, String title, String description, int locationWard) {
        this.citizenId = citizenId;
        this.category = category;
        this.title = title;
        this.description = description;
        this.locationWard = locationWard;
        this.status = ComplaintStatus.PENDING;
    }

    // Full Constructor
    public Complaint(int complaintId, int citizenId, ComplaintCategory category, String title,
                     String description, int locationWard, ComplaintStatus status,
                     Integer assignedEmployeeId, Timestamp createdAt) {
        this.complaintId = complaintId;
        this.citizenId = citizenId;
        this.category = category;
        this.title = title;
        this.description = description;
        this.locationWard = locationWard;
        this.status = status;
        this.assignedEmployeeId = assignedEmployeeId;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getComplaintId() { return complaintId; }
    public void setComplaintId(int complaintId) { this.complaintId = complaintId; }

    public int getCitizenId() { return citizenId; }
    public void setCitizenId(int citizenId) { this.citizenId = citizenId; }

    public ComplaintCategory getCategory() { return category; }
    public void setCategory(ComplaintCategory category) { this.category = category; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getLocationWard() { return locationWard; }
    public void setLocationWard(int locationWard) { this.locationWard = locationWard; }

    public ComplaintStatus getStatus() { return status; }
    public void setStatus(ComplaintStatus status) { this.status = status; }

    public Integer getAssignedEmployeeId() { return assignedEmployeeId; }
    public void setAssignedEmployeeId(Integer assignedEmployeeId) { this.assignedEmployeeId = assignedEmployeeId; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}