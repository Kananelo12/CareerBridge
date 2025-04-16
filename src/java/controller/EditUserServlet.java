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

import java.sql.Connection;
import java.sql.SQLException;
import model.Internship;
import model.User;
import model.UserDetail;
import service.InternshipService;
import service.UserService;
import utils.ConnectionFile;

/**
 *
 * @author kanan
 */
public class EditUserServlet extends HttpServlet {

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
            request.getRequestDispatcher("AdminDashboard.jsp").forward(request, response);
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            request.setAttribute("error", "No User ID provided.");
            response.sendRedirect("AdminDashboard.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("delete".equalsIgnoreCase(action)) {
            try {
                int studentId = Integer.parseInt(idParam);
                UserService userService = new UserService(conn);
                userService.deleteUser(studentId);

                request.setAttribute("success", "User deleted successfully.");
            } catch (SQLException | NumberFormatException ex) {
                request.setAttribute("error", "Failed to delete user: " + ex.getMessage());
            }

            // Forward back to dashboard
            request.getRequestDispatcher("AdminDashboard.jsp").forward(request, response);
        } else {
            try {
                int studentId = Integer.parseInt(idParam);
                UserService userService = new UserService(conn);
                UserDetail userInfo = userService.getUserById(studentId);

                if (userInfo == null) {
                    request.setAttribute("error", "User not found.");
                    request.getRequestDispatcher("AdminDashboard.jsp").forward(request, response);
                    return;
                }

                request.setAttribute("allUsers", userInfo);
                request.getRequestDispatcher("EditUser.jsp").forward(request, response);
            } catch (NumberFormatException | SQLException ex) {
                ex.printStackTrace();
                request.setAttribute("error", "Invalid user ID or error fetching data: " + ex.getMessage());
                request.getRequestDispatcher("AdminDashboard.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (conn == null) {
            request.setAttribute("error", "Database connection failed. Try again!");
            request.getRequestDispatcher("AdminDashboard.jsp").forward(request, response);
            return;
        }

        int studentId = Integer.parseInt(request.getParameter("studentId"));

        UserService userService = new UserService(conn);
        UserDetail userInfo = new UserDetail();

        // Get the user information from the database
        try {
            userInfo = userService.getUserById(studentId);
        } catch (SQLException ex) {
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("EditUser.jsp").forward(request, response);
        }

        if (userInfo == null) {
            request.setAttribute("error", "User not found!");
            request.getRequestDispatcher("EditUser.jsp").forward(request, response);
        }

        // Get updated parameters from form fields
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String address = request.getParameter("address");

        // Update the UserDetail object with new values
        userInfo.setFirstName(firstName);
        userInfo.setLastName(lastName);
        userInfo.setEmail(email);
        userInfo.setPhoneNumber(phoneNumber);
        userInfo.setAddress(address);

        // Update the user model fields
        User user = new User();
        user.setUserId(studentId);
        user.setEmail(email);

        // Updating both users and user details tables
        try {
            userService.updateUser(user, userInfo);
            request.setAttribute("success", "User information updated successfully.");
            request.getRequestDispatcher("AdminDashboard.jsp").forward(request, response);
        } catch (SQLException ex) {
            request.setAttribute("error", "Update failed: " + ex.getMessage());
            request.getRequestDispatcher("EditUser.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
