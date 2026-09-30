package com.chat.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.chat.util.DBConnection;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
                          throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {

            // Connect to MySQL
            Connection con = DBConnection.getConnection();

            // Check username and password
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                // Login successful
                HttpSession session = request.getSession();
                session.setAttribute("username", username);

                // Go to chat page
                response.sendRedirect("chat.html");

            } else {

                // Login failed
                response.setContentType("text/html");

                response.getWriter().println(
                    "<script>" +
                    "alert('Invalid Username or Password');" +
                    "window.location='login.jsp';" +
                    "</script>"
                );
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");

            response.getWriter().println(
                "<h3>Database/Login Error</h3>" +
                "<p>" + e.getMessage() + "</p>"
            );
        }
    }
}