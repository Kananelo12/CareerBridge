package controller;

import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;
import javax.mail.MessagingException;
import model.Company;
import model.Document;
import model.User;
import model.UserDetail;
import service.UserService;
import utils.ConnectionFile;
import utils.NotificationService;

/**
 *
 * @author kanan
 */
public class LoginServlet extends HttpServlet {

    // Database connection variable
    private Connection conn;

    @Override
    public void init() {
        try {
            conn = ConnectionFile.getConn();
        } catch (Exception ex) {
            System.err.println("Connection Failed: " + ex.getMessage());
        }
    }

    // Generates a random 6-digit OTP code.
    private String generateOTP() {
        int otp = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Checking if database connection is available
        if (conn == null) {
            request.setAttribute("error", "Database connection failed. Try again later.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        String isVerifyAdmin = request.getParameter("admin-passkey");
        if (isVerifyAdmin != null) {
            HttpSession session = request.getSession();
            String generatedOtp = (String) session.getAttribute("generatedOtp");

            // If an OTP is present in session, we're in OTP verification mode
            if (generatedOtp != null) {
                StringBuilder enteredOtp = new StringBuilder();
                for (int i = 1; i <= 6; i++) {
                    String digit = request.getParameter("otp" + i);
                    enteredOtp.append(digit != null ? digit : "");
                }

                if (enteredOtp.length() == 6 && generatedOtp.equals(enteredOtp.toString())) {
                    // When OTP is correct, load user details and set in session
                    String adminEmail = (String) session.getAttribute("adminEmail");
                    UserDAO userDAO = new UserDAO(conn);
                    User adminUser;
                    try {
                        adminUser = userDAO.getUserByEmail(adminEmail);
                        session.setAttribute("user", adminUser);
                    } catch (SQLException ex) {
                        request.setAttribute("error", "Error verifying user: " + ex.getMessage());
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                    }

                    response.sendRedirect("AdminDashboard.jsp");
                    return;
                } else {
                    request.setAttribute("error", "Incorrect or incomplete OTP. Please try again.");
                    request.setAttribute("active", session.getAttribute("adminEmail"));
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }
            }

            String adminEmail = request.getParameter("adminEmail");
            if (adminEmail == null || adminEmail.trim().isEmpty()) {
                request.setAttribute("error", "Please provide your admin email.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            // Verify that the email belongs to an admin
            UserService userService = new UserService(conn);
            User adminUser;
            try {
                adminUser = userService.getUserByEmail(adminEmail);

                if (adminUser == null || !"admin".equalsIgnoreCase(adminUser.getRoleName())) {
                    request.setAttribute("error", "Only admins are allowed to request an OTP.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }
            } catch (SQLException ex) {
                request.setAttribute("error", "Error verifying user: " + ex.getMessage());
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

            // Generate and send OTP
            String otp = generateOTP();
            try {
                NotificationService.sendEmail(
                        adminEmail,
                        "Your Admin OTP Code",
                        "Your OTP code is: " + otp
                );
            } catch (MessagingException ex) {
                throw new ServletException("Error sending OTP email", ex);
            }

            // Store OTP context in session and prompt user
            session.setAttribute("generatedOtp", otp);
            session.setAttribute("adminEmail", adminEmail);
            request.setAttribute("success", "OTP has been sent to " + adminEmail);
            request.setAttribute("active", adminEmail);
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // Standard Login
        // Retrieve and trim form parameters
        String email = request.getParameter("email");
        String passWd = request.getParameter("password");

        // input validation
        if (email.isEmpty() || passWd.isEmpty()) {
            request.setAttribute("error", "Please fill in all fields!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        try {
            UserService userService = new UserService(conn);
            User user = userService.login(email, passWd);
            // If no record is found, email does not exist
            if (user == null) {
                request.setAttribute("error", "Invalid email or password!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

            // Retrieve additional user details and document info
            int userId = user.getUserId();
            UserDetail userDetails = userService.getUserDetails(userId);
            Document document = userService.getDocument(userId);

            // Invalidating current session
            HttpSession session = request.getSession();
            session.invalidate();
            session = request.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("userDetails", userDetails);
            session.setAttribute("document", document);

            // redirecting based on user role
            String roleName = user.getRoleName().toLowerCase();
            switch (roleName) {
                case "admin" -> {
                    request.getRequestDispatcher("/AdminDashboard").forward(request, response);
                }
                case "employer" -> {
                    Company company = userService.getCompany(userId);
                    session.setAttribute("company", company);
                    response.sendRedirect("EmployerDashboard.jsp");
                }
                case "student" -> {
                    response.sendRedirect("StudentDashboard.jsp");
                }
                default -> {
                    response.sendRedirect("login.jsp");
                }
            }
        } catch (ServletException | IOException | SQLException ex) {
            request.setAttribute("error", "Database Error: " + ex.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
