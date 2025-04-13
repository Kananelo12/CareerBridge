/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ApplicationDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import model.Application;
import model.User;
import utils.ConnectionFile;

/**
 *
 * @author kanan
 */


@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB threshold before writing to disk
    maxFileSize = 1024 * 1024 * 10,         // Max file size: 10MB
    maxRequestSize = 1024 * 1024 * 50         // Max request size: 50MB
)

public class ApplicationServlet extends HttpServlet {

    private Connection conn;

    // directory path for uploaded files
    private static final String STUDENT_DIRECTORY = "C:" + File.separator + "Users" + File.separator
            + "kanan" + File.separator + "Documents" + File.separator + "NetBeansProjects" + File.separator
            + "2230541_Internship_System" + File.separator + "web" + File.separator + "uploads" + File.separator + "studentdocs";

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
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }

        // Checking if student is logged in.
        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        User user = (User) request.getSession().getAttribute("user");

        // Getting the internship Id from the hidden form field.
        String idParam = request.getParameter("internship_id");
        if (idParam == null || idParam.trim().isEmpty()) {
            request.setAttribute("error", "Internship ID is missing.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }
        int internshipId;
        try {
            internshipId = Integer.parseInt(idParam);
        } catch (NumberFormatException ex) {
            request.setAttribute("error", "Invalid internship ID.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }

        // Processing document uploads.
        String cvUrl = null;
        String transcriptUrl = null;

        // for redirecting back to the page that made the request
        String referer = request.getHeader("Referer");
        
        // Process CV upload (optional)
        Part cvPart = request.getPart("cv");
        if (cvPart != null && cvPart.getSize() > 0) {
            String cvFileName = Paths.get(cvPart.getSubmittedFileName()).getFileName().toString();
            String cvExtension = cvFileName.substring(cvFileName.lastIndexOf(".") + 1).toLowerCase();
            // Validating allowed extensions (pdf, doc, docx)
            if (!(cvExtension.equals("pdf") || cvExtension.equals("doc") || cvExtension.equals("docx"))) {
                request.setAttribute("error", "Invalid CV format. Allowed formats: PDF, DOC, DOCX.");

                // Redirect back to the referring page or a default page.
                if (referer == null || referer.isEmpty()) {
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                } else {
                    request.getRequestDispatcher(referer).forward(request, response);
                }
                return;
            }
            String uniqueCVName = java.util.UUID.randomUUID().toString() + "." + cvExtension;
            File cvDir = new File(STUDENT_DIRECTORY);
            if (!cvDir.exists()) {
                cvDir.mkdirs();
            }
            String cvPath = STUDENT_DIRECTORY + File.separator + uniqueCVName;
            cvPart.write(cvPath);
            cvUrl = "uploads/studentdocs/" + uniqueCVName; // Use relative URL for your web app.
        }

        // Process Transcript upload (required)
        Part transcriptPart = request.getPart("transcript");
        if (transcriptPart != null && transcriptPart.getSize() > 0) {
            String transcriptFileName = Paths.get(transcriptPart.getSubmittedFileName()).getFileName().toString();
            String transcriptExtension = transcriptFileName.substring(transcriptFileName.lastIndexOf(".") + 1).toLowerCase();
            // accepting only PDF format transcripts
            if (!transcriptExtension.equals("pdf")) {
                request.setAttribute("error", "Transcript must be in PDF format.");
                request.getRequestDispatcher(referer).forward(request, response);
                return;
            }
            String uniqueTranscriptName = java.util.UUID.randomUUID().toString() + "." + transcriptExtension;
            File transcriptDir = new File(STUDENT_DIRECTORY);
            if (!transcriptDir.exists()) {
                transcriptDir.mkdirs();
            }
            String transcriptPath = STUDENT_DIRECTORY + File.separator + uniqueTranscriptName;
            transcriptPart.write(transcriptPath);
            transcriptUrl = "uploads/studentdocs/" + uniqueTranscriptName;
        } else {
            request.setAttribute("error", "Transcript is required.");

            // Redirect back to the referring page or a default page.
            if (referer == null || referer.isEmpty()) {
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher(referer).forward(request, response);
            }
            return;
        }

        Application application = new Application();
        application.setStudentId(user.getUserId());
        application.setInternshipId(internshipId);
        application.setCvUrl(cvUrl);
        application.setTranscriptUrl(transcriptUrl);
        // application_date and status are set automatically in the database
        // application.setApplicationDate(LocalDateTime.now());
        // application.setStatus("pending");

        try {
            ApplicationDAO applicationDAO = new ApplicationDAO(conn);
            int newApplicationId = applicationDAO.insertApplication(application);

            request.setAttribute("success", "Application submitted successfully with ID: " + newApplicationId);
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (ServletException | IOException | SQLException ex) {
            request.setAttribute("error", "Failed to submit application: " + ex.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
