package com.chat.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.chat.util.DBConnection;

@WebServlet("/ChatServlet")
public class ChatServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
                          throws ServletException, IOException {

        // Get message from chat page
        String message = request.getParameter("message");

        // Get logged-in username from session
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        // Check login
        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String sender = username;

        try {

            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO messages (username, sender, message) VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, sender);
            ps.setString(3, message);

            ps.executeUpdate();

            ps.close();
            con.close();

            // Go back to chat page
            response.sendRedirect("chat.html");

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");
            response.getWriter().println(
                "<h3>Database Error: " + e.getMessage() + "</h3>"
            );
        }
    }
}