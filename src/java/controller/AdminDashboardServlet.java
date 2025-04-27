/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
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
import service.DashboardService;
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
        try {
            var svc = new DashboardService(conn);

            // Store in session
            var session = request.getSession();
            session.setAttribute("totalInternships", svc.getTotalInternships());
            session.setAttribute("totalApplications", svc.getTotalApplications());
            session.setAttribute("pendingApplications", svc.getPendingApplications());
            System.out.println("IS SESSION WORKING");

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
