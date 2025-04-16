<%-- 
    Document   : EditUser
    Created on : Apr 16, 2025, 9:21:32 PM
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
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Edit User Page | Career Bridge</title>

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
                <h2 class="form-title">Edit User Information</h2>

                <form action="EditUserServlet" method="POST" class="global-form">
                    <!-- Pass the internship ID as hidden field -->
                    <input type="hidden" name="studentId" value="${allUsers.userId}" />

                    <div class="input-group-flex">
                        <div class="grid-input-group">
                            <label for="firstName" class="form-label">Firstname</label>
                            <input type="text" name="firstName" id="firstName" class="form-control" value="${allUsers.firstName}" required />
                        </div>

                        <div class="grid-input-group">
                            <label for="lastName" class="form-label">Lastname</label>
                            <input type="text" name="lastName" id="lastName" class="form-control" value="${allUsers.lastName}" required />
                        </div>
                    </div>

                    <div class="input-group">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" name="email" id="email" class="form-control" value="${allUsers.email}" required />
                    </div>

                    <div class="input-group-flex">
                        <div class="grid-input-group">
                            <label for="phoneNumber" class="form-label">Phone Number</label>
                            <input type="text" name="phoneNumber" id="phoneNumber" class="form-control" value="${allUsers.phoneNumber}" required />
                        </div>

                        <div class="grid-input-group">
                            <label for="address" class="form-label">Address</label>
                            <input type="text" name="address" id="address" class="form-control" value="${allUsers.address}" required />
                        </div>
                    </div>
                    <button type="submit" class="globalBtn">Update User</button>
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
