<%-- 
    Document   : StudentDashboard
    Created on : Apr 9, 2025, 1:12:27 AM
    Author     : kanan
--%>

<%@page import="model.FeedbackReply"%>
<%@page import="dao.FeedbackReplyDAO"%>
<%@page import="model.Feedback"%>
<%@page import="dao.FeedbackDAO"%>
<%@page import="model.UserDetail"%>
<%@page import="model.User"%>
<%@page import="utils.ConnectionFile"%>
<%@page import="java.sql.Connection"%>
<%@page import="model.Company"%>
<%@page import="java.util.Map"%>
<%@page import="dao.ApplicationDAO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"  %>
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

    UserDetail userInfo = (UserDetail) session.getAttribute("userDetails");
    int studentId = userInfo.getUserId();

    List<Map<String, Object>> applications = new ApplicationDAO(conn).getApplicationsByStudentId(studentId);
    request.setAttribute("applications", applications);

    List<Map<String, Object>> feedbackDetails = new FeedbackDAO(conn).getFeedbackDetailsByStudentId(studentId);
    request.setAttribute("feedbackDetails", feedbackDetails);

    FeedbackReplyDAO replyDAO = new FeedbackReplyDAO(conn);
    Integer feedbackId = (Integer) session.getAttribute("feedbackId");

    if (feedbackId != null) {
        List<FeedbackReply> feedbackReplies = replyDAO.getRepliesByFeedbackId(feedbackId);
        request.setAttribute("feedbackReplies", feedbackReplies);
    } else {
        List<FeedbackReply> feedbackReplies = replyDAO.getAllReplies();
        request.setAttribute("feedbackReplies", feedbackReplies);
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
        <title>Student Dashboard | Career Bridge</title>

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
                <%@include file="./nav-files/studentSidebar.jsp" %>
            </div>
            <div class="dashboard-right-section">
                <section class="section overview" id="internshipSection">
                    <%@include file="./nav-files/internshipCards.jsp" %>
                </section>

                <section class="section hidden" id="applicationSection">
                    <div class="container">
                        <h2 class="table-title">Track My Applications</h2>
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
                                    <c:forEach var="row" items="${applications}">
                                        <c:set var="app" value="${row.application}" />
                                        <tr>
                                            <td>${app.applicationId}</td>
                                            <td>${row.studentName}</td>
                                            <td>${row.companyName}</td>
                                            <td><a href="${app.cvUrl}" target="_blank">View CV</a></td>
                                            <td><a href="${app.transcriptUrl}" target="_blank">View Transcript</a></td>
                                            <td>${app.applicationDate}</td>
                                            <td class="badge">${app.status}</td>
                                            <td class="actions">
                                                <c:choose>
                                                    <c:when test="${app.status eq 'pending' or app.status eq 'rejected'}">

                                                    </c:when>
                                                    <c:when test="${app.status eq 'completed'}">
                                                        <a href="feedback.jsp?appId=${app.applicationId}&studId=${app.studentId}&internship_id=${app.internshipId}" class="btn edit-btn">Review</a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="ApplicationServlet?action=complete&appId=${app.applicationId}&studId=${app.studentId}&internshipId=${app.internshipId}" class="btn edit-btn">Complete</a>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>

                                </tbody>
                            </table>
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
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="record" items="${feedbackDetails}">
                                        <tr>
                                            <td>${record.feedback.feedbackId}</td>
                                            <td>${record.student_name}</td>
                                            <td>${record.internship_title}</td>
                                            <td>${record.feedback.rating}</td>
                                            <td>${record.feedback.comments}</td>
                                            <td>${record.feedback.feedbackDate}</td>
                                        </tr>
                                    </c:forEach>

                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div class="container">
                        <h2 class="table-title">Replies to feedback</h2>
                        <div class="responsive-table">
                            <table>
                                <thead>
                                    <tr>
                                        <th>#ID</th>
                                        <th>Feedback ID</th>
                                        <th>Reply Text</th>
                                        <th>Reply Date</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="reply" items="${feedbackReplies}">
                                        <tr>
                                            <td>${reply.replyId}</td>
                                            <td>${reply.feedbackId}</td>
                                            <td>${reply.replyText}</td>
                                            <td>${reply.replyDate}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </section>

            </div>
        </main>

        <!--=======Application Modal =======-->
        <div class="modal__overlay flex" id="application-modal">
            <div class="modal application__modal" style="max-width: 600px;">
                <form action="ApplicationServlet" method="POST" class="global-form" enctype="multipart/form-data">
                    <div class="modal__title flex" style="margin-bottom: 1.3rem;">
                        <h3 class="form-title">Apply for Internship</h3>
                        <div class="close__modal__btn flex">
                            <i class="fas fa-close"></i>
                        </div>
                    </div>
                    <div class="modal__body">
                        <input type="hidden" name="internship_id" id="internship_id" value="${internshipCard.internshipId}" />

                        <div class="input-group">
                            <label for="cv" class="form-label">Upload Your CV</label>
                            <input type="file" name="cv" id="cv" class="form-control" accept=".pdf" required />
                        </div>
                        <div class="input-group">
                            <label for="transcript" class="form-label">Upload Your Transcript (PDF)</label>
                            <input type="file" name="transcript" id="transcript" class="form-control" accept=".pdf" required />
                        </div>
                        <button type="submit" class="globalBtn submit-application">
                            Apply Now
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <script src="./assets/js/utilities.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script src="./assets/js/customChart.js"></script>
        <script>
            const navItems = document.querySelectorAll(".nav__item");
            navItems.forEach((navItem) => {
                navItem.addEventListener("click", function () {
                    navItems.forEach(item => item.classList.remove("active"));
                    this.classList.add("active");
                });
            });

            const internshipIdInput = document.getElementById('internship_id');


            const applyButtons = document.querySelectorAll(".grid__btn");
            const applicationModal = document.getElementById("application-modal");
            applyButtons.forEach((appBtn) => {
                appBtn.addEventListener("click", (e) => {
                    e.preventDefault();
                    const internshipId = appBtn.getAttribute('data-id');
                    internshipIdInput.value = internshipId;
                    applicationModal.classList.add("active");
                });
            });

            // hide the modal overlay
            const modal = document.querySelector(".modal__overlay");
            const closeModalBtn = document.querySelector(".close__modal__btn");
            closeModalBtn.addEventListener("click", () => {
                modal.classList.remove("active");
            });
        </script>

    </body>
</html>
