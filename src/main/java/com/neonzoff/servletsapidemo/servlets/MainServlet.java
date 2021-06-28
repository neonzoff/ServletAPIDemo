package com.neonzoff.servletsapidemo.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tseplyaev Dmitry
 */
//@WebServlet(urlPatterns = "main")
public class MainServlet extends HttpServlet {
    private List<User> users;

    @Override
    public void init() throws ServletException {
        super.init();
        //Список хранения добавленных пользователей
        users = new ArrayList<>();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        resp.setContentType("text/html");
        PrintWriter printWriter = resp.getWriter();
        printWriter.write("Main Servlet\n");
        printWriter.write("<br>");
        printWriter.write("<a href=/ServletAPIDemo_war_exploded/static/addUserForm.html>add user</a>");
        printWriter.write("<br>");
        printWriter.write("<a href=/ServletAPIDemo_war_exploded/static/addFileForm.html>add file</a>");

        System.out.println("Main servlet doGet");
        printWriter.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Main servlet doPost");
//        super.doPost(req, resp);
        String requestURI = req.getRequestURI();
        String params = formatParams(req);
        PrintWriter printWriter = resp.getWriter();
        addUser(req);
        printWriter.write("URI:\n" + requestURI + "\nParams:\n" + params + "\nUsers:\n");
        writeUsers(printWriter);
        printWriter.close();
    }

    private void writeUsers(PrintWriter printWriter) {
        for (User user : users) {
            printWriter.write(user + "\n");
        }
    }

    private void addUser(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String[] name = parameterMap.get("name");
        String[] surname = parameterMap.get("surname");
        users.add(new User(name[0],surname[0]));
    }

    private String formatParams(HttpServletRequest request) {
        return request.getParameterMap()
                .entrySet()
                .stream()
                .map(entry -> {
                    String param = String.join(" and ", entry.getValue());
                    return entry.getKey() + " - " + param;
                })
                .collect(Collectors.joining("\n"));
    }
}
