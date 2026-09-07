package model;

import java.sql.Timestamp;

public class Payment {

    private int paymentId;
    private int citizenId;
    private String citizenName;
    private String paymentType;     // "Waste Management Fee", "Vehicle Tax", "Business Tax", "Property Tax"
    private String details;         // human-readable summary, used on the receipt
    private String wardNo;
    private String houseNo;
    private Integer months;         // Waste Management Fee only
    private Integer vehicleCC;      // Vehicle Tax only
    private String businessType;    // Business Tax only
    private String businessTier;    // Business Tax only
    private Double propertyValue;   // Property Tax only
    private double amount;
    private String gateway;         // "eSewa", "Khalti", "Connect IPS"
    private String paymentStatus;   // "PAID"
    private Timestamp paymentDate;

    public Payment() {
        this.paymentStatus = "PAID";
    }

    public Payment(int paymentId, int citizenId, String citizenName, String paymentType, String details,
                   String wardNo, String houseNo, Integer months, Integer vehicleCC, String businessType,
                   String businessTier, Double propertyValue, double amount, String gateway,
                   String paymentStatus, Timestamp paymentDate) {
        this.paymentId = paymentId;
        this.citizenId = citizenId;
        this.citizenName = citizenName;
        this.paymentType = paymentType;
        this.details = details;
        this.wardNo = wardNo;
        this.houseNo = houseNo;
        this.months = months;
        this.vehicleCC = vehicleCC;
        this.businessType = businessType;
        this.businessTier = businessTier;
        this.propertyValue = propertyValue;
        this.amount = amount;
        this.gateway = gateway;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
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

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

    public Integer getMonths() {
        return months;
    }

    public void setMonths(Integer months) {
        this.months = months;
    }

    public Integer getVehicleCC() {
        return vehicleCC;
    }

    public void setVehicleCC(Integer vehicleCC) {
        this.vehicleCC = vehicleCC;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessTier() {
        return businessTier;
    }

    public void setBusinessTier(String businessTier) {
        this.businessTier = businessTier;
    }

    public Double getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Double propertyValue) {
        this.propertyValue = propertyValue;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }
}