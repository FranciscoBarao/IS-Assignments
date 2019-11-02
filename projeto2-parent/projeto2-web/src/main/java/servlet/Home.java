
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.*;

@WebServlet("/home")
public class Home extends Application {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.header(request, response);
        HttpSession session = request.getSession(false);  
        User user = (User)session.getAttribute("user");
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        out.println("<HEAD><TITLE>Index</TITLE></HEAD><BODY>");
        out.println("Welcome " + user.getName() + "!");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
