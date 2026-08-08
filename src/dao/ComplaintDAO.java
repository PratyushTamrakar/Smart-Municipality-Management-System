package dao;

import model.Complaint;
import model.enums.ComplaintStatus;

import java.util.List;
import java.util.Optional;

public interface ComplaintDAO {
    boolean createComplaint(Complaint complaint);
    Optional<Complaint> getComplaintById(int complaintId);
    List<Complaint> getComplaintsByCitizenId(int citizenId);
    List<Complaint> getAllComplaints();
    boolean updateComplaintStatus(int complaintId, ComplaintStatus status);
    boolean assignComplaintToEmployee(int complaintId, int employeeId);
}