package listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import utils.BCrypt;
import utils.ConnectionFile;

/**
 *
 * @author kanan
 */
@WebListener
public class StartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        // set the absolute path to the .env file
        String envFilePath = sce.getServletContext().getRealPath("/WEB-INF/config/.env");
        // Setting it as a system property to be extracted when needed
        System.setProperty("env.filepath", envFilePath);
        System.out.println("Configured .env file path: " + envFilePath);
        
        // Seed database with admin user at app start up if no users exist
        try (Connection conn = ConnectionFile.getConn()) {

            // Seed database with roles
            String roleCheckQuery = "SELECT COUNT(*) AS count FROM roles";
            Statement roleStmt = conn.createStatement();
            ResultSet roleRs = roleStmt.executeQuery(roleCheckQuery);
            roleRs.next();
            int roleCount = roleRs.getInt("count");

            if (roleCount == 0) {
                System.out.println("[SEED] No roles found. Creating default roles...");

                String insertRolesQuery = "INSERT INTO roles (role_id, roleName) VALUES (?, ?), (?, ?), (?, ?)";
                PreparedStatement rolePs = conn.prepareStatement(insertRolesQuery);
                rolePs.setInt(1, 1);
                rolePs.setString(2, "admin");
                rolePs.setInt(3, 2);
                rolePs.setString(4, "employer");
                rolePs.setInt(5, 3);
                rolePs.setString(6, "student");
                rolePs.executeUpdate();
                rolePs.close();
                System.out.println("[SEED] Roles seeded successfully.");
            } else {
                System.out.println("[SEED] Roles already exist. Skipping role seeding.");
            }
            roleStmt.close();

            // Seed database with an admin user if no users exist
            String checkQuery = "SELECT COUNT(*) AS count FROM users";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(checkQuery);
            rs.next();
            int userCount = rs.getInt("count");

            if (userCount == 0) {
                System.out.println("[SEED] No users found. Creating admin account...");

                // Insert into users table
                String email = "kananeloj12@gmail.com";
                String plainPassword = "admin@123";
                String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

                String insertUserQuery = "INSERT INTO users (email, password, role_id) VALUES (?, ?, ?)";
                PreparedStatement userPs = conn.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS);
                userPs.setString(1, email);
                userPs.setString(2, hashedPassword);
                userPs.setInt(3, 1);
                userPs.executeUpdate();

                ResultSet generatedKeys = userPs.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);

                    // Insert into userdetails
                    String insertDetailsQuery = """
                        INSERT INTO userdetails (
                            user_id, firstName, lastName, email, phoneNumber,
                            address, profileImageUrl
                        ) VALUES (?, ?, ?, ?, ?, ?, ?)
                    """;

                    PreparedStatement detailsPs = conn.prepareStatement(insertDetailsQuery);
                    detailsPs.setInt(1, userId);
                    detailsPs.setString(2, "Kananelo");
                    detailsPs.setString(3, "Joel");
                    detailsPs.setString(4, email);
                    detailsPs.setString(5, "+266 57650967");
                    detailsPs.setString(6, "Admin HQ");
                    detailsPs.setString(7, null); // profileImageUrl

                    detailsPs.executeUpdate();
                    detailsPs.close();

                    System.out.println("[SEED] Admin user and userdetails inserted.");
                }

                userPs.close();
            } else {
                System.out.println("[SEED] Users already exist, skipping seeding.");
            }

            stmt.close();
        } catch (SQLException e) {
            System.err.println("[SEED ERROR] " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[APP SHUTDOWN] Cleaning up resources...");
    }
}
