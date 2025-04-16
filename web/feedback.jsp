<%-- 
    Document   : feedback
    Created on : Apr 15, 2025, 6:47:00 PM
    Author     : kanan
--%>

<%@page import="utils.ConnectionFile"%>
<%@page import="java.sql.Connection"%>
<%@page import="model.Internship"%>
<%@page import="service.InternshipService"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String savedTheme = "";
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if ("saved-theme".equals(cookie.getName())) {
                savedTheme = cookie.getValue();
                break;
            }
        }
    }

    Connection conn = ConnectionFile.getConn();

    // Retrieve parameters from the URL
    int internshipId = Integer.parseInt(request.getParameter("internship_id"));
    String studId = request.getParameter("studId");

    InternshipService internshipService = new InternshipService(conn);
    Internship internship = internshipService.getInternshipById(internshipId);

    if (internship == null) {
        request.setAttribute("error", "Internship not found.");
        request.getRequestDispatcher("feedback.jsp").forward(request, response);
        return;
    }
    // Set object in the request scope
    request.setAttribute("internship", internship);
    request.setAttribute("studentId", studId);

    String messageClass = "";
    if (request.getAttribute("error") != null || request.getAttribute("success") != null) {
        messageClass = "active";
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Review Internship Experience</title>

        <!-- ======= CSS Styles ======= -->
        <link rel="stylesheet" href="./assets/css/all.min.css" />
        <link rel="stylesheet" href="./assets/css/fontawesome.min.css" />
        <link rel="stylesheet" href="./assets/css/common.css" />
        <link rel="stylesheet" href="./assets/css/main-styles.css" />

        <!-- ======= Favicon ======= -->
        <link rel="apple-touch-icon" sizes="180x180" href="./assets/images/favicon/apple-touch-icon.png">
        <link rel="icon" type="image/png" sizes="32x32" href="./assets/images/favicon/favicon-32x32.png">
        <link rel="icon" type="image/png" sizes="16x16" href="./assets/images/favicon/favicon-16x16.png">
        <link rel="manifest" href="./assets/images/favicon/site.webmanifest">

        <style>
            section {
                background: var(--bg-color-02);
                width: 100%;
                height: 100vh;
            }
            .container {
                box-shadow: var(--shadow-custom);
            }
        </style>
    </head>
    <body class="<%=savedTheme%>">
        <!-- ======= Message Box ======= -->
        <div class="message__wrapper <%=messageClass%>">
            <div class="message__box">
                <div class="close__msg__btn flex">
                    <i class="fas fa-close"></i>
                </div>
                <div class="message">
                    <% if (request.getAttribute("error") != null) {%>
                    <div class="alert-danger">
                        <%= request.getAttribute("error")%>
                    </div>
                    <c:if test="${error eq 'The provided email already exist.'}">
                        <div class="login">
                            <a href="./login.jsp">Login</a>
                        </div>
                    </c:if>
                    <% } %>
                    <% if (request.getAttribute("success") != null) {%>
                    <div class="alert-success">
                        <%= request.getAttribute("success")%>
                    </div>
                    <% }%>
                </div>
            </div>
        </div>

        <%@include file="./nav-files/header.jsp" %>
        <section class="flex">
            <div class="modal container" style="max-width: 600px;">
                <h2 class="form-title" style="margin-bottom: 0.5rem;">Review Your Internship Experience</h2>

                <form action="FeedbackServlet" method="POST" class="global-form">
                    <!-- Pass the internship ID ans Student ID as hidden fields -->
                    <input type="hidden" name="internship_id" value="${internship.internshipId}" />
                    <input type="hidden" name="student_id" value="${studentId}" />

                    <div class="input-group">
                        <label for="title" class="form-label">Internship Title</label>
                        <input type="text" name="title" id="title" class="form-control" value="${internship.title}" disabled="" />
                    </div>
                    <div class="input-group">
                        <label for="rating" class="form-label">Internship Ratings</label>
                        <select name="rating" id="rating" class="form-control" required>
                            <option value="">Select a rating</option>
                            <option value="1">1 - Very Poor</option>
                            <option value="2">2 - Poor</option>
                            <option value="3">3 - Average</option>
                            <option value="4">4 - Good</option>
                            <option value="5">5 - Excellent</option>
                        </select>
                    </div>
                    <div class="input-group">
                        <label for="comments" class="form-label">Internship Description</label>
                        <textarea name="comments" id="comments" class="form-control" placeholder="Share your experience..."></textarea>
                    </div>

                    <button type="submit" class="globalBtn">Submit Feedback</button>
                </form>
            </div>
        </section>


        <script src="./assets/js/utilities.js"></script>
        <script>
            const dropdownBtn = document.querySelector(".dropdown-btn");
            const dropdown = document.querySelector(".dropdown");

            // Show dropdown when button is clicked
            dropdownBtn.addEventListener("click", (event) => {
                dropdown.classList.remove("hidden");
                // Prevent the click event from bubbling up to the document
                event.stopPropagation();
            });

            // Hide dropdown when clicking outside
            document.addEventListener("click", (event) => {
                if (!dropdown.contains(event.target) && !dropdownBtn.contains(event.target)) {
                    dropdown.classList.add("hidden");
                }
            });
        </script>
    </body>
</html>
