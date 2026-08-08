package dao;

import model.TaxPayment;
import java.util.List;
import java.util.Optional;

public interface TaxDAO {
    boolean recordTaxPayment(TaxPayment payment);
    Optional<TaxPayment> getPaymentById(int paymentId);
    List<TaxPayment> getPaymentsByCitizenId(int citizenId);
    List<TaxPayment> getAllPayments();
    double getTotalRevenueCollected();
}