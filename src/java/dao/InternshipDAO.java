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

    public void updateInternship(Internship internship) throws SQLException {
        String sql = "UPDATE internship SET company_id = ?, title = ?, location = ?, description = ?, posted_date = ?, requirements = ?, status = ? "
                + "WHERE internship_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, internship.getCompanyId());
            pstmt.setString(2, internship.getTitle());
            pstmt.setString(3, internship.getLocation());
            pstmt.setString(4, internship.getDescription());
            pstmt.setTimestamp(5, Timestamp.valueOf(internship.getPostedDate()));
            pstmt.setString(6, internship.getRequirements());
            pstmt.setString(7, internship.getStatus());
            pstmt.setInt(8, internship.getInternshipId());
            pstmt.executeUpdate();
        }
    }
    
    public void updateIntern(Internship internship) throws SQLException {
        String sql = "UPDATE internship SET student_id = ? "
                + "WHERE internship_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, internship.getStudentId());
            pstmt.setInt(2, internship.getInternshipId());
            pstmt.executeUpdate();
        }
    }

    public void deleteInternship(int internshipId) throws SQLException {
        String sql = "DELETE FROM internship WHERE internship_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, internshipId);
            pstmt.executeUpdate();
        }
    }

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

    public Internship getInternshipById(int internshipId) throws SQLException {
        String sql = "SELECT i.*, c.company_name, c.logo_url FROM internship i "
                + "JOIN company c ON i.company_id = c.company_id "
                + "WHERE internship_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, internshipId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToInternship(rs);
                }
            }
        }
        return null;
    }

    public List<Internship> getInternships(int offset, int limit) throws SQLException {
        List<Internship> internships = new ArrayList<>();
        String sql = "SELECT i.*, c.company_name, c.logo_url "
                + "FROM internship i "
                + "JOIN company c ON i.company_id = c.company_id "
                + "ORDER BY i.posted_date DESC LIMIT ? OFFSET ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    internships.add(mapRowToInternship(rs));
                }
            }
        }
        return internships;
    }

    public List<Internship> getInternshipsByCompanyId(int companyId) throws SQLException {
        List<Internship> internships = new ArrayList<>();
        String sql = "SELECT i.*, c.company_name, c.logo_url FROM internship i "
                + "JOIN company c ON i.company_id = c.company_id"
                + " WHERE i.company_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, companyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    internships.add(mapRowToInternship(rs));
                }
            }
        }
        return internships;
    }

    public List<Internship> getInternshipsWithCompany(int offset, int limit) throws SQLException {
        List<Internship> internships = new ArrayList<>();
        String sql = "SELECT i.*, c.company_id, c.company_name, c.logo_url "
                + "FROM internship i "
                + "JOIN company c ON i.company_id = c.company_id "
                + "ORDER BY i.posted_date DESC LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            try (ResultSet rs = pstmt.executeQuery()) {
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
    
    public List<Internship> searchInternships(String location, String category) throws SQLException {
        List<Internship> internships = new ArrayList<>();
        String sql = """
            SELECT i.*, c.company_name, c.logo_url
              FROM internship i
              JOIN company c ON i.company_id = c.company_id
             WHERE LOWER(i.location) LIKE ?
               AND i.category = ?
             ORDER BY i.posted_date DESC
            """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + location.trim().toLowerCase() + "%");
            stmt.setString(2, category);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    internships.add(mapRowToInternship(rs));
                }
            }
        }
        return internships;
    }

}
