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

import java.sql.Timestamp;
import java.sql.SQLException;
import java.sql.Connection;
import model.Internship;
import service.InternshipService;
import utils.ConnectionFile;

/**
 *
 * @author kanan
 */
public class EditInternshipServlet extends HttpServlet {

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
        if (conn == null) {
            request.setAttribute("error", "Database connection failed. Try again!");
            request.getRequestDispatcher("EmployerDashboard.jsp").forward(request, response);
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            request.setAttribute("error", "No internship ID provided.");
            response.sendRedirect("EmployerDashboard.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("delete".equalsIgnoreCase(action)) {
            try {
                int internshipId = Integer.parseInt(idParam);
                InternshipService internshipService = new InternshipService(conn);
                internshipService.deleteInternship(internshipId);

                request.setAttribute("success", "Internship deleted successfully.");
            } catch (SQLException | NumberFormatException ex) {
                request.setAttribute("error", "Failed to delete internship: " + ex.getMessage());
            }
            
            request.getRequestDispatcher("EmployerDashboard.jsp").forward(request, response);
        } else {
            try {
                int internshipId = Integer.parseInt(idParam);
                InternshipService internshipService = new InternshipService(conn);
                Internship internship = internshipService.getInternshipById(internshipId);
                if (internship == null) {
                    request.setAttribute("error", "Internship not found.");
                    request.getRequestDispatcher("EmployerDashboard.jsp").forward(request, response);
                    return;
                }

                request.setAttribute("internship", internship);
                request.getRequestDispatcher("EditInternship.jsp").forward(request, response);
            } catch (NumberFormatException | SQLException ex) {
                ex.printStackTrace();
                request.setAttribute("error", "Invalid internship ID or error fetching data: " + ex.getMessage());
                request.getRequestDispatcher("EmployerDashboard.jsp").forward(request, response);
            }
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

        // Retrieve updated internship parameters.
        int internshipId = Integer.parseInt(request.getParameter("internshipId"));

        InternshipService internshipService = new InternshipService(conn);
        Internship updatedInternship = new Internship();

        // Get the original internship from DB
        try {
            updatedInternship = internshipService.getInternshipById(internshipId);
        } catch (SQLException ex) {
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("EmployerDashboard.jsp").forward(request, response);
        }

        if (updatedInternship == null) {
            request.setAttribute("error", "Internship not found.");
            request.getRequestDispatcher("EmployerDashboard.jsp").forward(request, response);
            return;
        }

        String title = request.getParameter("title");
        String category = request.getParameter("category");
        String location = request.getParameter("location");
        String description = request.getParameter("description");
        String requirements = request.getParameter("requirements");
        String status = request.getParameter("status");

        updatedInternship.setInternshipId(internshipId);
        updatedInternship.setTitle(title);
        updatedInternship.setCategory(category);
        updatedInternship.setLocation(location);
        updatedInternship.setDescription(description);
        updatedInternship.setPostedDate(updatedInternship.getPostedDate());
        updatedInternship.setRequirements(requirements);
        updatedInternship.setStatus(status);

        try {
            internshipService.updateInternship(updatedInternship);
            request.setAttribute("success", "Internship updated successfully.");
            request.getRequestDispatcher("EmployerDashboard.jsp").forward(request, response);
        } catch (SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("error", "Failed to update internship: " + ex.getMessage());
            request.getRequestDispatcher("EditInternship.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
