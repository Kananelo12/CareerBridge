package service;

import dao.ApplicationDAO;
import model.Application;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kanan
 */
public class ApplicationService {

    private Connection conn;
    private ApplicationDAO applicationDAO;

    public ApplicationService(Connection conn) {
        this.conn = conn;
        this.applicationDAO = new ApplicationDAO(conn);
    }

    public int createApplication(Application application) throws SQLException {
        return applicationDAO.insertApplication(application);
    }


    public void updateApplication(Application application) throws SQLException {
        applicationDAO.updateApplication(application);
    }

    public void deleteApplication(int applicationId) throws SQLException {
        applicationDAO.deleteApplication(applicationId);
    }

    public Application getApplicationById(int applicationId) throws SQLException {
        return applicationDAO.getApplicationById(applicationId);
    }

    public List<Application> getApplicationsByInternshipId(int internshipId) throws SQLException {
        return applicationDAO.getApplicationsByInternshipId(internshipId);
    }

    public List<Map<String, Object>> getApplicationsByStudentId(int studentId) throws SQLException {
        return applicationDAO.getApplicationsByStudentId(studentId);
    }

    public List<Map<String, Object>> getApplicationsByCompanyId(int companyId) throws SQLException {
        return applicationDAO.getApplicationsByCompanyId(companyId);
    }
}
