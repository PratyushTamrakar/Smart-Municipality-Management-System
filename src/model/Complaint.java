package model;

import java.sql.Timestamp;

public class Complaint {

    private int id;
    private int citizenId;
    private String citizenName;
    private String category;    // e.g. "Electricity", "Water", "Waste", "Dispute with Neighbours"
    private String description;
    private String wardNo;
    private String houseNo;
    private String status;      // PENDING, IN_PROGRESS, RESOLVED
    private Timestamp createdAt;

    public Complaint() {
    }

    public Complaint(int id, int citizenId, String citizenName, String category, String description,
                     String wardNo, String houseNo, String status, Timestamp createdAt) {
        this.id = id;
        this.citizenId = citizenId;
        this.citizenName = citizenName;
        this.category = category;
        this.description = description;
        this.wardNo = wardNo;
        this.houseNo = houseNo;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Complaint(int citizenId, String citizenName, String category, String description,
                     String wardNo, String houseNo) {
        this.citizenId = citizenId;
        this.citizenName = citizenName;
        this.category = category;
        this.description = description;
        this.wardNo = wardNo;
        this.houseNo = houseNo;
        this.status = "PENDING";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(int citizenId) {
        this.citizenId = citizenId;
    }

    public String getCitizenName() {
        return citizenName;
    }

    public void setCitizenName(String citizenName) {
        this.citizenName = citizenName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
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