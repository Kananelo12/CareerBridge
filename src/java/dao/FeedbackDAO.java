package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Feedback;

/**
 *
 * @author kanan
 */
public class FeedbackDAO {

    private Connection conn;

    public FeedbackDAO(Connection conn) {
        this.conn = conn;
    }

    public int insertFeedback(Feedback feedback) throws SQLException {
        String sql = "INSERT INTO feedback (student_id, internship_id, rating, comments) VALUES (?, ?, ?, ?)";
        int generatedId = 0;
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, feedback.getStudentId());
            stmt.setInt(2, feedback.getInternshipId());
            stmt.setString(3, feedback.getRating());
            stmt.setString(4, feedback.getComments());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                    feedback.setFeedbackId(generatedId);
                } else {
                    throw new SQLException("Failed to retrieve generated feedback ID.");
                }
            }
        }
        return generatedId;
    }

    public void updateFeedback(Feedback feedback) throws SQLException {
        String sql = "UPDATE feedback SET student_id = ?, internship_id = ?, rating = ?, comments = ?, feedback_date = ? "
                + "WHERE feedback_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, feedback.getStudentId());
            stmt.setInt(2, feedback.getInternshipId());
            stmt.setString(3, feedback.getRating());
            stmt.setString(4, feedback.getComments());
            
            if (feedback.getFeedbackDate() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(feedback.getFeedbackDate()));
            } else {
                stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            }
            stmt.setInt(6, feedback.getFeedbackId());
            stmt.executeUpdate();
        }
    }

    public void deleteFeedback(int feedbackId) throws SQLException {
        String sql = "DELETE FROM feedback WHERE feedback_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, feedbackId);
            stmt.executeUpdate();
        }
    }

    public List<Map<String, Object>> getAllFeedbackDetails() throws SQLException {
        List<Map<String, Object>> feedbackDetails = new ArrayList<>();

        String sql = """
        SELECT 
          f.*,
          CONCAT(u.firstName, ' ', u.lastName) AS student_name,
          i.title AS internship_title
        FROM feedback f
        JOIN userdetails u ON f.student_id = u.user_id
        JOIN internship i ON f.internship_id = i.internship_id
        ORDER BY f.feedback_date DESC
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                
                Feedback fb = mapRowToFeedback(rs);
                row.put("feedback", fb);

                // Add the joined fields
                row.put("studentName", rs.getString("student_name"));
                row.put("internshipTitle", rs.getString("internship_title"));

                feedbackDetails.add(row);
            }
        }

        return feedbackDetails;
    }

    public Feedback getFeedbackById(int feedbackId) throws SQLException {
        Feedback feedback = null;
        String sql = "SELECT * FROM feedback WHERE feedback_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, feedbackId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    feedback = mapRowToFeedback(rs);
                }
            }
        }
        return feedback;
    }

    public List<Map<String, Object>> getFeedbacksByCompanyId(int companyId) throws SQLException {
        List<Map<String, Object>> feedbackDetails = new ArrayList<>();
        String sql = "SELECT f.*, "
                + "CONCAT(u.firstName, ' ', u.lastName) AS student_name, "
                + "i.title AS internship_title "
                + "FROM feedback f "
                + "JOIN userdetails u ON f.student_id = u.user_id "
                + "JOIN internship i ON f.internship_id = i.internship_id "
                + "WHERE i.company_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, companyId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                   
                    Feedback feedback = mapRowToFeedback(rs);
                    row.put("feedback", feedback);
                    // Add extra fields from the join
                    row.put("student_name", rs.getString("student_name"));
                    row.put("internship_title", rs.getString("internship_title"));

                    feedbackDetails.add(row);
                }
            }
        }
        return feedbackDetails;
    }

    public List<Feedback> getFeedbacksByInternshipId(int internshipId) throws SQLException {
        List<Feedback> feedbackList = new ArrayList<>();
        String sql = "SELECT * FROM feedback WHERE internship_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, internshipId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Feedback feedback = mapRowToFeedback(rs);
                    feedbackList.add(feedback);
                }
            }
        }
        return feedbackList;
    }

    public List<Map<String, Object>> getFeedbackDetailsByStudentId(int studentId) throws SQLException {
        List<Map<String, Object>> feedbackDetails = new ArrayList<>();
        String sql = "SELECT f.*, "
                + "CONCAT(u.firstName, ' ', u.lastName) AS student_name, "
                + "i.title AS internship_title "
                + "FROM feedback f "
                + "JOIN userdetails u ON f.student_id = u.user_id "
                + "JOIN internship i ON f.internship_id = i.internship_id "
                + "WHERE f.student_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    // Map row to Feedback
                    Feedback feedback = mapRowToFeedback(rs);
                    row.put("feedback", feedback);
                    // Add extra fields from the join
                    row.put("student_name", rs.getString("student_name"));
                    row.put("internship_title", rs.getString("internship_title"));

                    feedbackDetails.add(row);
                }
            }
        }
        return feedbackDetails;
    }

    private Feedback mapRowToFeedback(ResultSet rs) throws SQLException {
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(rs.getInt("feedback_id"));
        feedback.setStudentId(rs.getInt("student_id"));
        feedback.setInternshipId(rs.getInt("internship_id"));
        feedback.setRating(rs.getString("rating"));
        feedback.setComments(rs.getString("comments"));
        Timestamp ts = rs.getTimestamp("feedback_date");
        if (ts != null) {
            feedback.setFeedbackDate(ts.toLocalDateTime());
        }
        return feedback;
    }
}
