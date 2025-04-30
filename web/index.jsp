<%-- 
    Document   : index
    Created on : Apr 8, 2025, 9:21:23 PM
    Author     : kanan
--%>

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

        <title>Career Bridge | Landing Page</title>

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
            div.eapps-widget {
                position: static !important;
            }
        </style>
    </head>
    <body class="<%=savedTheme%>">
        <!-- ======= Scroll to top button ======= -->
        <div class="toTopBtn flex"></div>

        <!-- ======= Toggle Theme button ======= -->
        <a href="ThemeToggler" class="themeBtn flex">
            <i class="fas fa-moon"></i>
            <i class="fas fa-sun"></i>
        </a>

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

        <%@include file="./nav-files/header.jsp"%>
        <main>
            <section class="hero">
                <div class="hero-img">
                    <img src="./assets/images/hero-image.png" alt="House Image" />
                </div>
                <div class="max-width">
                    <div class="hero-content">
                        <div class="tag">
                            <p>Smart Interns</p>
                        </div>
                        <h1 class="title">Let's hunt for your</h1>
                        <h1 class="title title-2">dream internship</h1>
                        <p class="copy">Discover exciting internship opportunities, some with convenient accommodation to support your journey.</p>
                        <div class="search-wrapper">
                            <div class="searchBtns">
                                <button class="search-btn active">Internships</button>
                                <button class="search-btn">Jobs</button>
                            </div>
                            <div class="search-forms" id="search-forms">
                                <!-- Internship Form -->
                                <form action="InternshipServlet" method="GET" id="internship-form">
                                    <input type="hidden" name="form-type" value="internshipForm" />
                                    <div class="form-group">
                                        <label for="location">Location</label>
                                        <input type="text" name="location" id="location" placeholder="Maseru..." class="form-control" />
                                    </div>
                                    <div class="form-group">
                                        <label for="category">Category</label>
                                        <div class="select-wrapper">
                                            <select name="category" id="category" class="custom-select form-control">
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
                                    <div class="form-group">
                                        <input type="submit" name="searchProperty" value="Search"/>
                                    </div>
                                </form>

                                <!-- Job Form -->
                                <form action="" method="GET" id="job-form" class="hidden">
                                    <input type="hidden" name="form-type" value="job" />
                                    <div class="form-group">
                                        <label for="rentLocation">Location</label>
                                        <input type="text" name="rentLocation" id="rentLocation" placeholder="Maseru..." class="form-control" />
                                    </div>
                                    <div class="form-group">
                                        <label for="jobCategory">Category</label>
                                        <div class="select-wrapper">
                                            <select name="jobCategory" id="jobCategory" class="custom-select form-control">
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
                                    <div class="form-group">
                                        <label for="stipend">Pay Range</label>
                                        <div class="select-wrapper">
                                            <select name="stipend" id="stipend" class="custom-select form-control">
                                                <option value="500,1000">R500 - R1000</option>
                                                <option value="1000,2000">R1000 - R2000</option>
                                                <option value="2000,3000">R2000 - R3000</option>
                                                <option value="3000,5000">R3000 - R5000</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="Duration">Duration</label>
                                        <div class="select-wrapper">
                                            <select name="Duration" id="Duration" class="custom-select form-control">
                                                <option value="3">3 months</option>
                                                <option value="6">6 months</option>
                                                <option value="12">12 months</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <input type="submit" name="searchProperty" value="Search"/>
                                    </div>
                                </form>
                            </div>
                            <c:forEach var="p" items="${property}">
                                <c:out value="${p}"/>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </section>

            <section class="internships" id="internships">
                <div class="max-width">
                    <div class="internships-content">
                        <div class="tag">
                            <p>Discover</p>
                        </div>
                        <h1 class="title">Best Recommended</h1>
                        <div class="copy-wrapper">
                            <p class="copy">Explore our exclusive selection of top-tier internships—unique opportunities designed to shape future professionals.</p>
                            <a href="internship.jsp">View More <i class="fas fa-circle-chevron-right"></i></a>
                        </div>

                        <%@include file="./nav-files/internshipCards.jsp" %>
                    </div>
                </div>
            </section>
        </main>
        <%@include file="./nav-files/footer.jsp"%>

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
                            <input type="file" name="cv" id="cv" class="form-control" accept=".pdf,.doc,.docx" required />
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


        <!-- Elfsight AI Chatbot | Career Bridge AI Chatbot -->
        <script src="https://static.elfsight.com/platform/platform.js" async></script>
        <div class="elfsight-app-8e0e3896-bb4d-4a33-b9db-187b5c221750" data-elfsight-app-lazy></div>

        <script src="./assets/js/utilities.js"></script>
        <script src="./assets/js/index.js"></script>
        <script>
            const internshipIdInput = document.getElementById('internship_id');

            const internshipGridTitle = document.querySelector(".card__title");
            internshipGridTitle.classList.add("hidden");
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
