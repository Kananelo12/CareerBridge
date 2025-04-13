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
<%
    Connection conn = ConnectionFile.getConn();

    // Offset the list from 0 and limit them to 6 items
    int offset = 0;
    int limit = 6;

    InternshipDAO internshipDAO = new InternshipDAO(conn);
    // offset the list from 0 and limit them to 6 items
    List<Internship> internships = internshipDAO.getInternships(offset, limit);
    request.setAttribute("internshipCards", internships);
%>
<div class="internship__cards__content">
    <h1 class="card__title">Pick Your Internship</h1>
    <div class="internship-grid">

        <c:forEach var="internshipCard" items="${internshipCards}">
            <div class="grid__item">
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
                            <img class="" src="./assets/images/calendar.png" alt="calendar" />
                            <span class="date"><fmt:formatDate value="${internshipCard.postedDateAsDate}" pattern="MMM dd, yyyy"/></span>
                        </div>
                        <div class="grid__rating">
                            <img class="" src="./assets/images/star.png" alt="Star" />
                            <span>12/100</span>
                        </div>
                        <p class="grid__description">${internshipCard.description}</p>
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
                </div>
            </div>
        </c:forEach>

    </div>
</div>