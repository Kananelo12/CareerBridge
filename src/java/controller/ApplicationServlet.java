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
import javax.mail.MessagingException;
import model.Application;
import model.Internship;
import model.User;
import model.UserDetail;
import service.ApplicationService;
import service.InternshipService;
import service.UserService;
import utils.ConnectionFile;
import utils.NotificationService;

/**
 *
 * @author kanan
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB threshold before writing to disk
        maxFileSize = 1024 * 1024 * 10, // Max file size: 10MB
        maxRequestSize = 1024 * 1024 * 50 // Max request size: 50MB
)

public class ApplicationServlet extends HttpServlet {

    private Connection conn;

    // directory path for student docs
    private String TEMP_STUDENT_DIRECTORY;
    private String finalStudentPath;

    @Override
    public void init() {
        // Relative paths for images and documents
        TEMP_STUDENT_DIRECTORY = getServletContext().getRealPath("/uploads/studentdocs");
        // Extract 'build' from path, to target project root directory
        finalStudentPath = TEMP_STUDENT_DIRECTORY.replace("build" + File.separator, "");

        try {
            conn = ConnectionFile.getConn();
            System.out.println("STUDENT Directory: " + TEMP_STUDENT_DIRECTORY);
            System.out.println("FINAL STUDENT Directory: " + finalStudentPath);
        } catch (Exception ex) {
            System.err.println("Connection Failed: " + ex.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (conn == null) {
            request.setAttribute("error", "Database connection failed. Try again!");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }

        User user = (User) request.getSession().getAttribute("user");

        String studentIdParam = request.getParameter("studId");
        String idParam = request.getParameter("appId");
        if (idParam == null || idParam.isEmpty()) {
            request.setAttribute("error", "No Application ID provided.");
            response.sendRedirect("EmployerDashboard.jsp");
            return;
        }

        String action = request.getParameter("action");
        String internshipIdParam = request.getParameter("internshipId");

        try {
            int appId = Integer.parseInt(idParam);
            int studentId = Integer.parseInt(studentIdParam);
            int internshipId = Integer.parseInt(internshipIdParam);

            // Get email of applicant
            UserService userService = new UserService(conn);
            UserDetail studeUser = userService.getUserDetails(studentId);

            // Building the email params
            String recipientEmail = studeUser.getEmail();
            String subject = "Internship Application Follow Up";
            String emailBody;

            ApplicationService appService = new ApplicationService(conn);

            // Get current application
            Application app = appService.getApplicationById(appId);
            if (app == null) {
                request.setAttribute("error", "Application not found!");
            } else {
                if ("reject".equalsIgnoreCase(action)) {
                    // Update status to "rejected"
                    app.setStatus("rejected");
                    request.setAttribute("error", "Application has been rejected!");
                    emailBody = "Regratably, Your Application has been rejected!\n\nAutomated System\nApplication ID: " + appId + "\nCareerBridge";
                } else if ("complete".equalsIgnoreCase(action)) {
                    // Update status to "completed"
                    app.setStatus("completed");
                    request.setAttribute("success", "Congratulations\nInternship has been completed!");
                    emailBody = "Congratulations, Your Internship period has been completed. Welldone on reaching this far!\n\nAutomated System\nApplication ID: " + appId + "\nCareerBridge";

                    appService.updateApplication(app);
                    NotificationService.sendEmail(recipientEmail, subject, emailBody);

                    request.getRequestDispatcher("StudentDashboard.jsp").forward(request, response);
                    return;
                } else {
                    // Otherwise, update status to "rejected"
                    InternshipService internService = new InternshipService(conn);
                    Internship internship = new Internship();

                    app.setStatus("accepted");

                    // Update internships with accepted users
                    internship.setStudentId(studentId);
                    internship.setInternshipId(internshipId);

                    internService.updateIntern(internship);
                    request.setAttribute("success", "Application accepted successfully.");
                    emailBody = "Congratulations, Your Application has been accepted.\nApplication ID: " + appId + "\n\nAutomated System\nCareerBridge";
                }

                appService.updateApplication(app);
                NotificationService.sendEmail(recipientEmail, subject, emailBody);

            }
        } catch (NumberFormatException | SQLException | MessagingException ex) {
            request.setAttribute("error", "Failed to update application: " + ex.getMessage());
        }

        request.getRequestDispatcher("EmployerDashboard.jsp").forward(request, response);
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
        
        // Check if student has already applied for this internship
        try {
            ApplicationDAO applicationDAO = new ApplicationDAO(conn);
            if (applicationDAO.hasApplied(user.getUserId(), internshipId)) {
                request.setAttribute("error", "You have already applied for this internship!");
                request.getRequestDispatcher("StudentDashboard.jsp").forward(request, response);
                return;
            }
        } catch (ServletException | IOException | SQLException ex) {
            request.setAttribute("error", "Error: " + ex.getMessage());
            request.getRequestDispatcher("StudentDashboard.jsp").forward(request, response);
        }

        // Processing document uploads.
        String cvUrl = null;
        String transcriptUrl = null;

        // for redirecting back to the page that made the request
        String referer = request.getHeader("Referer");

        // Process CV upload
        Part cvPart = request.getPart("cv");
        if (cvPart != null && cvPart.getSize() > 0) {
            String cvFileName = Paths.get(cvPart.getSubmittedFileName()).getFileName().toString();
            String cvExtension = cvFileName.substring(cvFileName.lastIndexOf(".") + 1).toLowerCase();
            // Validating allowed extensions (pdf, doc, docx)
            if (!cvExtension.equals("pdf")) {
                request.setAttribute("error", "Invalid CV format. Only PDF format is allowed");

                // Redirect back to the referring page or a default page.
                if (referer == null || referer.isEmpty()) {
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                } else {
                    request.getRequestDispatcher(referer).forward(request, response);
                }
                return;
            }
            String uniqueCVName = java.util.UUID.randomUUID().toString() + "." + cvExtension;
            File cvDir = new File(finalStudentPath);
            if (!cvDir.exists()) {
                cvDir.mkdirs();
            }
            String cvPath = finalStudentPath + File.separator + uniqueCVName;
            cvPart.write(cvPath);
            cvUrl = "uploads/studentdocs/" + uniqueCVName;
        }

        // Process Transcript upload
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
            File transcriptDir = new File(finalStudentPath);
            if (!transcriptDir.exists()) {
                transcriptDir.mkdirs();
            }
            String transcriptPath = finalStudentPath + File.separator + uniqueTranscriptName;
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

            
        // Set application data
        Application application = new Application();
        application.setStudentId(user.getUserId());
        application.setInternshipId(internshipId);
        application.setCvUrl(cvUrl);
        application.setTranscriptUrl(transcriptUrl);

        // Building the email params
        String recipientEmail = user.getEmail();
        String subject = "Application for Internship";
        String emailBody = "Your application has been sent.\nWe will be in touch\n\nAutomated System\nCareerBridge";

        try {
            ApplicationDAO applicationDAO = new ApplicationDAO(conn);
            int newApplicationId = applicationDAO.insertApplication(application);
            

            request.setAttribute("success", "Application submitted successfully with ID: " + newApplicationId);
            request.getRequestDispatcher("StudentDashboard.jsp").forward(request, response);
            // Send email to applicant
            NotificationService.sendEmail(recipientEmail, subject, emailBody);
        } catch (ServletException | IOException | SQLException | MessagingException ex) {
            request.setAttribute("error", "Failed to submit application: " + ex.getMessage());
            request.getRequestDispatcher("StudentDashboard.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
