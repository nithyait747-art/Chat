package com.chat.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.chat.util.DBConnection;

@WebServlet("/sendMessage")
public class SendMessageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain;charset=UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            response.getWriter().write("Please login.");
            return;
        }

        String sender = (String) session.getAttribute("username");
        String receiver = request.getParameter("receiver");
        String message = request.getParameter("message");

        if (receiver == null || message == null ||
            receiver.trim().isEmpty() || message.trim().isEmpty()) {
            response.getWriter().write("Receiver and message are required.");
            return;
        }

        receiver = receiver.trim();
        message = message.trim();

        try (Connection con = DBConnection.getConnection()) {

            try (PreparedStatement user = con.prepareStatement(
                "INSERT INTO users(username) VALUES(?) ON DUPLICATE KEY UPDATE username=username")) {
                user.setString(1, receiver);
                user.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO messages(sender, receiver, message) VALUES(?,?,?)")) {
                ps.setString(1, sender);
                ps.setString(2, receiver);
                ps.setString(3, message);
                ps.executeUpdate();
            }

            response.getWriter().write("success");

        } catch (Exception e) {
            throw new IOException("Message sending failed: " + e.getMessage(), e);
        }
    }
}
