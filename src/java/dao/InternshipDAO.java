/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Timestamp;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Company;
import model.Internship;

/**
 *
 * @author kanan
 */
public class InternshipDAO {

    // Database connection variable
    private Connection conn;

    public InternshipDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a new internship into the database.
     *
     * @param internship the Internship object to insert.
     * @return the generated internship ID.
     * @throws SQLException if any SQL error occurs.
     */
    public int insertInternship(Internship internship) throws SQLException {
        String query = "INSERT INTO internship (company_id, title, category, location, description, posted_date, requirements) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, internship.getCompanyId());
            pstmt.setString(2, internship.getTitle());
            pstmt.setString(3, internship.getCategory());
            pstmt.setString(4, internship.getLocation());
            pstmt.setString(5, internship.getDescription());
            // Converting LocalDateTime to Timestamp for JDBC
            pstmt.setTimestamp(6, Timestamp.valueOf(internship.getPostedDate()));
            pstmt.setString(7, internship.getRequirements());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    internship.setInternshipId(generatedId);
                    return generatedId;
                }
            }
        }
        return 0;
    }

    /**
     * Updates an existing internship record.
     *
     * @param internship the Internship object with updated values.
     * @throws SQLException if any SQL error occurs.
     */
    public void updateInternship(Internship internship) throws SQLException {
        String sql = "UPDATE internship SET company_id = ?, title = ?, location = ?, description = ?, posted_date = ?, requirements = ?, status = ? "
                + "WHERE internship_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, internship.getCompanyId());
            stmt.setString(2, internship.getTitle());
            stmt.setString(3, internship.getLocation());
            stmt.setString(4, internship.getDescription());
            stmt.setTimestamp(5, Timestamp.valueOf(internship.getPostedDate()));
            stmt.setString(6, internship.getRequirements());
            stmt.setString(7, internship.getStatus());
            stmt.setInt(8, internship.getInternshipId());
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes an internship record from the database.
     *
     * @param internshipId the ID of the internship to delete.
     * @throws SQLException if any SQL error occurs.
     */
    public void deleteInternship(int internshipId) throws SQLException {
        String sql = "DELETE FROM internship WHERE internship_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, internshipId);
            stmt.executeUpdate();
        }
    }

    /**
     * Helper method to map a ResultSet row to an Internship object.
     *
     * @param rs the ResultSet.
     * @return an Internship object.
     * @throws SQLException if any SQL error occurs.
     */
    private Internship mapRowToInternship(ResultSet rs) throws SQLException {
        Internship internship = new Internship();
        internship.setInternshipId(rs.getInt("internship_id"));
        internship.setCompanyId(rs.getInt("company_id"));
        internship.setTitle(rs.getString("title"));
        internship.setCategory(rs.getString("category"));
        internship.setLocation(rs.getString("location"));
        internship.setDescription(rs.getString("description"));
        Timestamp ts = rs.getTimestamp("posted_date");
        if (ts != null) {
            LocalDateTime ldt = ts.toLocalDateTime();
            internship.setPostedDate(ldt);
            // Convert LocalDateTime to java.util.Date
            Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
            internship.setPostedDateAsDate(date);
        }
        internship.setRequirements(rs.getString("requirements"));
        internship.setStudentId(rs.getInt("student_id"));
        internship.setStatus(rs.getString("status"));

        Company company = new Company();
        company.setCompanyId(rs.getInt("company_id"));
        company.setCompanyName(rs.getString("company_name"));
        company.setLogoUrl(rs.getString("logo_url"));

        internship.setCompany(company);
        return internship;
    }

    /**
     * Retrieves an internship by its ID.
     *
     * @param internshipId the ID of the internship.
     * @return an Internship object or null if not found.
     * @throws SQLException if any SQL error occurs.
     */
    public Internship getInternshipById(int internshipId) throws SQLException {
        String sql = "SELECT * FROM internship WHERE internship_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, internshipId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToInternship(rs);
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a paginated list of internships.
     *
     * @param offset the starting index.
     * @param limit the number of records to retrieve.
     * @return a list of Internship objects.
     * @throws SQLException if any SQL error occurs.
     */
    public List<Internship> getInternships(int offset, int limit) throws SQLException {
        List<Internship> internships = new ArrayList<>();
        String sql = "SELECT i.*, c.company_name, c.logo_url "
                + "FROM internship i "
                + "JOIN company c ON i.company_id = c.company_id "
                + "ORDER BY i.posted_date DESC LIMIT ? OFFSET ?";

//        String sql = "SELECT * FROM internship ORDER BY posted_date DESC LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    internships.add(mapRowToInternship(rs));
                }
            }
        }
        return internships;
    }

    public List<Internship> getInternshipsByCompanyId(int companyId) throws SQLException {
        List<Internship> internships = new ArrayList<>();
        String sql = "SELECT * FROM internship WHERE company_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, companyId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    internships.add(mapRowToInternship(rs));
                }
            }
        }
        return internships;
    }

    public List<Internship> getInternshipsWithCompany(int offset, int limit) throws SQLException {
        List<Internship> internships = new ArrayList<>();
        // Adjust field names and table names as needed.
        String sql = "SELECT i.*, c.company_id, c.company_name, c.logo_url "
                + "FROM internship i "
                + "JOIN company c ON i.company_id = c.company_id "
                + "ORDER BY i.posted_date DESC LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Internship internship = mapRowToInternship(rs);
                    // Populate company fields manually
                    Company company = new Company();
                    company.setCompanyId(rs.getInt("company_id"));
                    company.setCompanyName(rs.getString("company_name"));
                    company.setLogoUrl(rs.getString("logo_url"));
                    internship.setCompany(company);
                    internships.add(internship);
                }
            }
        }
        return internships;
    }

}
