
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
        out.println("<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css\" integrity=\"sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB\" crossorigin=\"anonymous\">");
        if (session != null) {
            out.println(
                    "<a href=/projeto2-web/home>Home</a> | <a href=/projeto2-web/profile/user>Profile</a>  |  <a href=/projeto2-web/logout>Logout</a>");
            out.println("<hr>");
        } else {
            out.println("<a href=/projeto2-web/login>Login</a> |  <a href=/projeto2-web/register>Register</a>");
            out.println("<hr>");
        }
    }

    protected void checkLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
