package dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import model.Company;
import model.Document;

/**
 *
 * @author kanan
 */
public class CompanyDAO {

    private Connection conn;

    public CompanyDAO(Connection conn) {
        this.conn = conn;
    }

    public int insertCompany(Company company) throws SQLException {
        String query = "INSERT INTO company (company_name, description, industry, website, logo_url, location, contact_info) VALUES (?, ?, ?, ?, ?, ?, ?)";

        int companyId = 0;
        try (PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, company.getCompanyName());
            pstmt.setString(2, company.getDescription());
            pstmt.setString(3, company.getIndustry());
            pstmt.setString(4, company.getWebsite());
            pstmt.setString(5, company.getLogoUrl());
            pstmt.setString(6, company.getLocation());
            pstmt.setString(7, company.getContactInfo());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    companyId = rs.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve generated company id.");
                }
            }
        }
        return companyId;
    }

    public Company getCompanyByUserId(int userId) throws SQLException {
        String query = "SELECT c.company_id, c.company_name, c.description, c.industry, c.website, c.logo_url, c.location, c.contact_info "
                + "FROM company c JOIN userdetails ud ON c.company_id = ud.company_id WHERE ud.user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                Company company = null;
                if (rs.next()) {
                    company = new Company();
                    company.setCompanyId(rs.getInt("company_id"));
                    company.setCompanyName(rs.getString("company_name"));
                    company.setDescription(rs.getString("description"));
                    company.setIndustry(rs.getString("industry"));
                    company.setWebsite(rs.getString("website"));
                    company.setLogoUrl(rs.getString("logo_url"));
                    company.setLocation(rs.getString("location"));
                    company.setContactInfo(rs.getString("contact_info"));
                }
                return company;
            }
        }
    }
    
}
