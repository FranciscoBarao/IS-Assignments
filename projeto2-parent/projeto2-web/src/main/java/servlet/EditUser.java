package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

import data.*;

import ejb.serverbeans.UsersEJBLocal;

@WebServlet("/edit/user")
public class EditUser extends Application {
    private static final long serialVersionUID = 1L;

    @EJB
    UsersEJBLocal userEJB;

    public void editUserForm(HttpServletRequest request, HttpServletResponse response, boolean withErrorMessage)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<TITLE>Edit User</TITLE>");

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        if (withErrorMessage)
            out.println("<div class=\"alert alert-danger\" role=\"alert\"> Something wrong happened, Try Again </div>");

        out.println("<div class=\"d-flex justify-content-center align-items-center container\">");
        out.println("<form method=post>");
        out.println("<div class =\"form-group\">");
        out.println(
                "<label for=\"email\"> Email </label> <Input type=email class=form-control id=email name=email value=\""
                        + user.getEmail() + "\" required style=\"width: 300px;\"></div>");

        out.println("<div class =\"form-group\">");
        out.println("<label for=\"password\"> Password </label> <Input type=password class=form-control value=\""
                + user.getPassword() + "\" name=password   required style=\"width: 300px;\"></div>");
        out.println("<div class =\"form-group\">");
        out.println("<label for=\"name\"> Name </label> <Input type=text class=form-control value=\"" + user.getName()
                + "\" name=name  required style=\"width: 300px;\"></div>");
        out.println("<div class =\"form-group\">");
        out.println("<label for=\"country\"> Country </label> <Input type=text class=form-control value=\""
                + user.getCountry() + "\" name=country  required style=\"width: 300px;\"></div>");

        out.println("<button type=\"submit\" class=\"btn btn-primary\">Submit</button>");
        out.println("<a href=\"/projeto2-web/profile/user\" class=\"btn btn-primary\">Back</a>");
        out.println("</form></div>");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.header(request, response);
        super.checkLogin(request, response);
        editUserForm(request, response, false);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.header(request, response);
        super.checkLogin(request, response);

        response.setContentType("text/html");
        HashMap<String, String> params = new HashMap<String, String>();
        // Can be better, this way we update everything, even if things stay the same
        params.put("email", request.getParameter("email"));
        params.put("password", request.getParameter("password"));
        params.put("name", request.getParameter("name"));
        params.put("country", request.getParameter("country"));

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        if (userEJB.update(user.getEmail(), params)) {
            user = userEJB.read(request.getParameter("email"));
            if (user != null) {
                session.setAttribute("user", user);
            }
            response.sendRedirect(request.getContextPath() + "/profile");
        } else
            editUserForm(request, response, true);
    }
}
