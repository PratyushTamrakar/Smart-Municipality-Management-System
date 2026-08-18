package model;

import java.sql.Timestamp;

public class TaxPayment {
    private int paymentId;
    private int citizenId;
    private String taxType;
    private double amount;
    private String paymentStatus;
    private Timestamp paymentDate;

    public TaxPayment() {}

    public TaxPayment(int citizenId, String taxType, double amount) {
        this.citizenId = citizenId;
        this.taxType = taxType;
        this.amount = amount;
        this.paymentStatus = "PAID";
    }

    // Getters and Setters
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getCitizenId() { return citizenId; }
    public void setCitizenId(int citizenId) { this.citizenId = citizenId; }

    public String getTaxType() { return taxType; }
    public void setTaxType(String taxType) { this.taxType = taxType; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public Timestamp getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Timestamp paymentDate) { this.paymentDate = paymentDate; }
}