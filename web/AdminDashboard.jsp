<%-- 
    Document   : AdminDashboard
    Created on : Apr 9, 2025, 1:11:58 AM
    Author     : kanan
--%>

<%@page import="java.util.Map"%>
<%@page import="dao.FeedbackDAO"%>
<%@page import="model.Internship"%>
<%@page import="dao.InternshipDAO"%>
<%@page import="model.Company"%>
<%@page import="utils.ConnectionFile"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.util.List"%>
<%@page import="model.User"%>
<%@page import="dao.UserDAO"%>
<%@page import="java.time.format.DateTimeFormatter" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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

    // Determine the current page from the request parameter
    int currentPage = 1;
    String pageParam = request.getParameter("page");
    if (pageParam != null) {
        try {
            currentPage = Integer.parseInt(pageParam);
        } catch (Exception ex) {
            currentPage = 1;
        }
    }

    Connection conn = ConnectionFile.getConn();

    UserDAO userDAO = new UserDAO(conn);
    List<User> allUsers = userDAO.getAllUsers();
    request.setAttribute("allUsers", allUsers);

    // retrieve only interns (students with internships).
    List<User> internUsers = userDAO.getInternUsers();
    request.setAttribute("internUsers", internUsers);

    // Offset the list from 0 and limit them to 6 items
    int limit = 6;
    int offset = (currentPage - 1) * limit;

    InternshipDAO internshipDAO = new InternshipDAO(conn);
    List<Internship> internships = internshipDAO.getInternships(offset, limit);
    request.setAttribute("internships", internships);
    request.setAttribute("currentPage", currentPage);
    request.setAttribute("limit", limit);

    FeedbackDAO feedbackDAO = new FeedbackDAO(conn);
    List<Map<String, Object>> allFeedback = feedbackDAO.getAllFeedbackDetails();
    request.setAttribute("allFeedback", allFeedback);

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
        <title>Admin Dashboard | Career Bridge</title>

        <!-- ======= CSS Styles ======= -->
        <link rel="stylesheet" href="./assets/css/all.min.css" />
        <link rel="stylesheet" href="./assets/css/fontawesome.min.css" />
        <link rel="stylesheet" href="./assets/css/common.css" />
        <link rel="stylesheet" href="./assets/css/table.css" />
        <link rel="stylesheet" href="./assets/css/sidebar.css" />

        <!-- ======= Favicon ======= -->
        <link rel="apple-touch-icon" sizes="180x180" href="./assets/images/favicon/apple-touch-icon.png">
        <link rel="icon" type="image/png" sizes="32x32" href="./assets/images/favicon/favicon-32x32.png">
        <link rel="icon" type="image/png" sizes="16x16" href="./assets/images/favicon/favicon-16x16.png">
        <link rel="manifest" href="./assets/images/favicon/site.webmanifest">
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


        <main class="dashboard">
            <div class="dashboard-left-section" id="sidebarContainer">
                <%@include file="./nav-files/sidebar.jsp" %>
            </div>
            <div class="dashboard-right-section">

                <!-- Card stats -->
                <section class="section" id="overviewSection">
                    <div class="card-w-100" style="padding:1.5rem; border-radius:12px;">
                        <!-- header, welcome, etc. -->

                        <div class="card-container flex__between">
                            <div class="card-item">
                                <div class="item-title">
                                    <img src="./assets/images/calendar-check.png" alt="Icon" />
                                    <c:choose>
                                        <c:when test="${not empty totalInternships}" >
                                            <span>${totalInternships}</span>
                                        </c:when>
                                        <c:otherwise>

                                            <span>EMPTY</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <p class="item-text">Total internships</p>
                            </div>
                            <div class="card-item">
                                <div class="item-title">
                                    <img src="./assets/images/hourglass.png" alt="Icon" />
                                    <span>${totalApplications}</span>
                                </div>
                                <p class="item-text">Total applications</p>
                            </div>
                            <div class="card-item">
                                <div class="item-title">
                                    <img src="./assets/images/hourglass.png" alt="Icon" />
                                    <span>${pendingApplications}</span>
                                </div>
                                <p class="item-text">Pending applications</p>
                            </div>
                        </div>

                        <!-- Chart -->
                        <div class="card-container bento-grid">
                            <div class="card-group flex__between">
                                <div class="card card-chart">
                                    <h3 class="card-title">Applications Last 7 Days</h3>
                                    <canvas id="statsChart"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>

                <section class="section hidden" id="usersSection">
                    <div class="container">
                        <h2 class="table-title">Manage Users</h2>
                        <div class="responsive-table">
                            <table>
                                <thead>
                                    <tr>
                                        <th>#ID</th>
                                        <th>Full Name</th>
                                        <th>Email</th>
                                        <th>Role</th>
                                        <th>Phone</th>
                                        <th>Address</th>
                                        <th class="actions">Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="userRecord" items="${allUsers}">
                                        <tr>
                                            <td>${userRecord.userId}</td>
                                            <c:choose>
                                                <c:when test="${not empty userRecord.userDetails.profileImageUrl}">
                                                    <td class="image-column">
                                                        <div class="table-img-container">
                                                            <img src="${userRecord.userDetails.profileImageUrl}" class="table-img" />
                                                        </div> 
                                                        ${userRecord.userDetails.firstName} ${userRecord.userDetails.lastName}
                                                    </td>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:set var="fName" value="${fn:toUpperCase(fn:substring(userRecord.userDetails.firstName, 0, 1))}" />
                                                    <c:set var="lName" value="${fn:toUpperCase(fn:substring(userRecord.userDetails.lastName, 0, 1))}" />

                                                    <td class="image-column">
                                                        <div class="profile__initials">
                                                            ${fName}${lName}
                                                        </div> ${userRecord.userDetails.firstName} ${userRecord.userDetails.lastName}
                                                    </td>
                                                </c:otherwise>
                                            </c:choose>
                                            <td>${userRecord.userDetails.email}</td>
                                            <td>${userRecord.roleName}</td>
                                            <td>${userRecord.userDetails.phoneNumber}</td>
                                            <td>${userRecord.userDetails.address}</td>
                                            <td class="actions">
                                                <a href="EditUserServlet?id=${userRecord.userId}" class="btn edit-btn">Edit</a>
                                                <a href="EditUserServlet?action=delete&id=${userRecord.userId}" class="btn delete-btn" 
                                                   onclick="return confirm('Are you sure you want to delete this user?');">
                                                    Delete
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </section>
                <section class="section hidden" id="internshipSection">
                    <div class="container">
                        <h2 class="table-title">Manage Internships</h2>
                        <div class="responsive-table">
                            <table>
                                <thead>
                                    <tr>
                                        <th>#ID</th>
                                        <th>Company Name</th>
                                        <th>Category</th>
                                        <th>Location</th>
                                        <th>Posted Date</th>
                                        <th>Requirements</th>
                                        <th>Taken By</th>
                                        <th>Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="internship" items="${internships}">
                                        <tr>
                                            <td>${internship.internshipId}</td>
                                            <td>${internship.company.companyName}</td>
                                            <td>${internship.category}</td>
                                            <td>${internship.location}</td>
                                            <td>${internship.postedDate}</td>
                                            <td>${internship.requirements}</td>
                                            <td>${internship.studentId}</td>
                                            <td class="badge">${internship.status}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        <div class="cards__pagination flex__between">
                            <!-- Previous Button: Enabled only if currentPage > 1 -->
                            <c:choose>
                                <c:when test="${currentPage > 1}">
                                    <a href="AdminDashboard.jsp?page=${currentPage - 1}" class="pagination__btn flex">
                                        <i class="fas fa-arrow-left"></i>
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <span class="pagination__btn flex disabled">
                                        <i class="fas fa-arrow-left"></i>
                                    </span>
                                </c:otherwise>
                            </c:choose>

                            <span class="page-number">Page ${currentPage}</span>

                            <!-- Next Button: If the number of records fetched equals the limit, assume more pages -->
                            <c:choose>
                                <c:when test="${fn:length(internships) == limit}">
                                    <a href="AdminDashboard.jsp?page=${currentPage + 1}" class="pagination__btn flex">
                                        <i class="fas fa-arrow-right"></i>
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <span class="pagination__btn flex disabled">
                                        <i class="fas fa-arrow-right"></i>
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </section>

                <section class="section hidden" id="feedbackSection">
                    <div class="container">
                        <h2 class="table-title">Feedback & Reviews</h2>
                        <div class="responsive-table">
                            <table>
                                <thead>
                                    <tr>
                                        <th>#ID</th>
                                        <th>Student</th>
                                        <th>Internship</th>
                                        <th>Rating</th>
                                        <th>Comments</th>
                                        <th>Date</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="row" items="${allFeedback}">
                                        <c:set var="fb" value="${row.feedback}" />
                                        <tr>
                                            <td>${fb.feedbackId}</td>
                                            <td>${row.studentName}</td>
                                            <td>${row.internshipTitle}</td>
                                            <td>${fb.rating}</td>
                                            <td>${fb.comments}</td>
                                            <td>${fb.feedbackDate}</td>
                                            <td>
                                                <a href="FeedbackServlet?action=delete&id=${fb.feedbackId}" class="btn delete-btn">Delete</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </section>

            </div>
        </main>

        <script>
            // Check if a saved theme exists in localStorage
            const savedTheme = localStorage.getItem("saved-theme");
            if (savedTheme) {
                document.body.classList[savedTheme === "dark" ? "add" : "remove"]("dark-theme");
            }

            const navItems = document.querySelectorAll(".nav__item");
            navItems.forEach((navItem) => {
                navItem.addEventListener("click", function () {
                    navItems.forEach(item => item.classList.remove("active"));
                    this.classList.add("active");
                });
            });
        </script>
        <script src="./assets/js/utilities.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script>
            const raw = [
            <c:forEach var="dc" items="${dailyAppCounts}" varStatus="st">
            { date: '${dc.date}', count: ${dc.count} }<c:if test="${!st.last}">,</c:if>
            </c:forEach>
            ];

            const labels = raw.map(r => r.date);
            const data = raw.map(r => r.count);

            const ctx = document.getElementById('statsChart').getContext('2d');
            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels,
                    datasets: [{
                            label: 'Applications',
                            data,
                            backgroundColor: '#00ff99',
                            borderRadius: 6,
                        }]
                },
                options: {
                    responsive: true,
                    plugins: {legend: {display: false}},
                    scales: {
                        y: {ticks: {beginAtZero: true}},
                    }
                }
            });
        </script>

    </body>
</html>
