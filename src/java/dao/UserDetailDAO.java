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
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, details.getUserId());
        pstmt.setString(2, details.getFirstName());
        pstmt.setString(3, details.getLastName());
        pstmt.setString(4, details.getEmail());
        pstmt.setString(5, details.getPhoneNumber());
        pstmt.setString(6, details.getAddress());
        pstmt.setString(7, details.getProfileImageUrl());
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void updateUserDetails(UserDetail details) throws SQLException {
        String sql = "UPDATE userdetails SET firstName = ?, lastName = ?, email = ?, phoneNumber = ?, address = ?, profileImageUrl = ? WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, details.getFirstName());
            pstmt.setString(2, details.getLastName());
            pstmt.setString(3, details.getEmail());
            pstmt.setString(4, details.getPhoneNumber());
            pstmt.setString(5, details.getAddress());
            pstmt.setString(6, details.getProfileImageUrl());
            pstmt.setInt(7, details.getUserId());
            pstmt.executeUpdate();
        }
    }

    public UserDetail getUserDetailsByUserId(int userId) throws SQLException {
        String query = "SELECT user_id, firstName, lastName, email, profileImageUrl FROM userdetails WHERE user_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();
        UserDetail userDetails = null;
        if (rs.next()) {
            userDetails = new UserDetail();
            userDetails.setUserId(rs.getInt("user_id"));
            userDetails.setFirstName(rs.getString("firstName"));
            userDetails.setLastName(rs.getString("lastName"));
            userDetails.setEmail(rs.getString("email"));
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
