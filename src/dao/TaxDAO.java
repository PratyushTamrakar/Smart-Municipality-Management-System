package dao;

import model.Payment;

import java.util.List;

public interface TaxDAO extends MunicipalDAO {

    boolean makePayment(Payment payment);

    List<Payment> getPaymentsByCitizen(int citizenId);

    List<Payment> getAllPayments();
}