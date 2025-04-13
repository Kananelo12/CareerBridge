<%-- 
    Document   : EmployerDashboard
    Created on : Apr 9, 2025, 1:12:14 AM
    Author     : kanan
--%>

<%@page import="model.Internship"%>
<%@page import="model.Company"%>
<%@page import="dao.InternshipDAO"%>
<%@page import="utils.ConnectionFile"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.util.List"%>
<%@page import="model.User"%>
<%@page import="dao.UserDAO"%>
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
    
    Connection conn = ConnectionFile.getConn();
    
    Company company = (Company) session.getAttribute("company");
    int companyId = company.getCompanyId();

    UserDAO userDAO = new UserDAO(conn);
    // retrieve only interns (students with internships).
    List<User> internUsers = userDAO.getInternUsersByCompany(companyId);
    request.setAttribute("internUsers", internUsers);

    InternshipDAO internshipDAO = new InternshipDAO(conn);
    List<Internship> internships = internshipDAO.getInternshipsByCompanyId(companyId);
    request.setAttribute("internships", internships);

    String messageClass = "";
    if (request.getAttribute("error") != null || request.getAttribute("success") != null) {
        messageClass = "active";
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="viewport" content="width=device-width, intial-scale=1.0" />
        <title>Employer Dashboard | Career Bridge</title>

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
                <%@include file="./nav-files/employerSidebar.jsp" %>
            </div>
            <div class="dashboard-right-section">
                <section class="section" id="overviewSection">
                    <div class="card-w-100" style="padding: 1.5rem; border-radius: 12px;">
                        <div class="content">
                            <h2 class="dashboard-role">Welcome, ${user.roleName}</h2>
                            <p class="content-text">Start your day with managing your interns</p>
                        </div>

                        <div class="card-container flex__between">
                            <div class="card-item">
                                <div class="item-title">
                                    <img src="./assets/images/calendar-check.png" alt="Card Icon" />
                                    <span>94</span>
                                </div>
                                <p class="item-text">Total number of  scheduled appointments</p>
                            </div>
                            <div class="card-item">
                                <div class="item-title">
                                    <img src="./assets/images/hourglass.png" alt="Card Icon" />
                                    <span>32</span>
                                </div>
                                <p class="item-text">Total number of  scheduled appointments</p>
                            </div>
                            <div class="card-item">
                                <div class="item-title">
                                    <img src="./assets/images/alert-triangle.png" alt="Card Icon" />
                                    <span>56</span>
                                </div>
                                <p class="item-text">Total number of  scheduled appointments</p>
                            </div>
                        </div>

                        <div class="container">
                            <div class="responsive-table">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>#ID</th>
                                            <th>Intern</th>
                                            <th>Email</th>
                                            <th>Phone</th>
                                            <th>Address</th>
                                            <th class="actions">Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="internRecord" items="${internUsers}">
                                            <tr>
                                                <td>${internRecord.userId}</td>
                                                <c:choose>
                                                    <c:when test="${not empty internRecord.userDetails.profileImageUrl}">
                                                        <td class="image-column">
                                                            <div class="table-img-container">
                                                                <img src="${internRecord.userDetails.profileImageUrl}" class="table-img" />
                                                            </div> 
                                                            ${internRecord.userDetails.firstName} ${internRecord.userDetails.lastName}
                                                        </td>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:set var="fName" value="${fn:toUpperCase(fn:substring(internRecord.userDetails.firstName, 0, 1))}" />
                                                        <c:set var="lName" value="${fn:toUpperCase(fn:substring(internRecord.userDetails.lastName, 0, 1))}" />

                                                        <td class="image-column">
                                                            <div class="profile__initials">
                                                                ${fName}${lName}
                                                            </div> ${internRecord.userDetails.firstName} ${internRecord.userDetails.lastName}
                                                        </td>
                                                    </c:otherwise>
                                                </c:choose>
                                                <td>${internRecord.userDetails.email}</td>
                                                <td>${internRecord.userDetails.phoneNumber}</td>
                                                <td>${internRecord.userDetails.address}</td>
                                                <td class="actions">
                                                    <a href="editUser.jsp?userId=${internRecord.userId}" class="btn edit-btn">Edit</a>
                                                    <a href="deleteUser?userId=${internRecord.userId}" class="btn delete-btn" 
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
                    </div>
                </section>
                <section class="section hidden" id="inboxSection">
                    Inbox
                </section>
                <section class="section hidden" id="internshipSection">
                    <div class="card-dark card-w-100" style="margin: 0 1rem 1.5rem 1rem;">
                        <a href="#" class="internship-btn">Post Internship</a>
                    </div>

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
                                        <th class="actions">Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="internship" items="${internships}">
                                        <tr>
                                            <td>${internship.internshipId}</td>
                                            <td>${company.companyName}</td>
                                            <td>${internship.category}</td>
                                            <td>${internship.location}</td>
                                            <td>${internship.postedDate}</td>
                                            <td>${internship.requirements}</td>
                                            <td>${internship.studentId}</td>
                                            <td class="badge">${internship.status}</td>
                                            <td class="actions">
                                                <a href="EditInternshipServlet?id=${internship.internshipId}" class="btn edit-btn">Edit</a>
                                                <a href="EditInternshipServlet?action=delete&id=${internship.internshipId}" class="btn delete-btn" 
                                                   onclick="return confirm('Are you sure you want to delete this internship?');">
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
                <section class="section hidden" id="applicationSection">
<!--                    <div class="card-dark card-w-100" style="margin: 0 1rem 1.5rem 1rem;">
                        <a href="#" class="internship-btn">Post Internship</a>
                    </div>-->

                    <div class="container">
                        <h2 class="table-title">Manage Applications</h2>
                        <div class="responsive-table">
                            <table>
                                <thead>
                                    <tr>
                                        <th>#ID</th>
                                        <th>Student</th>
                                        <th>Company</th>
                                        <th>CV</th>
                                        <th>Transcript</th>
                                        <th>Application</th>
                                        <th>Status</th>
                                        <th class="actions">Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="internship" items="${internships}">
                                        <tr>
                                            <td>${internship.internshipId}</td>
                                            <td>${company.companyName}</td>
                                            <td>${internship.category}</td>
                                            <td>${internship.location}</td>
                                            <td>${internship.postedDate}</td>
                                            <td>${internship.requirements}</td>
                                            <td>INTERN NAME</td>
                                            <td class="badge">${internship.status}</td>
                                            <td class="actions">
                                                <a href="editInternship.jsp?id=${internship.internshipId}" class="btn edit-btn">Edit</a>
                                                <a href="deleteInternship?id=${internship.internshipId}" class="btn delete-btn" 
                                                   onclick="return confirm('Are you sure you want to delete this internship?');">
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
                <section class="section hidden" id="companySection">
                    Company Profile
                </section>

            </div>
        </main>

        <!--=======Internship Modal =======-->
        <div class="modal__overlay flex" id="internship-modal">
            <div class="modal internship__modal" style="max-width: 600px;">
                <form action="InternshipServlet" method="POST" class="global-form">
                    <div class="modal__title flex" style="margin-bottom: 1.3rem;">
                        <h3 class="form-title">Enter internship details</h3>
                        <div class="close__modal__btn flex">
                            <i class="fas fa-close"></i>
                        </div>
                    </div>

                    <div class="modal__body">
                        <div class="input-group-flex">
                            <div class="grid-input-group">
                                <label for="title" class="form-label">Internship Title</label>
                                <input
                                    type="text"
                                    class="form-control"
                                    name="title"
                                    id="title"
                                    placeholder="Mobile Developer.."
                                    />
                            </div>
                            <div class="grid-input-group">
                                <label for="category" class="form-label">Category</label>
                                <select name="category" id="category" class="form-control">
                                    <option value="">Select category</option>
                                    <option value="engineering">Engineering</option>
                                    <option value="information technology">Information Technology</option>
                                    <option value="marketing">Marketing</option>
                                    <option value="finance">Finance</option>
                                    <option value="human resources">Human Resources</option>
                                    <option value="design">Design</option>
                                    <option value="sales">Sales</option>
                                    <option value="operations">Operations</option>
                                </select>
                            </div>
                        </div>
                        <div class="input-group">
                            <label for="location" class="form-label">Location</label>
                            <input
                                type="text"
                                class="form-control"
                                name="location"
                                id="location"
                                placeholder="Downtown, Maseru.."
                                />
                        </div>
                        <div class="input-group">
                            <label for="description" class="form-label">Internship Description</label>
                            <textarea class="form-control" name="description" id="description"></textarea>
                        </div>
                        <div class="input-group">
                            <label for="requirements" class="form-label">Internship Requirements</label>
                            <textarea class="form-control" name="requirements" id="requirements"></textarea>
                        </div>
                        <button type="submit" class="globalBtn submit-internship">
                            Post Internship
                        </button>
                    </div>
                </form>
            </div>
        </div>

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

            const internshipBtn = document.querySelector(".internship-btn");
            const internshipModal = document.getElementById("internship-modal");

            internshipBtn.addEventListener("click", (e) => {
                e.preventDefault();
                internshipModal.classList.add("active");

            });
        </script>
        <script src="./assets/js/utilities.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script src="./assets/js/customChart.js"></script>
    </body>
</html>
