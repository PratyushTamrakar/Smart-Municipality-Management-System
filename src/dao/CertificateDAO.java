package dao;

import model.Certificate;

import java.util.List;

public interface CertificateDAO extends MunicipalDAO {

    boolean applyCertificate(Certificate certificate);

    List<Certificate> getApplicationsByCitizen(int citizenId);

    List<Certificate> getAllApplications();

    boolean updateCertificateStatus(int applicationId, String status);
}