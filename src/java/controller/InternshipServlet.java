/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.sql.Connection;
import java.time.LocalDateTime;
import model.Company;
import model.Internship;
import service.InternshipService;
import utils.ConnectionFile;

/**
 *
 * @author kanan
 */
public class InternshipServlet extends HttpServlet {

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (conn == null) {
            request.setAttribute("error", "Database connection failed. Try again!");
            request.getRequestDispatcher("EmployerDashboard.jsp").forward(request, response);
            return;
        }
        
        // Retrieve the logged-in employer's company from the session.
        Company company = (Company) request.getSession().getAttribute("company");
        if (company == null) {
            request.setAttribute("error", "No company information found. Please log in as an employer.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        String title = request.getParameter("title");
        String category = request.getParameter("category");
        String location = request.getParameter("location");
        String description = request.getParameter("description");
        String requirements = request.getParameter("requirements");

        // Input validation
        boolean hasErrors = false;
        String errorList = "";

        if (title == null) {
            hasErrors = true;
            errorList += "\nInternship title cannot be empty!";
        }
        if (category == null) {
            hasErrors = true;
            errorList += "\nCategory cannot be empty!";
        }
        if (location == null) {
            hasErrors = true;
            errorList += "\nLocation cannot be empty!";
        }
        
        if (hasErrors) {
            request.setAttribute("error", errorList);
            request.getRequestDispatcher("EmployerDashboard.jsp").forward(request, response);
            return;
        }


        // Create a new Internship object
        Internship internship = new Internship();
        internship.setCompanyId(company.getCompanyId());
        internship.setTitle(title);
        internship.setCategory(category);
        internship.setLocation(location);
        internship.setDescription(description);
        internship.setPostedDate(LocalDateTime.now());
        internship.setRequirements(requirements);
        internship.setStatus("open");

        
        try {
            // Create the internship record.
            InternshipService internshipService = new InternshipService(conn);
            int internshipId = internshipService.createInternship(internship);
            
            request.setAttribute("success", "Internship created successfully with ID: " + internshipId);
            request.getRequestDispatcher("EmployerDashboard.jsp").forward(request, response);

        } catch (SQLException ex) {
            request.setAttribute("error", "Failed to create internship: " + ex.getMessage());
            request.getRequestDispatcher("EmployerDashboard.jsp").forward(request, response);
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
