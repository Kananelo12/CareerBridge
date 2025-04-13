<%-- 
    Document   : notFound
    Created on : Apr 13, 2025, 3:14:47 PM
    Author     : kanan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <title>404 Page | Resource Not Found</title>

        <!-- ======= CSS Styles ======= -->
        <link rel="stylesheet" href="./assets/css/all.min.css" />
        <link rel="stylesheet" href="./assets/css/fontawesome.min.css" />
        <link rel="stylesheet" href="./assets/css/common.css" />
        <link rel="stylesheet" href="./assets/css/main-styles.css" />
        <link rel="stylesheet" href="./assets/css/404.css" />

        <!-- ======= Favicon ======= -->
        <link rel="apple-touch-icon" sizes="180x180" href="./assets/images/favicon/apple-touch-icon.png">
        <link rel="icon" type="image/png" sizes="32x32" href="./assets/images/favicon/favicon-32x32.png">
        <link rel="icon" type="image/png" sizes="16x16" href="./assets/images/favicon/favicon-16x16.png">
        <link rel="manifest" href="./assets/images/favicon/site.webmanifest">
    </head>
    <body class="<%=savedTheme%>">
        <div class="main-container">
            <!-- Header -->
            <%@include file="./nav-files/header.jsp" %>

            <!-- Main -->
            <main id="main">
                <div class="max-width">
                    <section class="section d-grid">
                        <div class="left-column">
                            <h1 class="heading">404</h1>
                            <h2 class="subheading">
                                Sorry we couldn't find the page you're looking for.
                            </h2>
                            <a href="./index.jsp" class="action-link" aria-label="back to homepage">
                                Back to Homepage
                            </a>
                        </div>
                        <div class="right-column">
                            <img src="./assets/images/server-down.svg" alt="Server down picture" />
                        </div>
                    </section>
                </div>
            </main>
            <!-- End of Main -->
        </div>

        <script src="./assets/js/utilities.js"></script>
        <script src="./assets/js/index.js"></script>
    </body>
</html>
