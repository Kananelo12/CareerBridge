/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Application;

/**
 *
 * @author kanan
 */
public class ApplicationDAO {

    private Connection conn;

    public ApplicationDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a new application into the database.
     *
     * @param application the Application object to insert.
     * @return the generated application ID.
     * @throws SQLException if any SQL error occurs.
     */
    public int insertApplication(Application application) throws SQLException {
        String sql = "INSERT INTO application (student_id, internship_id, cv_url, transcript_url) VALUES (?, ?, ?, ?)";
        int generatedId = 0;
        try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, application.getStudentId());
            stmt.setInt(2, application.getInternshipId());
            stmt.setString(3, application.getCvUrl());
            stmt.setString(4, application.getTranscriptUrl());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                    application.setApplicationId(generatedId);
                } else {
                    throw new SQLException("Failed to retrieve generated application ID.");
                }
            }
        }
        return generatedId;
    }

    /**
     * Updates an existing application record.
     *
     * @param application the Application object with updated values.
     * @throws SQLException if any SQL error occurs.
     */
    public void updateApplication(Application application) throws SQLException {
        String sql = "UPDATE application SET student_id = ?, internship_id = ?, cv_url = ?, transcript_url = ?, application_date = ?, status = ? "
                + "WHERE application_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, application.getStudentId());
            stmt.setInt(2, application.getInternshipId());
            stmt.setString(3, application.getCvUrl());
            stmt.setString(4, application.getTranscriptUrl());
            stmt.setTimestamp(5, Timestamp.valueOf(application.getApplicationDate()));
            stmt.setString(6, application.getStatus());
            stmt.setInt(7, application.getApplicationId());
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes an application record from the database.
     *
     * @param applicationId the ID of the application to delete.
     * @throws SQLException if any SQL error occurs.
     */
    public void deleteApplication(int applicationId) throws SQLException {
        String sql = "DELETE FROM application WHERE application_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, applicationId);
            stmt.executeUpdate();
        }
    }

    /**
     * Retrieves an application by its ID.
     *
     * @param applicationId the application ID.
     * @return an Application object, or null if not found.
     * @throws SQLException if any SQL error occurs.
     */
    public Application getApplicationById(int applicationId) throws SQLException {
        Application application = null;
        String sql = "SELECT * FROM application WHERE application_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, applicationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    application = mapRowToApplication(rs);
                }
            }
        }
        return application;
    }

    /**
     * Retrieves all applications for a given internship.
     *
     * @param internshipId the internship ID.
     * @return a list of Application objects.
     * @throws SQLException if any SQL error occurs.
     */
    public List<Application> getApplicationsByInternshipId(int internshipId) throws SQLException {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT * FROM application WHERE internship_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, internshipId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    applications.add(mapRowToApplication(rs));
                }
            }
        }
        return applications;
    }

    public List<Map<String, Object>> getApplicationsByStudentId(int studentId) throws SQLException {
        List<Map<String, Object>> applications = new ArrayList<>();

        String sql = "SELECT a.*, "
                + "CONCAT(u.firstName, ' ', u.lastName) AS student_name, "
                + "a.cv_url, a.transcript_url, "
                + "i.category, i.location, c.company_name "
                + "FROM application a "
                + "JOIN internship i ON a.internship_id = i.internship_id "
                + "JOIN userdetails u ON a.student_id = u.user_id "
                + "JOIN company c ON i.company_id = c.company_id "
                + "WHERE a.student_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    Application app = new Application();
                    app.setApplicationId(rs.getInt("application_id"));
                    app.setStudentId(rs.getInt("student_id"));
                    app.setInternshipId(rs.getInt("internship_id"));
                    app.setCvUrl(rs.getString("cv_url"));
                    app.setTranscriptUrl(rs.getString("transcript_url"));
                    app.setApplicationDate(rs.getTimestamp("application_date").toLocalDateTime());
                    app.setStatus(rs.getString("status"));

                    row.put("application", app);
                    row.put("studentName", rs.getString("student_name"));
                    row.put("companyName", rs.getString("company_name"));
                    row.put("category", rs.getString("category"));
                    row.put("location", rs.getString("location"));

                    applications.add(row);
                }
            }
        }
        return applications;
    }

    public List<Map<String, Object>> getApplicationsByCompanyId(int companyId) throws SQLException {
        List<Map<String, Object>> applications = new ArrayList<>();

        String sql = "SELECT a.*, "
                + "CONCAT(u.firstName, ' ', u.lastName) AS student_name, "
                + "a.cv_url, a.transcript_url, "
                + "i.category, i.location, c.company_name "
                + "FROM application a "
                + "JOIN internship i ON a.internship_id = i.internship_id "
                + "JOIN userdetails u ON a.student_id = u.user_id "
                + "JOIN company c ON i.company_id = c.company_id "
                + "WHERE c.company_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, companyId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    Application app = new Application();
                    app.setApplicationId(rs.getInt("application_id"));
                    app.setStudentId(rs.getInt("student_id"));
                    app.setInternshipId(rs.getInt("internship_id"));
                    app.setCvUrl(rs.getString("cv_url"));
                    app.setTranscriptUrl(rs.getString("transcript_url"));
                    app.setApplicationDate(rs.getTimestamp("application_date").toLocalDateTime());
                    app.setStatus(rs.getString("status"));

                    row.put("application", app);
                    row.put("studentName", rs.getString("student_name"));
                    row.put("companyName", rs.getString("company_name"));
                    row.put("category", rs.getString("category"));
                    row.put("location", rs.getString("location"));

                    applications.add(row);
                }
            }
        }
        return applications;
    }

    /**
     * Maps a ResultSet row to an Application object.
     *
     * @param rs the ResultSet.
     * @return an Application object.
     * @throws SQLException if any SQL error occurs.
     */
    private Application mapRowToApplication(ResultSet rs) throws SQLException {
        Application application = new Application();
        application.setApplicationId(rs.getInt("application_id"));
        application.setStudentId(rs.getInt("student_id"));
        application.setInternshipId(rs.getInt("internship_id"));
        application.setCvUrl(rs.getString("cv_url"));
        application.setTranscriptUrl(rs.getString("transcript_url"));
        Timestamp ts = rs.getTimestamp("application_date");
        if (ts != null) {
            application.setApplicationDate(ts.toLocalDateTime());
        }
        application.setStatus(rs.getString("status"));
        return application;
    }
}
