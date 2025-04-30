<%-- 
    Document   : internshipCards
    Created on : Apr 13, 2025, 3:03:35 AM
    Author     : kanan
--%>

<%@page import="java.util.List"%>
<%@page import="model.Internship"%>
<%@page import="dao.InternshipDAO"%>
<%@page import="java.sql.Connection"%>
<%@page import="utils.ConnectionFile"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
    Connection con = ConnectionFile.getConn();

    // Check the current page of the card items
    int currentPage = 1;
    String pageParam = request.getParameter("page");
    if (pageParam != null) {
        try {
            currentPage = Integer.parseInt(pageParam);
        } catch (Exception ex) {
            currentPage = 1;
        }
    }

    int limit = 6;
    int offset = (currentPage - 1) * limit;

    InternshipDAO internshipDAO = new InternshipDAO(con);
    // offset the list from 0 and limit them to 6 items
    List<Internship> internships = internshipDAO.getInternships(offset, limit);
    request.setAttribute("internshipCards", internships);

    request.setAttribute("currentPage", currentPage);
    request.setAttribute("limit", limit);
%>
<div class="internship__cards__content">
    <h1 class="card__title">Pick Your Internship</h1>
    <div class="internship-grid">

        <c:forEach var="internshipCard" items="${internshipCards}">
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
                <!-- Footer that should stick to the bottom -->
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
    <div class="cards__pagination flex__between">
        <%
            String referer = request.getHeader("Referer");
        %>
        <c:choose>
            <c:when test="${currentPage > 1}">
                <a href="${referer}?page=${currentPage - 1}#internships" class="pagination__btn flex">
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

        <c:choose>
            <c:when test="${fn:length(internshipCards) == limit}">
                <a href="${referer}?page=${currentPage + 1}#internships" class="pagination__btn flex">
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