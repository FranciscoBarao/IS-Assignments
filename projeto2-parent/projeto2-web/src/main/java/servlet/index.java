
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/")
public class index extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public index() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<HEAD><TITLE>Index</TITLE></HEAD><BODY>");

        out.println("<BR><FORM ACTION=login METHOD=get> <INPUT TYPE=submit VALUE=Login></FORM>");
        out.println("<BR><FORM ACTION=register METHOD=get> <INPUT TYPE=submit VALUE=Register></FORM></BODY>");

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
