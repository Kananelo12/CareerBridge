/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.FeedbackDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import service.InternshipService;
import utils.ConnectionFile;

/**
 *
 * @author kanan
 */
public class FeedbackServlet extends HttpServlet {

    // Database connection variable
    private Connection conn;

    @Override
    public void init() throws ServletException {
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
            request.setAttribute("error", "Database connection failed. Try again later.");
            request.getRequestDispatcher("feedback.jsp").forward(request, response);
            return;
        }

        // Get the action and id params
        String action = request.getParameter("action");
        String feedbackIdParam = request.getParameter("id");

        int feedbackId;
        if ("delete".equalsIgnoreCase(action)) {
            try {
                feedbackId = Integer.parseInt(feedbackIdParam);
                
                // Delete the feedback using its ID
                InternshipService internService = new InternshipService(conn);
                internService.deleteFeedback(feedbackId);
                
                request.setAttribute("success", "Feedback deleted successfully!");
                request.getRequestDispatcher("AdminDashboard.jsp").forward(request, response);
            } catch (NumberFormatException | SQLException ex) {
                request.setAttribute("error", ex.getMessage());
                request.getRequestDispatcher("AdminDashboard.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (conn == null) {
            request.setAttribute("error", "Database connection failed. Try again later.");
            request.getRequestDispatcher("feedback.jsp").forward(request, response);
            return;
        }

        // Get parameters from the form submission (assumed to be sent via POST)
        String internshipIdParam = request.getParameter("internship_id");
        String studentIdParam = request.getParameter("student_id");
        String rating = request.getParameter("rating");
        String comments = request.getParameter("comments");

        // Validating all fields
        if (internshipIdParam == null || studentIdParam == null
                || rating == null || rating.trim().isEmpty()
                || comments == null || comments.trim().isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("feedback.jsp").forward(request, response);
            return;
        }

        int internshipId;
        int studentId;
        try {
            internshipId = Integer.parseInt(internshipIdParam);
            studentId = Integer.parseInt(studentIdParam);
        } catch (NumberFormatException ex) {
            request.setAttribute("error", "Invalid internship or student ID.");
            request.getRequestDispatcher("feedback.jsp").forward(request, response);
            return;
        }

        // Insert into Database
        String sql = "INSERT INTO feedback (student_id, internship_id, rating, comments) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, internshipId);
            pstmt.setString(3, rating);
            pstmt.setString(4, comments);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                request.setAttribute("success", "Thank you! Your feedback has been submitted.");
                request.getRequestDispatcher("StudentDashboard.jsp").forward(request, response);
                return;
            } else {
                request.setAttribute("error", "Feedback submission failed. Please try again.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("error", "Feedback submission failed: " + ex.getMessage());
        }

        request.getRequestDispatcher("feedback.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
