package dao;

import model.CertificateApplication;
import java.util.List;

public interface CertificateDAO {
    boolean applyForCertificate(CertificateApplication app);
    List<CertificateApplication> getApplicationsByCitizenId(int citizenId);
    List<CertificateApplication> getAllApplications();
    boolean updateApplicationStatus(int applicationId, String status);
}