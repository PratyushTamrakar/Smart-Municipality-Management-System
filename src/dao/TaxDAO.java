package dao;

import model.TaxPayment;
import java.util.List;

public interface TaxDAO {
    boolean payTax(TaxPayment payment);
    List<TaxPayment> getPaymentsByCitizenId(int citizenId);
    List<TaxPayment> getAllPayments();
}