package model;

import model.enums.PaymentStatus;
import model.enums.TaxType;
import java.sql.Timestamp;

public class TaxPayment {
    private int paymentId;
    private int citizenId;
    private TaxType taxType;
    private double amount;
    private String transactionRef;
    private PaymentStatus status;
    private Timestamp paidAt;

    public TaxPayment() {}

    public TaxPayment(int citizenId, TaxType taxType, double amount, String transactionRef) {
        this.citizenId = citizenId;
        this.taxType = taxType;
        this.amount = amount;
        this.transactionRef = transactionRef;
        this.status = PaymentStatus.COMPLETED;
    }

    public TaxPayment(int paymentId, int citizenId, TaxType taxType, double amount,
                      String transactionRef, PaymentStatus status, Timestamp paidAt) {
        this.paymentId = paymentId;
        this.citizenId = citizenId;
        this.taxType = taxType;
        this.amount = amount;
        this.transactionRef = transactionRef;
        this.status = status;
        this.paidAt = paidAt;
    }

    // Getters and Setters
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getCitizenId() { return citizenId; }
    public void setCitizenId(int citizenId) { this.citizenId = citizenId; }

    public TaxType getTaxType() { return taxType; }
    public void setTaxType(TaxType taxType) { this.taxType = taxType; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getTransactionRef() { return transactionRef; }
    public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    public Timestamp getPaidAt() { return paidAt; }
    public void setPaidAt(Timestamp paidAt) { this.paidAt = paidAt; }
}