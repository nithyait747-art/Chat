package com.chat.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.chat.util.DBConnection;

@WebServlet("/messages")
public class GetMessagesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            response.getWriter().write("[]");
            return;
        }

        String currentUser = (String) session.getAttribute("username");
        String receiver = request.getParameter("receiver");

        if (receiver == null || receiver.trim().isEmpty()) {
            response.getWriter().write("[]");
            return;
        }

        receiver = receiver.trim();

        String sql =
                "SELECT sender, receiver, message, message_time " +
                "FROM messages " +
                "WHERE (sender=? AND receiver=?) " +
                "OR (sender=? AND receiver=?) " +
                "ORDER BY message_time ASC";

        StringBuilder json = new StringBuilder("[");
        boolean first = true;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, currentUser);
            statement.setString(2, receiver);
            statement.setString(3, receiver);
            statement.setString(4, currentUser);

            try (ResultSet result = statement.executeQuery()) {

                SimpleDateFormat format =
                        new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                while (result.next()) {

                    if (!first) {
                        json.append(",");
                    }

                    first = false;

                    String sender =
                            escapeJson(result.getString("sender"));

                    String rec =
                            escapeJson(result.getString("receiver"));

                    String message =
                            escapeJson(result.getString("message"));

                    String time =
                            escapeJson(
                                    format.format(
                                            result.getTimestamp("message_time")
                                    )
                            );

                    json.append("{")
                        .append("\"sender\":\"")
                        .append(sender)
                        .append("\",")
                        .append("\"receiver\":\"")
                        .append(rec)
                        .append("\",")
                        .append("\"message\":\"")
                        .append(message)
                        .append("\",")
                        .append("\"time\":\"")
                        .append(time)
                        .append("\"")
                        .append("}");
                }
            }

            json.append("]");

            response.getWriter().write(json.toString());

        } catch (Exception e) {

            throw new ServletException(
                    "Could not retrieve messages: "
                    + e.getMessage(), e);
        }
    }

    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }
}