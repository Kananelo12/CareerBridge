package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.User;
import model.UserDetail;
import utils.ConnectionFile;

public class UserDAO {

    // Database connection variable
    private Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    public int insertUser(User user) throws SQLException {
        String query = "INSERT INTO users (email, password, role_id) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, user.getEmail());
        pstmt.setString(2, user.getPassword());
        pstmt.setInt(3, user.getRoleId());
        int affectedRows = pstmt.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }
        int userId;
        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
        pstmt.close();
        return userId;
    }

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET email = ? WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getEmail());
            pstmt.setInt(2, user.getUserId());
            pstmt.executeUpdate();
        }
    }

    public void deleteUser(int userId) throws SQLException {
        String query = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }

    // Retrieves a user by id along
    public UserDetail getUserById(int userId) throws SQLException {
        String query = "SELECT user_id, firstName, lastName, email, phoneNumber, address, profileImageUrl, company_id "
                + "FROM userdetails WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    UserDetail userDetails = new UserDetail();
                    userDetails.setUserId(rs.getInt("user_id"));
                    userDetails.setFirstName(rs.getString("firstName"));
                    userDetails.setLastName(rs.getString("lastName"));
                    userDetails.setEmail(rs.getString("email"));
                    userDetails.setPhoneNumber(rs.getString("phoneNumber"));
                    userDetails.setAddress(rs.getString("address"));
                    userDetails.setProfileImageUrl(rs.getString("profileImageUrl"));
                    userDetails.setCompanyId(rs.getInt("company_id"));
                    return userDetails;
                }
            }
        }
        return null;
    }

    // Retrieves a user by email along with its role info (join with the roles table)
    public User getUserByEmail(String email) throws SQLException {
        String query = "SELECT u.user_id, u.email, u.password, u.role_id, r.rolename FROM users u JOIN roles r ON u.role_id = r.role_id WHERE email = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            User user = new User();
            user.setUserId(rs.getInt("user_id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setRoleId(rs.getInt("role_id"));
            user.setRoleName(rs.getString("rolename"));
            return user;
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        String query = "SELECT u.user_id, u.email AS loginEmail, u.role_id, r.roleName, "
                + "d.firstName, d.lastName, d.email AS personalEmail, d.phoneNumber, d.address, d.profileImageUrl "
                + "FROM users u "
                + "JOIN userdetails d ON u.user_id = d.user_id "
                + "JOIN roles r ON u.role_id = r.role_id";

        try (Connection conn = ConnectionFile.getConn(); PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setEmail(rs.getString("loginEmail"));
                user.setRoleId(rs.getInt("role_id"));
                user.setRoleName(rs.getString("roleName"));

                // Create and set user details
                UserDetail details = new UserDetail();
                details.setFirstName(rs.getString("firstName"));
                details.setLastName(rs.getString("lastName"));
                details.setEmail(rs.getString("personalEmail"));
                details.setPhoneNumber(rs.getString("phoneNumber"));
                details.setAddress(rs.getString("address"));
                details.setProfileImageUrl(rs.getString("profileImageUrl"));

                user.setUserDetails(details);

                users.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    public List<User> getInternUsersByCompany(int companyId) throws SQLException {
        List<User> users = new ArrayList<>();
        String query
                = "SELECT DISTINCT u.user_id, u.email, "
                + "ud.firstName, ud.lastName, ud.phoneNumber, ud.address, ud.profileImageUrl "
                + "FROM users u "
                + "JOIN userdetails ud ON u.user_id = ud.user_id "
                + "JOIN internship i ON u.user_id = i.student_id "
                + "WHERE i.company_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, companyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setEmail(rs.getString("email"));

                    UserDetail details = new UserDetail();
                    details.setFirstName(rs.getString("firstName"));
                    details.setLastName(rs.getString("lastName"));
                    details.setPhoneNumber(rs.getString("phoneNumber"));
                    details.setAddress(rs.getString("address"));
                    details.setProfileImageUrl(rs.getString("profileImageUrl"));

                    user.setUserDetails(details);
                    users.add(user);
                }
            }
        }
        return users;
    }

    public List<User> getInternUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query
                = "SELECT DISTINCT u.user_id, u.email, "
                + "ud.firstName, ud.lastName, ud.phoneNumber, ud.address, ud.profileImageUrl "
                + "FROM users u "
                + "JOIN userdetails ud ON u.user_id = ud.user_id "
                + "JOIN internship i ON u.user_id = i.student_id ";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setEmail(rs.getString("email"));

                    UserDetail details = new UserDetail();
                    details.setFirstName(rs.getString("firstName"));
                    details.setLastName(rs.getString("lastName"));
                    details.setPhoneNumber(rs.getString("phoneNumber"));
                    details.setAddress(rs.getString("address"));
                    details.setProfileImageUrl(rs.getString("profileImageUrl"));

                    user.setUserDetails(details);
                    users.add(user);
                }
            }
        }
        return users;
    }

}
