/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package theme;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author kanan
 */
public class ThemeToggler extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Default to light theme if no cookie is found.
        String currentTheme = "";
        
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie ck : cookies) {
                if ("saved-theme".equals(ck.getName())) {
                    currentTheme = ck.getValue();
                    break;
                }
            }
        }
        // 
        String newTheme = "dark-theme".equals(currentTheme) ? "" : "dark-theme";
        
        // Create and configure the cookie
        Cookie themeCookie = new Cookie("saved-theme", newTheme);
        themeCookie.setPath("/"); // Ensure the cookie is available application-wide.
        themeCookie.setMaxAge(60 * 60 * 24 * 30); // Expires in 30 days.
        response.addCookie(themeCookie);
        
        // Redirect back to the referring page or a default page.
        String referer = request.getHeader("Referer");
        if (referer == null || referer.isEmpty()) {
            response.sendRedirect("index.jsp");
        } else {
            response.sendRedirect(referer);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
