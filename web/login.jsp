<%-- 
    Document   : login
    Created on : Mar 30, 2025, 11:06:44 AM
    Author     : kanan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    
    String modalClass = "";
    String passkeyClass = "hidden";
    String getCodeClass = "";
    if (request.getAttribute("active") != null) {
        modalClass = "active";
        passkeyClass = "";
        getCodeClass = "hidden";
    }
    String messageClass = "";
    if (request.getAttribute("error") != null || request.getAttribute("success") != null) {
        messageClass = "active";
    }

%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>Login to CareerBridge | Internship Management System</title>

        <!-- ======= CSS Styles ======= -->
        <link rel="stylesheet" href="./assets/css/all.min.css" />
        <link rel="stylesheet" href="./assets/css/fontawesome.min.css" />
        <link rel="stylesheet" href="./assets/css/common.css" />
        <link rel="stylesheet" href="./assets/css/auth.css" />
        
        <!-- ======= Favicon ======= -->
        <link rel="apple-touch-icon" sizes="180x180" href="./assets/images/favicon/apple-touch-icon.png">
        <link rel="icon" type="image/png" sizes="32x32" href="./assets/images/favicon/favicon-32x32.png">
        <link rel="icon" type="image/png" sizes="16x16" href="./assets/images/favicon/favicon-16x16.png">
        <link rel="manifest" href="./assets/images/favicon/site.webmanifest">
    </head>
    <body class="<%=savedTheme%>">
        <!-- ======= Admin Modal ======= -->
        <div class="verification-overlay flex <%=modalClass%>">
            <div class="verification-modal">
                <form action="LoginServlet" method="POST">
                    <input type="hidden" name="admin-passkey" value="verify-admin" />
                    <div class="modal-title flex">
                        <h4>Admin Access Verification</h4>
                        <div class="close-modal flex">
                            <i class="fas fa-close"></i>
                        </div>
                    </div>
                    <div class="get-code-fields <%=getCodeClass%>">
                        <p class="modal-subtitle email" id="email-title">We will send a code to your email.</p>
                    <div class="recipient-email">
                        <input type="email" name="adminEmail" id="adminEmail" placeholder="Enter your email.." />
                        <input type="submit" value="Get Code" class="get-code-btn" />
                    </div>
                    </div>
                    <div class="modal-fields <%=passkeyClass%>">
                        <p class="modal-subtitle">To access the admin page, please enter the passkey</p>

                    <div class="passkey-fields">
                        <input type="text" name="otp1" maxlength="1" class="otp-input" />
                        <input type="text" name="otp2" maxlength="1" class="otp-input" />
                        <input type="text" name="otp3" maxlength="1" class="otp-input" />
                        <input type="text" name="otp4" maxlength="1" class="otp-input" />
                        <input type="text" name="otp5" maxlength="1" class="otp-input" />
                        <input type="text" name="otp6" maxlength="1" class="otp-input" />
                    </div>

                    <button type="submit" class="verify-admin-btn">Verify Identity</button>
                    </div>
                </form>
            </div>
        </div>
        <!-- ======= Registration Link ======= -->
        <div class="register-link">
            <a href="./register.jsp" class="register-btn">Register</a>
        </div>
        <!-- ======= Message Box ======= -->
        <div class="message-wrapper <%=messageClass%>">
            <div class="message-box">
                <div class="close-btn flex">
                    <i class="fas fa-close"></i>
                </div>
                <div class="message">
                    <% if (request.getAttribute("error") != null) {%>
                    <div class="alert-danger">
                        <%= request.getAttribute("error")%>
                    </div>
                    <c:if test="${error eq 'Email does not exist.'}">
                        <div class="register">
                            <a href="./register.jsp">Register</a>
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

        <div class="container">
            <div class="section-wrapper">
                <!-- Left Section (Form) -->
                <div class="left-section">
                    <div class="logo">
                        <img src="./assets/images/logo-mark.png" alt="App Logo" />
                        <h1>CareerBridge</h1>
                    </div>
                    <h2 class="form-title">Hi there <span class="wave">👋</span></h2>
                    <p class="sub-title">Get started with your internship search.</p>

                    <form action="LoginServlet" method="POST" class="login-form">
                        <div class="input-group">
                            <label for="email" class="form-label">Email</label>
                            <input
                                type="email"
                                class="form-control"
                                name="email"
                                id="email"
                                placeholder="John Doe"
                                />
                        </div>
                        <div class="input-group">
                            <label for="password" class="form-label">Password</label>
                            <input
                                type="password"
                                class="form-control"
                                name="password"
                                id="password"
                                placeholder="johndoe@gmail.com"
                                />
                        </div>
                        <div class="check-group">
                            <label for="rememberMe" class="form-label">
                                <input
                                    type="checkbox"
                                    id="rememberMe"
                                    name="rememberMe"
                                    /> Remember me
                            </label>
                        </div>
                        <button type="submit" class="get-started-btn">
                            Get Started
                        </button>
                    </form>

                    <div class="form-footer">
                        <p>&copy; 2025 CareerBridge</p>
                        <a href="#" class="admin-link">Admin</a>
                    </div>
                </div>

                <div class="right-section"></div>
            </div>
        </div>

        <script src="./assets/js/common.js"></script>
        <script>
            // Check if a saved theme exists in localStorage
            const savedTheme = localStorage.getItem("saved-theme");
            if (savedTheme) {
                document.body.classList[savedTheme === "dark" ? "add" : "remove"]("dark-theme");
            }
        </script>
    </body>
</html>
