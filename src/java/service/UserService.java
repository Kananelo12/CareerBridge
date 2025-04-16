package service;

import dao.CompanyDAO;
import dao.DocumentDAO;
import dao.UserDAO;
import dao.UserDetailDAO;
import java.sql.Connection;
import java.sql.SQLException;
import model.Company;
import model.Document;
import model.User;
import model.UserDetail;
import utils.BCrypt;

/**
 *
 * @author kanan
 */
public class UserService {

    private Connection conn;
    private UserDAO userDAO;
    private UserDetailDAO userDetailsDAO;
    private DocumentDAO documentDAO;
    private CompanyDAO companyDAO;

    public UserService(Connection conn) {
        this.conn = conn;
        this.userDAO = new UserDAO(conn);
        this.userDetailsDAO = new UserDetailDAO(conn);
        this.documentDAO = new DocumentDAO(conn);
        this.companyDAO = new CompanyDAO(conn);
        System.out.println("CompanyDAO initialized: " + (companyDAO != null));
    }

    /**
     * Registers a new user by inserting into the users and userdetails tables,
     * and if the user is an employer, inserting into the documents table.
     *
     * @param user
     * @param details
     * @param document
     * @param company
     * @throws java.sql.SQLException
     */
    public void registerUser(User user, UserDetail details, Document document, Company company) throws SQLException {
        conn.setAutoCommit(false);
        try {
            // Insert into users and get the generated user_id.
            int userId = userDAO.insertUser(user);

            // Set and insert user details.
            details.setUserId(userId);
            userDetailsDAO.insertUserDetails(details);

            // If the user is an employer and a document and company are provided,
            if (user.getRoleId() == 2 && document != null && company != null) {
                // insert into company and documents, then update user details with the company id.
                // Insert company and get generated ID
                int generatedCompanyId = companyDAO.insertCompany(company);
                // Update the existing user details in the database to set the company id.
                userDetailsDAO.updateCompanyId(generatedCompanyId, userId);

                // Insert document linked to user
                document.setUserId(userId);
                documentDAO.insertDocument(document);
            }
            conn.commit();
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    // Returns a User object if the email and password are valid, otherwise returns null.
    public User login(String email, String password) throws SQLException {
        User user = userDAO.getUserByEmail(email);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public void updateUser(User user, UserDetail details) throws SQLException {
        conn.setAutoCommit(false);
        try {
            // Update the main user table
            userDAO.updateUser(user);
            // Update the additional user details table
            userDetailsDAO.updateUserDetails(details);
            conn.commit();
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public void deleteUser(int userId) throws SQLException {
        userDAO.deleteUser(userId);
    }

    public UserDetail getUserById(int userId) throws SQLException {
        return userDAO.getUserById(userId);
    }

    //
    public UserDetail getUserDetails(int userID) throws SQLException {
        return userDetailsDAO.getUserDetailsByUserId(userID);
    }

    //
    public Document getDocument(int userId) throws SQLException {
        return documentDAO.getDocumentByUserId(userId);
    }

    public Company getCompany(int userId) throws SQLException {
        return companyDAO.getCompanyByUserId(userId);
    }
}
