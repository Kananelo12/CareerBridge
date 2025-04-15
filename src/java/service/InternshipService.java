package service;

import dao.InternshipDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import model.Internship;
/**
 *
 * @author kanan
 */
public class InternshipService {
    
    private Connection conn;
    private InternshipDAO internshipDAO;

    public InternshipService(Connection conn) {
        this.conn = conn;
        this.internshipDAO = new InternshipDAO(conn);
    }
    
    /**
     * Creates a new internship.
     * 
     * @param internship the Internship object to insert.
     * @return the generated internship ID.
     * @throws SQLException if any SQL error occurs.
     */
    public int createInternship(Internship internship) throws SQLException {
        return internshipDAO.insertInternship(internship);
    }

    /**
     * Updates an existing internship.
     * 
     * @param internship the Internship object with updated values.
     * @throws SQLException if any SQL error occurs.
     */
    public void updateInternship(Internship internship) throws SQLException {
        internshipDAO.updateInternship(internship);
    }
    
    /**
     * Updates an existing internship.
     * 
     * @param internship the Internship object with updated values.
     * @throws SQLException if any SQL error occurs.
     */
    public void updateIntern(Internship internship) throws SQLException {
        internshipDAO.updateIntern(internship);
    }

    /**
     * Deletes an internship by its ID.
     * 
     * @param internshipId the ID of the internship to delete.
     * @throws SQLException if any SQL error occurs.
     */
    public void deleteInternship(int internshipId) throws SQLException {
        internshipDAO.deleteInternship(internshipId);
    }

    /**
     * Retrieves an internship by its ID.
     * 
     * @param internshipId the ID of the internship.
     * @return the Internship object, or null if not found.
     * @throws SQLException if any SQL error occurs.
     */
    public Internship getInternshipById(int internshipId) throws SQLException {
        return internshipDAO.getInternshipById(internshipId);
    }

    /**
     * Retrieves a paginated list of internships.
     * 
     * @param offset the starting index.
     * @param limit the number of records to retrieve.
     * @return a List of Internship objects.
     * @throws SQLException if any SQL error occurs.
     */
    public List<Internship> getInternships(int offset, int limit) throws SQLException {
        return internshipDAO.getInternships(offset, limit);
    }

}
