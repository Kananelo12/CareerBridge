package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.sql.Connection;
import java.sql.SQLException;
import model.Company;
import service.UserService;
import utils.ConnectionFile;

/**
 *
 * @author kanan
 */
public class EditCompanyServlet extends HttpServlet {

    // Database Connection variable
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (conn == null) {
            request.setAttribute("error", "Database connection failed. Try again later.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();

        try {
            // Get parameters
            String companyName = request.getParameter("companyName");
            String description = request.getParameter("description");
            String industry = request.getParameter("industry");
            String website = request.getParameter("website");
            String location = request.getParameter("location");
            String contactInfo = request.getParameter("contactInfo");

            // Basic validation
            if (companyName == null || companyName.trim().isEmpty()
                    || location == null || location.trim().isEmpty()
                    || contactInfo == null || contactInfo.trim().isEmpty()) {

                request.setAttribute("error", "Company name, location, and contact info are required.");
                request.getRequestDispatcher("EmployerDashboard.jsp").forward(request, response);
                return;
            }

            // Get current company from session
            Company company = (Company) session.getAttribute("company");
            if (company == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // Update object
            company.setCompanyName(companyName);
            company.setDescription(description);
            company.setIndustry(industry);
            company.setWebsite(website);
            company.setLocation(location);
            company.setContactInfo(contactInfo);

            UserService userService = new UserService(conn);
            userService.updateCompany(company);

            // Update session and redirect
            session.setAttribute("company", company);
            request.setAttribute("success", "Company profile updated successfully.");
            request.getRequestDispatcher("EmployerDashboard.jsp").forward(request, response);

        } catch (SQLException ex) {
            request.setAttribute("error", "Database error: " + ex.getMessage());
            request.getRequestDispatcher("EmployerDashboard.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
