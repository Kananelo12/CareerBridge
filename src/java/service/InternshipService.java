package service;

import dao.ApplicationDAO;
import dao.FeedbackDAO;
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
    private FeedbackDAO feedbackDAO;
    private ApplicationDAO applicationDAO;

    public InternshipService(Connection conn) {
        this.conn = conn;
        this.internshipDAO = new InternshipDAO(conn);
        this.feedbackDAO = new FeedbackDAO(conn);
        this.applicationDAO = new ApplicationDAO(conn);
    }

    public int createInternship(Internship internship) throws SQLException {
        return internshipDAO.insertInternship(internship);
    }

    public void updateInternship(Internship internship) throws SQLException {
        internshipDAO.updateInternship(internship);
    }

    public void updateIntern(Internship internship) throws SQLException {
        internshipDAO.updateIntern(internship);
    }

    public void deleteInternship(int internshipId) throws SQLException {
        internshipDAO.deleteInternship(internshipId);
    }

    public Internship getInternshipById(int internshipId) throws SQLException {
        return internshipDAO.getInternshipById(internshipId);
    }

    public List<Internship> getInternships(int offset, int limit) throws SQLException {
        return internshipDAO.getInternships(offset, limit);
    }

    public void deleteFeedback(int feedbackId) throws SQLException {
        feedbackDAO.deleteFeedback(feedbackId);
    }

}
