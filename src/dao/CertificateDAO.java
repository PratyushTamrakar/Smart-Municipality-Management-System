package dao;

import model.CertificateApplication;
import model.enums.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface CertificateDAO {
    boolean applyForCertificate(CertificateApplication application);
    Optional<CertificateApplication> getApplicationById(int applicationId);
    List<CertificateApplication> getApplicationsByCitizenId(int citizenId);
    List<CertificateApplication> getAllApplications();
    boolean updateApplicationStatus(int applicationId, RequestStatus status, int officerId);
}