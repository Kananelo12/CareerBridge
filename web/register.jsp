<%-- 
    Document   : register
    Created on : Mar 30, 2025, 8:33:09 PM
    Author     : kanan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String messageClass = "";
    if (request.getAttribute("error") != null || request.getAttribute("success") != null) {
        messageClass = "active";
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
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>Registration | Lehae Connect</title>

        <!-- ======= CSS Styles ======= -->
        <link rel="stylesheet" href="./assets/css/all.min.css" />
        <link rel="stylesheet" href="./assets/css/fontawesome.min.css" />
        <link rel="stylesheet" href="./assets/css/common.css" />
        <link rel="stylesheet" href="./assets/css/register.css" />

        <!-- ======= Favicon ======= -->
        <link rel="apple-touch-icon" sizes="180x180" href="./assets/images/favicon/apple-touch-icon.png">
        <link rel="icon" type="image/png" sizes="32x32" href="./assets/images/favicon/favicon-32x32.png">
        <link rel="icon" type="image/png" sizes="16x16" href="./assets/images/favicon/favicon-16x16.png">
        <link rel="manifest" href="./assets/images/favicon/site.webmanifest">
    </head>
    <body class="<%=savedTheme%>">
        <!-- ======= Registration Link ======= -->
        <div class="login-link">
            <a href="./login.jsp" class="login-btn">Login</a>
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

                    <form action="RegisterServlet" method="POST" class="register-form" enctype="multipart/form-data">
                        <h3 class="form-section-title">Personal Information</h3>
                        <div class="input-group">
                            <label for="fullnames" class="form-label">Fullnames</label>
                            <input
                                type="text"
                                class="form-control"
                                name="fullnames"
                                id="fullnames"
                                placeholder="John Doe"
                                />
                        </div>
                        <div class="input-group-flex">
                            <div class="grid-input-group">
                                <label for="email" class="form-label">Email address</label>
                                <input
                                    type="email"
                                    class="form-control"
                                    name="email"
                                    id="email"
                                    placeholder="johndoe@gmail.com"
                                    />
                            </div>
                            <div class="grid-input-group">
                                <label for="phoneNumber" class="form-label">Phone Number</label>
                                <input
                                    type="text"
                                    class="form-control"
                                    name="phoneNumber"
                                    id="phoneNumber"
                                    placeholder="+266"
                                    />
                            </div>
                        </div>
                        <div class="input-group-flex">
                            <div class="grid-input-group">
                                <label for="address" class="form-label">Address</label>
                                <input
                                    type="text"
                                    class="form-control"
                                    name="address"
                                    id="address"
                                    placeholder="14 street, Ha-Abia, Maseru"
                                    />
                            </div>
                            <div class="grid-input-group">
                                <label for="roleType" class="form-label">Role</label>
                                <select name="roleType" id="roleType" class="form-control">
                                    <option value="student">Student</option>
                                    <option value="employer">Employer</option>
                                </select>
                            </div>
                        </div>
                        <div class="input-group">
                            <label for="password" class="form-label">Password</label>
                            <input
                                type="password"
                                class="form-control"
                                name="password"
                                id="password"
                                />
                        </div>
                        <div class="input-group file-picker">
                            <label for="profileImageUrl" class="form-label">Profile Image</label>
                            <input
                                type="file"
                                class="form-control"
                                name="profileImageUrl"
                                id="profileImageUrl"
                                accept=".jpg, .jpeg, .png"
                                />
                            <label for="profileImageUrl" class="custom-file-label">Choose File</label>
                        </div>

                        <div class="employer-group hidden">
                            <h3 class="form-section-title">Company Verification</h3>
                            <div class="input-group">
                                <label for="companyName" class="form-label">Company Name</label>
                                <input
                                    type="text"
                                    class="form-control"
                                    name="company_name"
                                    id="companyName"
                                    placeholder="Joel Holdings (PTY) LTD"
                                    />
                            </div>
                            <div class="input-group">
                                <label for="description" class="form-label">Company Description</label>
                                <textarea class="form-control" name="description" id="description"></textarea>
                            </div>
                            <div class="input-group-flex">
                                <div class="grid-input-group">
                                    <label for="industry" class="form-label">Industry</label>
                                    <select name="industry" id="industry" class="form-control">
                                        <option value="">-- Select Industry --</option>
                                        <option value="Information Technology">Information Technology</option>
                                        <option value="Finance & Banking">Finance & Banking</option>
                                        <option value="Healthcare & Pharmaceuticals">Healthcare & Pharmaceuticals</option>
                                        <option value="Education & Training">Education & Training</option>
                                        <option value="Telecommunications">Telecommunications</option>
                                        <option value="Legal Services">Legal Services</option>
                                        <option value="Marketing & Advertising">Marketing & Advertising</option>
                                        <option value="Hospitality & Tourism">Hospitality & Tourism</option>
                                    </select>
                                </div>
                                <div class="grid-input-group">
                                    <label for="website" class="form-label">Company Website</label>
                                    <input
                                        type="text"
                                        class="form-control"
                                        name="website"
                                        id="website"
                                        placeholder="www.joelholdings.com"
                                        />
                                </div>
                            </div>
                            <div class="input-group-flex">
                                <div class="grid-input-group file-picker">
                                    <label for="logoUrl" class="form-label">Company Logo</label>
                                    <input
                                        type="file"
                                        class="form-control"
                                        name="logo_url"
                                        id="logoUrl"
                                        accept=".png, .jpg, .jpeg"
                                        />
                                    <label for="logoUrl" class="custom-file-label">Choose File</label>
                                </div>
                                <div class="grid-input-group">
                                    <label for="location" class="form-label">Location</label>
                                    <input
                                        type="text"
                                        class="form-control"
                                        name="location"
                                        id="location"
                                        placeholder="14 street, Ha-Abia, Maseru"
                                        />
                                </div>
                            </div>
                            <div class="input-group">
                                <label for="contactInfo" class="form-label">Contact Information</label>
                                <input
                                    type="text"
                                    class="form-control"
                                    name="contact_info"
                                    id="contactInfo"
                                    placeholder="+266 .."
                                    />
                            </div>
                            <div class="input-group">
                                <label for="documentType" class="form-label">Document Type</label>
                                <select name="documentType" id="documentType" class="form-control">
                                    <option value="business_license">Business Registration Certificate</option>
                                    <option value="work_permit">Work Permit</option>
                                </select>
                            </div>
                            <div class="input-group file-picker">
                                <label for="documentUrl" class="form-label">Scanned Copy of Business License</label>
                                <input
                                    type="file"
                                    class="form-control"
                                    name="documentUrl"
                                    id="documentUrl"
                                    accept=".pdf"
                                    />
                                <label for="documentUrl" class="custom-file-label">Choose File</label>
                            </div>
                        </div>

                        <div class="check-group">
                            <label for="rememberMe" class="form-label">
                                <input
                                    type="checkbox"
                                    id="rememberMe"
                                    name="rememberMe"
                                    /> I acknowledge that I have reviewed and agree to the privacy policy
                            </label>
                        </div>
                        <button type="submit" class="get-started-btn">
                            Get Started
                        </button>
                    </form>

                    <div class="form-footer">
                        <p>&copy; 2025 CareerBridge</p>
                        <a href="https://joel-portfolio.web.app" class="dev-link" target="_blank">By Joel</a>
                    </div>
                </div>

                <div class="right-section"></div>
            </div>
        </div>

        <script src="./assets/js/common.js"></script>
        <script>
            // Check if a saved theme exists in localStorage
            const savedTheme = localStorage.getItem("saved-theme");
            const rightSection = document.querySelector(".right-section");
            if (savedTheme) {
                document.body.classList[savedTheme === "dark" ? "add" : "remove"]("dark-theme");
            }

            // show agent fields based on selected role type
            const roleSelect = document.getElementById("roleType");
            const employerGroup = document.querySelector(".employer-group");
            console.log(roleSelect);

            roleSelect.addEventListener("change", function () {
                // If the selected role is "agent", remove the hidden class; otherwise, add it.
                if (this.value === "employer") {
                    employerGroup.classList.remove("hidden");
                } else {
                    employerGroup.classList.add("hidden");
                }
            });
        </script>
    </body>
</html>
