<%-- 
    Document   : internship
    Created on : Apr 9, 2025, 1:44:47 AM
    Author     : kanan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Internship Page | Career Bridge</title>

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
            .searchResults {
                padding: 5rem 7rem 0 7rem;
                margin: 2rem 0;
            }
            .text {
                color: var(--text-color-01);
                margin-bottom: .5rem;
            }
            .grid__item {
                max-width: 450px;
            }
        </style>
    </head>
    <body class="<%=savedTheme%>">
        <%@include file="./nav-files/header.jsp"%>
        <section class="searchResults" id="searchResults">
            <h1 class="text">Search Results:</h1>
            <c:if test="${not empty searchResults}">
                <div class="internship-grid">
                    <c:forEach var="internshipCard" items="${searchResults}">
                        <div class="grid__item">
                            <div class="grid__card-content">
                                <span class="grid__tag">${internshipCard.category}</span>
                                <div class="grid__image">
                                    <c:choose>
                                        <c:when test="${not empty internshipCard.company.logoUrl}">
                                            <img src="./${internshipCard.company.logoUrl}" alt="Company Logo" />
                                        </c:when>
                                        <c:otherwise>
                                            <img src="./assets/images/placeholder-logo.png" alt="Placeholder Logo" />
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="grid__content">
                                    <h3 class="grid__title">${internshipCard.title}</h3>
                                    <h4 class="grid__subtitle">${internshipCard.company.companyName}</h4>
                                    <div class="grid__group">
                                        <div class="grid__date">
                                            <img src="./assets/images/calendar.png" alt="calendar" />
                                            <span class="date">
                                                <fmt:formatDate value="${internshipCard.postedDateAsDate}" pattern="MMM dd, yyyy"/>
                                            </span>
                                        </div>
                                        <div class="grid__rating">
                                            <img src="./assets/images/star.png" alt="Star" />
                                            <span>12/100</span>
                                        </div>
                                        <p class="grid__description truncated-text">${internshipCard.description}</p>
                                    </div>
                                </div>
                            </div>

                            <div class="grid__footer flex__between">
                                <div class="technologies">
                                    <div class="stack__img flex">
                                        <img src="./assets/images/react.png" alt="Tech-Stack Logo" />
                                    </div>
                                    <div class="stack__img flex">
                                        <img src="./assets/images/tailwind.png" alt="Tech-Stack Logo" />
                                    </div>
                                </div>
                                <a href="" class="grid__btn" data-id="${internshipCard.internshipId}">Apply</a>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
            <c:if test="${empty searchResults}">
                <p>No internships found for “${param.location}” in “${param.category}”.</p>
            </c:if>
        </section>

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
