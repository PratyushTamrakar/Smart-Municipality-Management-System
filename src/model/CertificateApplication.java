package model;

import model.enums.CertificateType;
import model.enums.RequestStatus;
import java.sql.Timestamp;

public class CertificateApplication {
    private int applicationId;
    private int citizenId;
    private CertificateType certificateType;
    private String details;
    private RequestStatus status;
    private Timestamp appliedDate;
    private Integer reviewedByOfficerId; // Nullable until an officer reviews it

    // Default Constructor
    public CertificateApplication() {}

    // Constructor for submitting a new application
    public CertificateApplication(int citizenId, CertificateType certificateType, String details) {
        this.citizenId = citizenId;
        this.certificateType = certificateType;
        this.details = details;
        this.status = RequestStatus.SUBMITTED;
    }

    // Full Constructor
    public CertificateApplication(int applicationId, int citizenId, CertificateType certificateType,
                                  String details, RequestStatus status, Timestamp appliedDate,
                                  Integer reviewedByOfficerId) {
        this.applicationId = applicationId;
        this.citizenId = citizenId;
        this.certificateType = certificateType;
        this.details = details;
        this.status = status;
        this.appliedDate = appliedDate;
        this.reviewedByOfficerId = reviewedByOfficerId;
    }

    // Getters and Setters
    public int getApplicationId() { return applicationId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }

    public int getCitizenId() { return citizenId; }
    public void setCitizenId(int citizenId) { this.citizenId = citizenId; }

    public CertificateType getCertificateType() { return certificateType; }
    public void setCertificateType(CertificateType certificateType) { this.certificateType = certificateType; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public Timestamp getAppliedDate() { return appliedDate; }
    public void setAppliedDate(Timestamp appliedDate) { this.appliedDate = appliedDate; }

    public Integer getReviewedByOfficerId() { return reviewedByOfficerId; }
    public void setReviewedByOfficerId(Integer reviewedByOfficerId) { this.reviewedByOfficerId = reviewedByOfficerId; }
}