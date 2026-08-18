package model;

import model.enums.ComplaintCategory;
import model.enums.ComplaintStatus;
import java.time.LocalDateTime;

public class Complaint {
    private int complaintId;
    private int citizenId;
    private ComplaintCategory category;
    private String title;
    private String description;
    private int wardNumber;
    private ComplaintStatus status = ComplaintStatus.PENDING;
    private LocalDateTime createdAt;

    public Complaint() {}

    public Complaint(int citizenId, ComplaintCategory category, String title, String description, int wardNumber) {
        this.citizenId = citizenId;
        this.category = category;
        this.title = title;
        this.description = description;
        this.wardNumber = wardNumber;
    }

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

    public int getWardNumber() { return wardNumber; }
    public void setWardNumber(int wardNumber) { this.wardNumber = wardNumber; }

    public ComplaintStatus getStatus() { return status; }
    public void setStatus(ComplaintStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}