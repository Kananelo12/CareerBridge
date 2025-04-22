/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.FeedbackReplyDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.sql.Connection;
import java.sql.SQLException;
import model.FeedbackReply;
import utils.ConnectionFile;

/**
 *
 * @author kanan
 */
public class ReplyFeedbackServlet extends HttpServlet {
    
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (conn == null) {
            request.setAttribute("error", "Database connection failed. Try again later.");
            request.getRequestDispatcher("EmployerDashboard.jsp").forward(request, response);
            return;
        }
        
        String feedbackIdStr = request.getParameter("feedbackIdHdn");
        String replyText = request.getParameter("replyText");
        
        // Input validation
        if (feedbackIdStr == null || replyText == null) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("EmployerDashboard.jsp").forward(request, response);
            return;
        }
        
        int feedbackId;
        try {
            feedbackId = Integer.parseInt(feedbackIdStr);
        } catch (NumberFormatException ex) {
            request.setAttribute("error", "Invalid feedback ID found!");
            request.getRequestDispatcher("EmployerDashboard.jsp").forward(request, response);
            return;
        }
        
        // Insert into database
        try {
            FeedbackReplyDAO replyDAO = new FeedbackReplyDAO(conn);
            FeedbackReply reply = new FeedbackReply();
            reply.setFeedbackId(feedbackId);
            reply.setReplyText(replyText);
            
            replyDAO.insertReply(reply);
            
            HttpSession session = request.getSession();
            session.setAttribute("feedbackId", feedbackId);
            request.setAttribute("success", "Reply submitted successfully.");
            request.getRequestDispatcher("EmployerDashboard.jsp").forward(request, response);
        } catch (SQLException ex) {
            request.setAttribute("error", "Reply submission failed: " + ex.getMessage());
            request.getRequestDispatcher("EmployerDashboard.jsp").forward(request, response);
        }
        
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
