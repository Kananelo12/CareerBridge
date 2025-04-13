<%-- 
    Document   : StudentDashboard
    Created on : Apr 9, 2025, 1:12:27 AM
    Author     : kanan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

        <!-- ======= CSS Styles ======= -->
        <link rel="stylesheet" href="./assets/css/all.min.css" />
        <link rel="stylesheet" href="./assets/css/fontawesome.min.css" />
        <link rel="stylesheet" href="./assets/css/common.css" />
        <link rel="stylesheet" href="./assets/css/sidebar.css" />

        <!-- ======= Favicon ======= -->
        <link rel="apple-touch-icon" sizes="180x180" href="./assets/images/favicon/apple-touch-icon.png">
        <link rel="icon" type="image/png" sizes="32x32" href="./assets/images/favicon/favicon-32x32.png">
        <link rel="icon" type="image/png" sizes="16x16" href="./assets/images/favicon/favicon-16x16.png">
        <link rel="manifest" href="./assets/images/favicon/site.webmanifest">
    </head>
    <body class="<%=savedTheme%>">

        <main class="dashboard">
            <div class="dashboard-left-section" id="sidebarContainer">
                <%@include file="./nav-files/studentSidebar.jsp" %>
            </div>
            <div class="dashboard-right-section">
                <section class="section overview" id="overviewSection">
                    <div class="card card-w-100 card-one">
                        <div class="card-content flex__between">
                            <div class="metric">
                                <h3 class="card-title">Applications Submitted</h3>
                                <h4 class="card-subtitle">546</h4>
                                <div class="metric-badge flex__between">
                                    <div class="trend flex">
                                        <i class="fas fa-arrow-up"></i>
                                    </div>
                                    <span class="">+33.45%</span>
                                </div>
                            </div>
                            <div class="separator"></div>
                            <div class="metric">
                                <h3 class="card-title">Interviews Scheduled</h3>
                                <h4 class="card-subtitle">546</h4>
                                <div class="metric-badge flex__between">
                                    <div class="trend flex">
                                        <i class="fas fa-arrow-up"></i>
                                    </div>
                                    <span class="">+33.45%</span>
                                </div>
                            </div>
                            <div class="separator"></div>
                            <div class="metric">
                                <h3 class="card-title">My Listings</h3>
                                <h4 class="card-subtitle">546</h4>
                                <div class="metric-badge flex__between">
                                    <div class="trend flex">
                                        <i class="fas fa-arrow-up"></i>
                                    </div>
                                    <span class="">+33.45%</span>
                                </div>
                            </div>
                            <div class="separator"></div>
                            <div class="metric">
                                <h3 class="card-title">My Listings</h3>
                                <h4 class="card-subtitle">546</h4>
                                <div class="metric-badge flex__between">
                                    <div class="trend flex">
                                        <i class="fas fa-arrow-up"></i>
                                    </div>
                                    <span class="">+33.45%</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card-container bento-grid">
                        <!-- Chart + Satisfaction + Webinars -->
                        <div class="card-group flex__between">
                            <!-- Audience Chart Card -->
                            <div class="card card-chart">
                                <h3 class="card-title">Audience</h3>
                                <canvas id="statsChart"></canvas>
                                <p class="metric-change">+58.31% for 7 last days</p>
                            </div>

                            <!-- Gauge/Satisfaction Card -->
                            <div class="card card-gauge">
                                <h3 class="card-title">Audience Satisfaction</h3>
                                <div class="gauge-container">
                                    <!-- You can use a radial progress plugin here -->
                                    <span class="gauge-percentage">76.7%</span>
                                </div>
                                <p class="gauge-note">Based on likes / dislikes</p>
                            </div>

                            <!-- Promo/Webinar Card -->
                            <div class="card card-promo">
                                <h3 class="card-title">Webinars</h3>
                                <p class="promo-text">Learn how you can earn more than 20% each month!</p>
                                <button class="btn-learn-more">Learn More</button>
                            </div>
                        </div>

                        <!--                        <div class="card card-w-flex card-one">
                        
                                                </div>-->
                    </div>
                </section>
                <section class="section hidden" id="internshipSection">
                    <h1>Internships</h1>
                </section>

            </div>
        </main>

        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script src="./assets/js/customChart.js"></script>
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
    </body>
</html>
