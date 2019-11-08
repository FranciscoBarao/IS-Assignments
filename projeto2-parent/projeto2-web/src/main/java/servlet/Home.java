
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.*;

@WebServlet("/home")
public class Home extends Application {
    private static final long serialVersionUID = 1L;

    protected void homeDisplay(User user, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        out.println("<HEAD><TITLE>Index</TITLE></HEAD><BODY>");
        out.println("<div class=\"d-flex justify-content-center align-items-center container\">");
        out.println("Welcome " + user.getName() + "</div>");
        out.println("<div class=\"d-flex justify-content-center align-items-center container\">");
        out.println("<br> <a class=\"btn btn-primary\" href=/projeto2-web/search>Search</a></div> ");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.header(request, response);
        super.checkLogin(request, response);
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        homeDisplay(user, request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
