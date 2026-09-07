package util;

import model.Payment;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Automatically writes a text receipt to disk (receipts/receipt_<id>.txt)
 * the moment a tax or fee payment is completed. No payment gateway
 * integration exists yet, so this simulates the confirmation step.
 */
public class ReceiptGenerator {

    private static final String RECEIPTS_FOLDER = "receipts";

    private ReceiptGenerator() {
    }

    public static String generateReceipt(Payment payment) {
        try {
            Files.createDirectories(Paths.get(RECEIPTS_FOLDER));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileName = RECEIPTS_FOLDER + "/receipt_" + payment.getPaymentId() + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("==================================================");
            writer.println("           KASTHAMANDAP MUNICIPALITY");
            writer.println("         SMART MUNICIPALITY MANAGEMENT SYSTEM");
            writer.println("                 PAYMENT RECEIPT");
            writer.println("==================================================");
            writer.println("Receipt No     : " + payment.getPaymentId());
            writer.println("Citizen        : " + payment.getCitizenName());
            writer.println("Payment Type   : " + payment.getPaymentType());
            writer.println("Details        : " + payment.getDetails());

            if (payment.getWardNo() != null && !payment.getWardNo().isEmpty()) {
                writer.println("Ward No        : " + payment.getWardNo());
            }
            if (payment.getHouseNo() != null && !payment.getHouseNo().isEmpty()) {
                writer.println("House No       : " + payment.getHouseNo());
            }
            if (payment.getMonths() != null) {
                writer.println("Months Paid    : " + payment.getMonths());
            }
            if (payment.getVehicleCC() != null) {
                writer.println("Vehicle CC     : " + payment.getVehicleCC());
            }
            if (payment.getBusinessType() != null && !payment.getBusinessType().isEmpty()) {
                writer.println("Business Type  : " + payment.getBusinessType());
            }
            if (payment.getBusinessTier() != null && !payment.getBusinessTier().isEmpty()) {
                writer.println("Business Tier  : " + payment.getBusinessTier());
            }
            if (payment.getPropertyValue() != null) {
                writer.println("Property Value : Rs. " + payment.getPropertyValue());
            }

            writer.println("--------------------------------------------------");
            writer.printf("Amount Paid    : Rs. %.2f%n", payment.getAmount());
            writer.println("Gateway        : " + payment.getGateway());
            writer.println("Status         : " + payment.getPaymentStatus());
            writer.println("Date           : " + payment.getPaymentDate());
            writer.println("==================================================");
            writer.println("           Thank you for your payment");
            writer.println("==================================================");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return fileName;
    }
}