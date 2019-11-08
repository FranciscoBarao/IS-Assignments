
package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Application extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void header(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        out.println(
                "<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css\" integrity=\"sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB\" crossorigin=\"anonymous\">");
        out.println("<nav role=\"navigation\" class=\"navbar navbar-default navbar-fixed-top\">");
        out.println("<div class=\"container\">");
        out.println("<a class=\"navbar-brand\" href=\"/projeto2-web/home\">MyBay</a>");

        // out.println("<li class=\"active\"><a href=\"#\">Home</a></li>");
        if (session != null) {
            out.println(
                    "<li class=\"nav\"><a class=\"nav-link btn btn-primary\" href=/projeto2-web/profile/user>Profile</a></li>");
            out.println(
                    "<li class=\"nav\"><a class=\"nav-link btn btn-primary\" href=/projeto2-web/logout>Logout</a></li>");
        } else {
            out.println("<li class=\"nav\"><a class=\"btn btn-primary\" href=/projeto2-web/login>Login</a></li>");
            out.println("<li class=\"nav\"><a class=\"btn btn-primary\" href=/projeto2-web/register>Register</a></li>");
        }

        out.println("</ul>");
        out.println("</nav>");
        out.println("<hr>");

    }

    protected void checkLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
