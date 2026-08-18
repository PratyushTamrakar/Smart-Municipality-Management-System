package model;

import model.enums.ComplaintStatus; // Reuse PENDING / APPROVED / REJECTED or create a new enum

import java.sql.Timestamp;

public class CertificateApplication {
    private int applicationId;
    private int citizenId;
    private String certificateType;
    private String applicantName;
    private String details;
    private String status; // PENDING, APPROVED, REJECTED
    private Timestamp appliedAt;

    public CertificateApplication() {}

    public CertificateApplication(int citizenId, String certificateType, String applicantName, String details) {
        this.citizenId = citizenId;
        this.certificateType = certificateType;
        this.applicantName = applicantName;
        this.details = details;
        this.status = "PENDING";
    }

    // Getters and Setters
    public int getApplicationId() { return applicationId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }

    public int getCitizenId() { return citizenId; }
    public void setCitizenId(int citizenId) { this.citizenId = citizenId; }

    public String getCertificateType() { return certificateType; }
    public void setCertificateType(String certificateType) { this.certificateType = certificateType; }

    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getAppliedAt() { return appliedAt; }
    public void setAppliedAt(Timestamp appliedAt) { this.appliedAt = appliedAt; }
}