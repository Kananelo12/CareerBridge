<%-- 
    Document   : header
    Created on : Mar 25, 2025, 10:11:16 AM
    Author     : kanan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<header>
    <div class="logo">
        <img src="./assets/images/logo-main.png" alt="Website Logo" />
    </div>
    <nav class="navbar">
        <ul class="nav-items">
            <li><a href="./index.jsp" class="nav-item active">Home</a></li>
            <li><a href="#internships" class="nav-item">Internships</a></li>
            <li><a href="#" class="nav-item">About</a></li>
            <li><a href="#" class="nav-item">Services</a></li>
            <li><a href="#" class="nav-item">Contact</a></li>
        </ul>
    </nav>
    <c:choose>
        <c:when test="${not empty sessionScope.user}">
            <div class="profile">
                <div class="profile-pic">
                    <!-- Replace with an actual profile image if available -->
                    <img src="./${sessionScope.userDetails.profileImageUrl}" alt="Profile Picture" />
                </div>
                <div class="profile-info">
                    <span class="profile-email">${sessionScope.userDetails.firstName}</span>
                    <a href="LogoutServlet" id="logoutBtn">Logout</a>
                </div>
                <div id="moreBtn">
                    <span class="dropdown-btn"><i class="fas fa-caret-down"></i></span>
                    <div class="dropdown hidden">
                        <c:choose>
                            <c:when test="${sessionScope.user.roleName eq 'admin'}">
                                <a href="AdminDashboard.jsp">Dashboard</a>
                            </c:when>
                            <c:when test="${sessionScope.user.roleName eq 'employer'}">
                                <a href="EmployerDashboard.jsp">Dashboard</a>
                            </c:when>
                            <c:otherwise>
                                <a href="StudentDashboard.jsp">Dashboard</a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="nav-buttons">
                <a href="./register.jsp" class="nav-btn">Register</a>
                <a href="./login.jsp" class="nav-btn">Login</a>
            </div>
        </c:otherwise>
    </c:choose>
</header>
