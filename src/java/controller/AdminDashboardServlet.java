package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import model.DayCount;
import model.User;
import service.DashboardService;
import service.UserService;
import utils.BCrypt;
import utils.ConnectionFile;

/**
 *
 * @author kanan
 */
@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/AdminDashboard"})
public class AdminDashboardServlet extends HttpServlet {

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (conn == null) {
            request.setAttribute("error", "Database connection failed. Try again!");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }
        
        String action = request.getParameter("action");
        if (action != null && "addAdmin".equalsIgnoreCase(action)) {
            String email = request.getParameter("email");
            String pwd = request.getParameter("password");
            String confirm = request.getParameter("confirmPassword");
            
            System.out.println("REACHED PARAMS");

            // Basic validation
            if (email == null || pwd == null || confirm == null || !pwd.equals(confirm) || pwd.length() < 8) {
                System.out.println("REACHED NULL");
                request.setAttribute("error", "Please provide a valid email and matching passwords (min 8 chars).");
                request.getRequestDispatcher("AdminDashboard.jsp").forward(request, response);
                return;
            }
            
            try {
                System.out.println("REACHED TRY CATCH");
                String hashed = BCrypt.hashpw(pwd, BCrypt.gensalt());
                User newAdmin = new User();
                newAdmin.setEmail(email);
                newAdmin.setPassword(hashed);
                newAdmin.setRoleId(1);

                UserService svc = new UserService(conn);
                svc.insertUser(newAdmin);

                System.out.println("REACHED INSERT");
                System.out.println("USER MODEL: " + newAdmin);
                request.setAttribute("success", "New admin created successfully.");
                request.getRequestDispatcher("AdminDashboard.jsp").forward(request, response);
                return;
            } catch (SQLException ex) {
                request.setAttribute("error", "Failed to create admin: " + ex.getMessage());
                request.getRequestDispatcher("AdminDashboard.jsp").forward(request, response);
                return;
            }
        }
        
        try {
            var svc = new DashboardService(conn);

            // Store in session
            var session = request.getSession();
            session.setAttribute("totalInternships", svc.getTotalInternships());
            session.setAttribute("totalApplications", svc.getTotalApplications());
            session.setAttribute("pendingApplications", svc.getPendingApplications());

            // last 7 days
            List<DayCount> series = svc.getDailyApplicationCounts(7);
            session.setAttribute("dailyAppCounts", series);
            
            response.sendRedirect("AdminDashboard.jsp");

        } catch (SQLException ex) {
            throw new ServletException("Unable to load dashboard metrics", ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
