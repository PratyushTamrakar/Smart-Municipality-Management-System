package util;

import model.CertificateApplication;
import model.TaxPayment;
import model.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileExporter {

    private static final String EXPORT_DIR = "exports";

    public static boolean exportTaxReceipt(TaxPayment payment, User citizen) {
        File dir = new File(EXPORT_DIR);
        if (!dir.exists()) {
            dir.mkdirs(); // Creates exports directory if missing
        }

        File file = new File(dir, "Tax_Receipt_" + payment.getPaymentId() + ".txt");

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("=================================================");
            writer.println("       SMART MUNICIPALITY TAX RECEIPT           ");
            writer.println("=================================================");
            writer.println("Receipt ID     : " + payment.getPaymentId());
            writer.println("Citizen Name   : " + citizen.getFullName());
            writer.println("Citizen Email  : " + citizen.getEmail());
            writer.println("Tax Type       : " + payment.getTaxType());
            writer.println("Amount Paid    : Rs. " + payment.getAmount());
            writer.println("Status         : PAID");
            writer.println("=================================================");
            writer.println("Thank you for contributing to municipal growth!");

            System.out.println("📄 File generated at: " + file.getAbsolutePath());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean exportApprovedCertificate(CertificateApplication app, User citizen) {
        File dir = new File(EXPORT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, "Certificate_" + app.getApplicationId() + ".txt");

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("=================================================");
            writer.println("    OFFICIAL MUNICIPAL CERTIFICATE DOCUMENT     ");
            writer.println("=================================================");
            writer.println("Application ID : " + app.getApplicationId());
            writer.println("Certificate    : " + app.getCertificateType());
            writer.println("Applicant Name : " + app.getApplicantName());
            writer.println("Registered By  : " + citizen.getFullName());
            writer.println("Details        : " + app.getDetails());
            writer.println("Approval Status: " + app.getStatus());
            writer.println("=================================================");
            writer.println("Verified by Smart Municipality Management System");

            System.out.println("📄 Certificate generated at: " + file.getAbsolutePath());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}