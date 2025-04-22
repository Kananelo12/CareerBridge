<%-- 
    Document   : EmployerDashboard
    Created on : Apr 9, 2025, 1:12:14 AM
    Author     : kanan
--%>

<%@page import="model.FeedbackReply"%>
<%@page import="dao.FeedbackReplyDAO"%>
<%@page import="dao.FeedbackDAO"%>
<%@page import="java.util.Map"%>
<%@page import="dao.ApplicationDAO"%>
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

    List<Map<String, Object>> applications = new ApplicationDAO(conn).getApplicationsByCompanyId(companyId);
    request.setAttribute("applications", applications);

    List<Map<String, Object>> feedback = new FeedbackDAO(conn).getFeedbacksByCompanyId(companyId);
    request.setAttribute("feedbackDetails", feedback);

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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
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

        <style>
            .pdf {
                width: 100%;
                aspect-ratio: 4/3;
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
                                <%
                                    int internCount = internUsers.size();
                                    request.setAttribute("internUserCount", internCount);
                                %>
                                <div class="item-title">
                                    <img src="./assets/images/intern-student.png" alt="Card Icon" />
                                    <span>${internUserCount}</span>
                                </div>
                                <c:set var="companyName" value="${applications[0].companyName}" />
                                <p class="item-text">Total number of accepted interns at ${companyName}</p>
                            </div>
                            <div class="card-item">
                                <%
                                    int applicationCount = applications.size();
                                    request.setAttribute("applicationCount", applicationCount);
                                %>
                                <div class="item-title">
                                    <img src="./assets/images/hourglass.png" alt="Card Icon" />
                                    <span>${applicationCount}</span>
                                </div>
                                <p class="item-text">Total number of  pending applications</p>
                            </div>
                            <div class="card-item">
                                <%
                                    int internshipCount = internships.size();
                                    request.setAttribute("internshipCount", internshipCount);
                                %>
                                <div class="item-title">
                                    <img src="./assets/images/internship-count.png" alt="Card Icon" />
                                    <span>${internshipCount}</span>
                                </div>
                                <p class="item-text">Total number of internship opportunities posted</p>
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
                                                <td>${internRecord.email}</td>
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
                                    <c:forEach var="row" items="${applications}">
                                        <c:set var="app" value="${row.application}" />
                                        <tr>
                                            <td>${app.applicationId}</td>
                                            <td>${row.studentName}</td>
                                            <td>${row.companyName}</td>
                                            <td><a href="${app.cvUrl}" id="downloadBtn" data-file-url="${app.cvUrl}" target="_blank">View CV</a></td>
                                            <td><a href="${app.transcriptUrl}" id="downloadBtn" data-file-url="${app.transcriptUrl}" target="_blank">View Transcript</a></td>
                                            <td>${app.applicationDate}</td>
                                            <td class="badge">${app.status}</td>
                                            <td class="actions">
                                                <a href="ApplicationServlet?action=accept&appId=${app.applicationId}&studId=${app.studentId}&internshipId=${app.internshipId}" class="btn edit-btn">Accept</a>
                                                <a href="ApplicationServlet?action=reject&appId=${app.applicationId}&studId=${app.studentId}&internshipId=${app.internshipId}" class="btn delete-btn"
                                                   onclick="return confirm('Are you sure you want to reject this application?');">Reject</a>
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
                                        <th>Action</th>
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
                                            <td>
                                                <a href="" 
                                                   class="btn edit-btn replyButton"
                                                   data-feedback-id="${record.feedback.feedbackId}"
                                                   data-student-name="${record.student_name}">
                                                    <i class="fas fa-reply" ></i>
                                                </a>
                                            </td>
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
                <section class="section hidden" id="companySection">
                    <div class="container">
                        <h2 class="form-title" style="margin-bottom: 2rem;">Edit Company Profile</h2>

                        <form action="EditCompanyServlet" method="POST" class="global-form">
                            <!-- Hidden field for company_id -->
                            <input type="hidden" name="companyId" value="${company.companyId}" />

                            <div class="input-group">
                                <label for="companyName" class="form-label">Company Name</label>
                                <input type="text" name="companyName" id="companyName" class="form-control" value="${company.companyName}" required />
                            </div>

                            <div class="input-group">
                                <label for="description" class="form-label">Description</label>
                                <textarea name="description" id="description" class="form-control" rows="3">${company.description}</textarea>
                            </div>

                            <div class="input-group-flex">
                                <div class="grid-input-group">
                                    <label for="industry" class="form-label">Industry</label>
                                    <select name="industry" id="industry" class="form-control" required>
                                        <option value="Information Technology" ${company.industry == 'Information Technology' ? 'selected' : ''}>Information Technology</option>
                                        <option value="Finance & Banking" ${company.industry == 'Finance & Banking' ? 'selected' : ''}>Finance & Banking</option>
                                        <option value="Healthcare & Pharmaceuticals" ${company.industry == 'Healthcare & Pharmaceuticals' ? 'selected' : ''}>Healthcare & Pharmaceuticals</option>
                                        <option value="Education & Training" ${company.industry == 'Education & Training' ? 'selected' : ''}>Education & Training</option>
                                        <option value="Telecommunications" ${company.industry == 'Telecommunications' ? 'selected' : ''}>Telecommunications</option>
                                        <option value="Legal Services" ${company.industry == 'Legal Services' ? 'selected' : ''}>Legal Services</option>
                                        <option value="Marketing & Advertising" ${company.industry == 'Marketing & Advertising' ? 'selected' : ''}>Marketing & Advertising</option>
                                        <option value="Hospitality & Tourism" ${company.industry == 'Hospitality & Tourism' ? 'selected' : ''}>Hospitality & Tourism</option>
                                    </select>
                                </div>

                                <div class="grid-input-group">
                                    <label for="website" class="form-label">Website</label>
                                    <input type="url" name="website" id="website" class="form-control" value="${company.website}" />
                                </div>


                            </div>

                            <div class="input-group">
                                <label for="location" class="form-label">Location</label>
                                <input type="text" name="location" id="location" class="form-control" value="${company.location}" required />
                            </div>

                            <div class="input-group">
                                <label for="contactInfo" class="form-label">Contact Info</label>
                                <input type="text" name="contactInfo" id="contactInfo" class="form-control" value="${company.contactInfo}" required />
                            </div>
                            <div class="input-group">
                                <button type="submit" class="globalBtn" style="padding: 1rem 0; margin-top: .5rem;">Update Company</button>
                            </div>
                        </form>
                    </div>
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

        <!--======= Feedback Reply Modal =======-->
        <div class="modal__overlay flex" id="feedbackModal">
            <div class="modal feedback__modal" style="max-width: 600px;">
                <form action="ReplyFeedbackServlet" method="POST" class="global-form">
                    <input type="hidden" name="feedbackIdHdn" id="modalFeedbackId" />

                    <div class="modal__title flex" style="margin-bottom: 1.3rem;" id="closeFeedbackModal">
                        <h3 class="form-title" id="reply-title">Reply to student feedback</h3>
                        <div class="close__modal__btn flex">
                            <i class="fas fa-close"></i>
                        </div>
                    </div>
                    <div class="modal__body">
                        <div class="input-group">
                            <label for="replyText" class="form-label">Reply Comments</label>
                            <textarea class="form-control" name="replyText" id="replyText" placeholder="Type your reply…"></textarea>
                        </div>
                        <button type="submit" class="globalBtn">Reply</button>
                    </div>
                </form>
            </div>
        </div>

        <!--======= File Frame Modal =======-->
        <div class="modal__overlay flex" id="fileFrame">
            <div class="modal frame_modal" style="max-width: 1000px;">
                <div class="modal__title flex" style="margin-bottom: 1.3rem;" id="closeFileFrame">
                    <div class="close__modal__btn flex">
                        <i class="fas fa-close"></i>
                    </div>
                </div>

                <div class="modal__body">
                    <embed 
                        class="pdf" 
                        id="fileEmbed"
                        type="application/pdf"
                        />
                </div>
            </div>
        </div>


        <script>
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

            const replyButtons = document.querySelectorAll(".replyButton");
            const feedbackModal = document.getElementById("feedbackModal");
            const replyFeedbackIdField = document.getElementById("modalFeedbackId");
            const replyTitleField = document.getElementById("reply-title");
            const closeFeedbackBtn = document.getElementById("closeFeedbackModal");

            replyButtons.forEach((replyBtn) => {
                replyBtn.addEventListener("click", (e) => {
                    e.preventDefault();

                    const feedbackId = replyBtn.dataset.feedbackId;
                    const student = replyBtn.dataset.studentName;
                    replyFeedbackIdField.value = feedbackId;
                    replyTitleField.textContent = 'Reply to ' + student + '\'s feedback';
                    console.log("Student: " + student);
                    console.log("h3 element text: " + replyTitleField.textContent);

                    feedbackModal.classList.add("active");
                });
            });

            closeFeedbackBtn.addEventListener("click", () => {
                feedbackModal.classList.remove("active");
            });

            const downloadButtons = document.querySelectorAll("#downloadBtn");
            const closeFrameBtn = document.getElementById("closeFileFrame");
            const frame = document.getElementById("fileFrame");
            const fileModal = document.getElementById("fileEmbed");
            
            downloadButtons.forEach((downloadBtn) => {
                downloadBtn.addEventListener("click", (e) => {
                    e.preventDefault();
                    const fileUrl = downloadBtn.getAttribute("data-file-url");
                    fileModal.src = fileUrl;
                    frame.classList.add("active");
                });
            });

            closeFrameBtn.addEventListener("click", () => {
                frame.classList.remove("active");
                fileModal.src = "";
            });
        </script>
        <script src="./assets/js/utilities.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script src="./assets/js/customChart.js"></script>
    </body>
</html>
