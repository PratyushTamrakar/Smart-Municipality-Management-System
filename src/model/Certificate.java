package model;

import java.sql.Timestamp;

public class Certificate {

    private int applicationId;
    private int citizenId;
    private String certificateType;     // "Birth Certificate" or "Death Certificate"
    private String personName;
    private String relation;            // relation of the applicant to the person named
    private String citizenshipNumber;   // required for Death applications
    private String hospitalRegNumber;   // required for Birth applications
    private String status;              // PENDING, APPROVED, REJECTED
    private Timestamp createdAt;

    public Certificate() {
        this.status = "PENDING";
    }

    public Certificate(int applicationId, int citizenId, String certificateType, String personName,
                       String relation, String citizenshipNumber, String hospitalRegNumber,
                       String status, Timestamp createdAt) {
        this.applicationId = applicationId;
        this.citizenId = citizenId;
        this.certificateType = certificateType;
        this.personName = personName;
        this.relation = relation;
        this.citizenshipNumber = citizenshipNumber;
        this.hospitalRegNumber = hospitalRegNumber;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(int citizenId) {
        this.citizenId = citizenId;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getCitizenshipNumber() {
        return citizenshipNumber;
    }

    public void setCitizenshipNumber(String citizenshipNumber) {
        this.citizenshipNumber = citizenshipNumber;
    }

    public String getHospitalRegNumber() {
        return hospitalRegNumber;
    }

    public void setHospitalRegNumber(String hospitalRegNumber) {
        this.hospitalRegNumber = hospitalRegNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}