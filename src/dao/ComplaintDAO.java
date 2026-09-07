package dao;

import model.Complaint;

import java.util.List;

public interface ComplaintDAO extends MunicipalDAO {

    boolean addComplaint(Complaint complaint);

    List<Complaint> getComplaintsByCitizen(int citizenId);

    List<Complaint> getAllComplaints();

    boolean updateComplaintStatus(int complaintId, String status);
}