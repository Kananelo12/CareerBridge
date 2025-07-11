package controller;

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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import model.Company;
import model.Document;
import model.User;
import model.UserDetail;
import service.UserService;
import utils.BCrypt;
import utils.ConnectionFile;

/**
 *
 * @author kanan
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB threshold before writing to disk
        maxFileSize = 1024 * 1024 * 10, // Max file size: 10MB
        maxRequestSize = 1024 * 1024 * 50 // Max request size: 50MB
)

public class RegisterServlet extends HttpServlet {

    private String TEMP_IMAGE_DIRECTORY;
    private String TEMP_DOCS_DIRECTORY;
    
    private String finalImgPath;
    private String finalDocPath;

    // Database connection variable
    private Connection conn;

    @Override
    public void init() throws ServletException {
        // Set relative paths for images and documents
        TEMP_IMAGE_DIRECTORY = getServletContext().getRealPath("/uploads/images");
        TEMP_DOCS_DIRECTORY = getServletContext().getRealPath("/uploads/documents");
        // Extract 'build' from the path to target project root directory
        finalImgPath = TEMP_IMAGE_DIRECTORY.replace("build" + File.separator, "");
        finalDocPath = TEMP_DOCS_DIRECTORY.replace("build" + File.separator, "");

        try {
            conn = ConnectionFile.getConn();
            System.out.println("TEMP Image Directory: " + TEMP_IMAGE_DIRECTORY);
            System.out.println("TEMP Documentary Directory: " + TEMP_DOCS_DIRECTORY);
            System.out.println("FINAL IMAGE Directory: " + finalImgPath);
            System.out.println("FINAL IMAGE Directory: " + finalDocPath);
        } catch (Exception ex) {
            System.out.println("Connection Failed: " + ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Ensure database connection is available
        if (conn == null) {
            request.setAttribute("error", "Database connection failed. Try again later.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // get and validate input params
        String fullnames = request.getParameter("fullnames");
        String email = request.getParameter("email");
        String passWd = request.getParameter("password");
        String phoneNumber = request.getParameter("phoneNumber");
        String address = request.getParameter("address");
        String roleType = request.getParameter("roleType");
        
        String dbImagePath = null;

        Part imageFilePart = request.getPart("profileImageUrl");
        if (imageFilePart != null && imageFilePart.getSize() > 0) {
            String imageName = Paths.get(imageFilePart.getSubmittedFileName()).getFileName().toString();
            String imageExtension = imageName.substring(imageName.lastIndexOf(".") + 1).toLowerCase();
            if (!(imageExtension.equals("jpg") || imageExtension.equals("jpeg") || imageExtension.equals("png"))) {
                request.setAttribute("error", "Invalid file format. Only JPG, JPEG, PNG allowed.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
            // Generate a unique file name using UUID
            String uniqueImageName = UUID.randomUUID().toString() + "." + imageExtension;

            // Create the uploads/images directory if it doesn't exist
            File uploadDir = new File(finalImgPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            // Save the file to disk
            String imagePath = finalImgPath + File.separator + uniqueImageName;
            imageFilePart.write(imagePath);
            // Image relative path
            dbImagePath = "uploads/images/" + uniqueImageName;
        }

        boolean hasErrors = false;
        String errorMessage = "";

        // Split fullnames into first and last names
        String firstName = "";
        String lastName = "";
        if (fullnames != null && !fullnames.trim().isEmpty()) {
            String[] nameParts = fullnames.trim().split(" ");
            if (nameParts.length < 2) {
                request.setAttribute("error", "Please provide both first and last names!");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            } else {
                firstName = nameParts[0];
                // Combine any remaining parts into the last name
                StringBuilder builder = new StringBuilder();
                for (int i = 1; i < nameParts.length; i++) {
                    builder.append(nameParts[i]).append(" ");
                }
                lastName = builder.toString().trim();
            }
        } else {
            request.setAttribute("error", "Full names is required!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Validate Email
        if (email == null || email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            hasErrors = true;
            errorMessage += "\nInvalid email format. ";
        }

        // Validate Password (at least 8 characters)
        if (passWd == null || passWd.length() < 8) {
            hasErrors = true;
            errorMessage += "\nPassword must be at least 8 characters long. ";
        }

        // Validate Phone Number (Only digits, at least 7 and at most 15 digits)
        if (phoneNumber == null || !phoneNumber.matches("^\\+266 ?\\d{8}$")) {
            hasErrors = true;
            errorMessage += "\nInvalid phone number format. Please use '+266 56565406' or '+26656565406'.. ";
        }

        // Validate Address (Non-empty)
        if (address == null || address.trim().isEmpty()) {
            hasErrors = true;
            errorMessage += "\nAddress cannot be empty. ";
        }

        // Validate Role Type (Only "guest" or "agent" allowed)
        if (roleType == null || (!roleType.equals("student") && !roleType.equals("employer"))) {
            hasErrors = true;
            errorMessage += "\nInvalid role type selected. ";
        }

        // If there are validation errors, send back an error response
        if (hasErrors) {
            request.setAttribute("error", errorMessage);
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Employer mode: registre company
        // document upload and validation
        Company company = null;
        Document document = null;
        if (roleType.equals("employer")) {
            String compName = request.getParameter("company_name");
            String compDescription = request.getParameter("description");
            String industry = request.getParameter("industry");
            String website = request.getParameter("website");
            Part logoFilePart = request.getPart("logo_url");
            String location = request.getParameter("location");
            String contactInfo = request.getParameter("contact_info");
            String documentType = request.getParameter("documentType");
            Part documentFilePart = request.getPart("documentUrl");
            String dbLogoPath = null;
            String dbDocPath = null;

            if (compName == null || compName.trim().isEmpty()) {
                request.setAttribute("error", "Please provide a company name!");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
            if (industry == null || industry.trim().isEmpty()) {
                request.setAttribute("error", "Please select an industry!");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
            if (location == null || location.trim().isEmpty()) {
                request.setAttribute("error", "Please specify the company's location.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
            if (contactInfo == null || contactInfo.trim().isEmpty()) {
                request.setAttribute("error", "Please provide the company's contact information!");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
            if (documentType == null || documentType.trim().isEmpty()) {
                request.setAttribute("error", "Please select a document type.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            // get the logo file and upload path
            if (logoFilePart != null && logoFilePart.getSize() > 0) {
                String logoName = Paths.get(logoFilePart.getSubmittedFileName()).getFileName().toString();
                String logoExtension = logoName.substring(logoName.lastIndexOf(".") + 1).toLowerCase();
                if (!(logoExtension.equals("jpg")
                        || logoExtension.equals("jpeg") || logoExtension.equals("png"))) {
                    request.setAttribute("error", "Invalid file format for logo. Allowed: JPG, JPEG, PNG.");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                    return;
                }
                String uniqueLogoName = UUID.randomUUID().toString() + "." + logoExtension;
                File logoUploadDir = new File(finalImgPath);
                if (!logoUploadDir.exists()) {
                    logoUploadDir.mkdirs();
                }
                String logoPath = finalImgPath + File.separator + uniqueLogoName;
                logoFilePart.write(logoPath);
                dbLogoPath = "uploads/images/" + uniqueLogoName;
            }

            // get the document file and upload path
            if (documentFilePart != null && documentFilePart.getSize() > 0) {
                String docName = Paths.get(documentFilePart.getSubmittedFileName()).getFileName().toString();
                String docExtension = docName.substring(docName.lastIndexOf(".") + 1).toLowerCase();
                if (!(docExtension.equals("pdf") || docExtension.equals("jpg")
                        || docExtension.equals("jpeg") || docExtension.equals("png"))) {
                    request.setAttribute("error", "Invalid file format for document. Allowed: PDF, JPG, JPEG, PNG.");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                    return;
                }
                String uniqueDocName = UUID.randomUUID().toString() + "." + docExtension;
                File docUploadDir = new File(finalDocPath);
                if (!docUploadDir.exists()) {
                    docUploadDir.mkdirs();
                }
                String docPath = finalDocPath + File.separator + uniqueDocName;
                documentFilePart.write(docPath);
                dbDocPath = "uploads/documents/" + uniqueDocName;
            }

            company = new Company();
            company.setCompanyName(compName);
            company.setDescription(compDescription);
            company.setIndustry(industry);
            company.setWebsite(website);
            company.setLogoUrl(dbLogoPath);
            company.setLocation(location);
            company.setContactInfo(contactInfo);

            document = new Document();
            document.setDocumentType(documentType);
            document.setDocumentUrl(dbDocPath);
        }

        // Hashing passwords with BCrypt Algorithm
        String hashedPassword = BCrypt.hashpw(passWd, BCrypt.gensalt());

        // Default role id is student
        int roleId = 3;

        // Determine actual role_id from database
        try {
            String roleQuery = "SELECT role_id FROM roles WHERE rolename = ?";
            PreparedStatement psRole = conn.prepareStatement(roleQuery);
            psRole.setString(1, roleType);
            ResultSet rsRole = psRole.executeQuery();
            if (rsRole.next()) {
                roleId = rsRole.getInt("role_id");
            } else {
                // if role is not found, default to student user
                roleId = 3;
            }
            psRole.close();

        } catch (SQLException ex) {
            request.setAttribute("error", "Error determining role: " + ex.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Create the User and UserDetails beans
        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setRoleId(roleId);

        UserDetail details = new UserDetail();
        details.setFirstName(firstName);
        details.setLastName(lastName);
        details.setEmail(email);
        details.setPhoneNumber(phoneNumber);
        details.setAddress(address);
        details.setProfileImageUrl(dbImagePath);

        // Using the service layer to perform the registration transaction.
        try {
            UserService userService = new UserService(conn);
            userService.registerUser(user, details, document, company);
            request.setAttribute("success", "Registration successful! Please log in.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (SQLException ex) {
            request.setAttribute("error", "Registration failed: " + ex.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
