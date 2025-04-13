<%--
    Document   : EditInternship
    Created on : Apr 12, 2025, 3:12:01 PM
    Author     : kanan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    // protecting sensitive pages
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
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

    String messageClass = "";
    if (request.getAttribute("error") != null || request.getAttribute("success") != null) {
        messageClass = "active";
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

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
                <h2 class="form-title">Edit Internship</h2>

                <form action="EditInternshipServlet" method="POST" class="global-form">
                    <!-- Pass the internship ID as hidden field -->
                    <input type="hidden" name="internshipId" value="${internship.internshipId}" />

                    <div class="input-group">
                        <label for="title" class="form-label">Internship Title</label>
                        <input type="text" name="title" id="title" class="form-control" placeholder="Mobile Developer.." value="${internship.title}" required />
                    </div>

                    <div class="input-group-flex">
                        <div class="grid-input-group">
                        <label for="category" class="form-label">Category</label>
                        <select name="category" id="category" class="form-control" required>
                            <option value="">Select category</option>
                            <option value="engineering" <c:if test="${internship.category eq 'engineering'}">selected</c:if>>Engineering</option>
                            <option value="information technology" <c:if test="${internship.category eq 'information technology'}">selected</c:if>>Information Technology</option>
                            <option value="marketing" <c:if test="${internship.category eq 'marketing'}">selected</c:if>>Marketing</option>
                            <option value="finance" <c:if test="${internship.category eq 'finance'}">selected</c:if>>Finance</option>
                            <option value="human resources" <c:if test="${internship.category eq 'human resources'}">selected</c:if>>Human Resources</option>
                            <option value="design" <c:if test="${internship.category eq 'design'}">selected</c:if>>Design</option>
                            <option value="sales" <c:if test="${internship.category eq 'sales'}">selected</c:if>>Sales</option>
                            <option value="operations" <c:if test="${internship.category eq 'operations'}">selected</c:if>>Operations</option>
                            </select>
                        </div>

                        <div class="grid-input-group">
                            <label for="location" class="form-label">Location</label>
                            <input type="text" name="location" id="location" class="form-control" placeholder="Downtown, Maseru.." value="${internship.location}" required />
                        </div>
                    </div>

                        <div class="input-group">
                            <label for="description" class="form-label">Internship Description</label>
                            <textarea name="description" id="description" class="form-control" required>${internship.description}</textarea>
                        </div>

                        <div class="input-group">
                            <label for="requirements" class="form-label">Internship Requirements</label>
                            <textarea name="requirements" id="requirements" class="form-control" required>${internship.requirements}</textarea>
                        </div>
                        <div class="input-group">
                            <label for="status" class="form-label">Internship Status</label>
                            <select name="status" id="status" class="form-control" required>
                            <option value="">Select status</option>
                            <option value="open" <c:if test="${internship.status eq 'open'}">selected</c:if>>Open</option>
                            <option value="closed" <c:if test="${internship.status eq 'closed'}">selected</c:if>>Close</option>
                            </select>
                        </div>

                        <button type="submit" class="globalBtn">Update Internship</button>
                </form>
            </div>
        </section>

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
