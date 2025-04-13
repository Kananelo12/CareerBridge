package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.UserDetail;

/**
 *
 * @author kanan
 */
public class UserDetailDAO {
    private Connection conn;

    public UserDetailDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertUserDetails(UserDetail details) throws SQLException {
        String query = "INSERT INTO userdetails (user_id, firstName, lastName, email, phoneNumber, address, profileImageUrl) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, details.getUserId());
        ps.setString(2, details.getFirstName());
        ps.setString(3, details.getLastName());
        ps.setString(4, details.getEmail());
        ps.setString(5, details.getPhoneNumber());
        ps.setString(6, details.getAddress());
        ps.setString(7, details.getProfileImageUrl());
        ps.executeUpdate();
        ps.close();
    }

    public UserDetail getUserDetailsByUserId(int userId) throws SQLException {
        String query = "SELECT firstName, lastName, profileImageUrl FROM userdetails WHERE user_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();
        UserDetail userDetails = null;
        if (rs.next()) {
            userDetails = new UserDetail();
            userDetails.setFirstName(rs.getString("firstName"));
            userDetails.setLastName(rs.getString("lastName"));
            userDetails.setProfileImageUrl(rs.getString("profileImageUrl"));
        }
        return userDetails;
    }
    
    public void updateCompanyId(int companyId, int userId) throws SQLException {
        String query = "UPDATE userdetails SET company_id = ? WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, companyId);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        }
    }
    
    
}
