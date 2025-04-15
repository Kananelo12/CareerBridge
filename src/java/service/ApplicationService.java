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

    /**
     * Creates a new application.
     *
     * @param application The Application object to insert.
     * @return The generated application ID.
     * @throws SQLException if an SQL error occurs.
     */
    public int createApplication(Application application) throws SQLException {
        return applicationDAO.insertApplication(application);
    }

    /**
     * Updates an existing application.
     *
     * @param application The Application object with updated values.
     * @throws SQLException if an SQL error occurs.
     */
    public void updateApplication(Application application) throws SQLException {
        applicationDAO.updateApplication(application);
    }

    /**
     * Deletes an application.
     *
     * @param applicationId The ID of the application to delete.
     * @throws SQLException if an SQL error occurs.
     */
    public void deleteApplication(int applicationId) throws SQLException {
        applicationDAO.deleteApplication(applicationId);
    }

    /**
     * Retrieves an application by its ID.
     *
     * @param applicationId The application ID.
     * @return The Application object or null if not found.
     * @throws SQLException if an SQL error occurs.
     */
    public Application getApplicationById(int applicationId) throws SQLException {
        return applicationDAO.getApplicationById(applicationId);
    }

    /**
     * Retrieves all applications for a given internship.
     *
     * @param internshipId The internship ID.
     * @return A list of Application objects.
     * @throws SQLException if an SQL error occurs.
     */
    public List<Application> getApplicationsByInternshipId(int internshipId) throws SQLException {
        return applicationDAO.getApplicationsByInternshipId(internshipId);
    }

    /**
     * Retrieves all applications for a given student.
     *
     * @param studentId The student ID.
     * @return A list of maps where each map contains an Application object and
     * additional data.
     * @throws SQLException if an SQL error occurs.
     */
    public List<Map<String, Object>> getApplicationsByStudentId(int studentId) throws SQLException {
        return applicationDAO.getApplicationsByStudentId(studentId);
    }

    /**
     * Retrieves all applications for a given company.
     *
     * @param companyId The company ID.
     * @return A list of maps where each map contains an Application object and
     * additional data.
     * @throws SQLException if an SQL error occurs.
     */
    public List<Map<String, Object>> getApplicationsByCompanyId(int companyId) throws SQLException {
        return applicationDAO.getApplicationsByCompanyId(companyId);
    }
}
