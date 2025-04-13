<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<aside class="sidebar">
    <div class="sidebar__top">
        <div class="user__info">
            <div class="profile__img">
                <c:choose>
                    <c:when test="${not empty userDetails.profileImageUrl}">
                        <img src="./${userDetails.profileImageUrl}" alt="Profile Image" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="firstInitial" value="${fn:toUpperCase(fn:substring(userDetails.firstName, 0, 1))}" />
                        <c:set var="lastInitial" value="${fn:toUpperCase(fn:substring(userDetails.lastName, 0, 1))}" />

                        <div class="profile__initials">
                            ${firstInitial}${lastInitial}
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="profile__info">
                <h2 class="profile__title">${userDetails.firstName} ${userDetails.lastName}</h2>
                <span class="profile__role">${user.roleName}</span>
            </div>
            <div class="sidebar__operations flex">
                <div class="icon flex">
                    <i class="fas fa-xmark"></i>
                </div>
            </div>
        </div>
    </div>
    <nav class="sidebar__nav">
        <ul class="nav__list">
            <li class="nav__item active" id="overview">
                <a href="#" class="nav__link">
                    <span class="nav__icon"><i class="fas fa-grip"></i></span>
                    <span class="nav__text">Overview</span>
                </a>
            </li>
            <li class="nav__item" id="inbox">
                <a href="#" class="nav__link">
                    <span class="nav__icon"><i class="fas fa-envelope"></i></span>
                    <span class="nav__text">Inbox</span>
                </a>
            </li>
            <li class="nav__item" id="users">
                <a href="#" class="nav__link">
                    <span class="nav__icon"><i class="fas fa-user"></i></span>
                    <span class="nav__text">Users</span>
                </a>
            </li>
            <li class="nav__item" id="internship">
                <a href="#" class="nav__link">
                    <span class="nav__icon"><i class="fas fa-house-user"></i></span>
                    <span class="nav__text">Internship</span>
                </a>
            </li>
            <li class="nav__item">
                <a href="#" class="nav__link">
                    <span class="nav__icon"><i class="fas fa-gear"></i></span>
                    <span class="nav__text">Settings</span>
                </a>
            </li>
        </ul>
    </nav>
    <div class="sidebar__bottom">
        <div class="app__settings">
            <div class="logout flex">
                <a href="LogoutServlet" class="logout__btn flex"><i class="fas fa-arrow-right-from-bracket"></i></a>
            </div>
            <div class="app__version">
                <h2 class="profile__title">LehaeConnect</h2>
                <span class="profile__role">ver. 0.234</span>
            </div>
            <div class="more__settings flex">
                <div class="dot"><i class="fas fa-ellipsis"></i></div>
            </div>
        </div>
    </div>
</aside>