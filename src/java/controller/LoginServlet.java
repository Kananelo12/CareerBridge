package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import model.Company;
import model.Document;
import model.User;
import model.UserDetail;
import service.UserService;
import utils.ConnectionFile;

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

    // Generate a 6-digit OTP
    private String generateOTP() {
        int otp = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Ensure database connection is available
        if (conn == null) {
            request.setAttribute("error", "Database connection failed. Try again later.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // If the admin-passkey parameter exists, we assume this is an admin verification request.
        // We validate that the adminEmail is provided. If so, we set success messages and any additional attributes,
        // then forward back to the login page to display the OTP modal. Otherwise, we forward an error message.
        String isVerifyAdmin = request.getParameter("admin-passkey");
        if (isVerifyAdmin != null) {
            HttpSession session = request.getSession();
            String generatedOtp = (String) session.getAttribute("generatedOtp");

            // If an OTP is present in session, we're in OTP verification mode
            if (generatedOtp != null) {
                String otp1 = request.getParameter("otp1");
                String otp2 = request.getParameter("otp2");
                String otp3 = request.getParameter("otp3");
                String otp4 = request.getParameter("otp4");
                String otp5 = request.getParameter("otp5");
                String otp6 = request.getParameter("otp6");

                // Check if all OTP fields are provided
                if (otp1 != null && otp2 != null && otp3 != null && otp4 != null && otp5 != null && otp6 != null) {
                    String enteredOtp = otp1 + otp2 + otp3 + otp4 + otp5 + otp6;
                    if (generatedOtp.equals(enteredOtp)) {
                        // OTP is correct; grant admin access
                        session.setAttribute("isAdmin", true);
                        response.sendRedirect("AdminDashboard.jsp");
                        return;
                    } else {
                        request.setAttribute("error", "Incorrect OTP. Please try again.");
                        request.setAttribute("active", session.getAttribute("adminEmail"));
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                        return;
                    }
                } else {
                    request.setAttribute("error", "Please enter the complete OTP.");
                    request.setAttribute("active", session.getAttribute("adminEmail"));
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }
            } else {
                // No OTP generated yet; handle initial admin OTP request
                String adminEmail = request.getParameter("adminEmail");
                if (adminEmail == null || adminEmail.trim().isEmpty()) {
                    request.setAttribute("error", "Please provide your admin email.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }
                // Generate OTP and send email
                String otp = generateOTP();
                final String senderEmail = "kananeloj12@gmail.com"; // Replace with your sender email
                final String senderPassword = "yuok zbhi mhvv hcsv";   // Use App Password or secure credentials

                Properties properties = new Properties();
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "587");

                Session mailSession = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail, senderPassword);
                    }
                });

                try {
                    Message message = new MimeMessage(mailSession);
                    message.setFrom(new InternetAddress(senderEmail));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(adminEmail));
                    message.setSubject("Your Admin OTP Code");
                    message.setText("Your OTP code is: " + otp);
                    Transport.send(message);
                } catch (MessagingException ex) {
                    ex.printStackTrace();
                    request.setAttribute("error", "Error sending OTP: " + ex.getMessage());
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }

                // Store OTP and adminEmail in session
                session.setAttribute("generatedOtp", otp);
                session.setAttribute("adminEmail", adminEmail);
                request.setAttribute("success", "OTP has been sent to " + adminEmail);
                request.setAttribute("active", adminEmail);
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }
        } else {
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
                // Use the Service Layer for business logic
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

                // Prevent session fixation by invalidating the current session
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
                        response.sendRedirect("AdminDashboard.jsp");
                    }
                    case "employer" -> {
                        // After a successful login for an employer
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
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
