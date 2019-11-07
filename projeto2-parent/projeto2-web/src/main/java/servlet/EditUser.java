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

        if (withErrorMessage)
            out.println("Update failed. Please try again.<BR>");

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        out.println("<BR>User Form");
        out.println("<BR><form method=post>");
        out.println("<BR>Email: <Input TYPE=EMAIL VALUE='" + user.getEmail() + "' required NAME=email>");
        out.println("<BR>Password: <INPUT TYPE=PASSWORD VALUE='" + user.getPassword() + "' required NAME=password>");
        out.println("<BR>Name: <Input TYPE=TEXT VALUE='" + user.getName() + "' required NAME=name>");
        out.println("<BR>Country: <Input TYPE=TEXT VALUE='" + user.getCountry() + "' required NAME=country>");

        out.println("<BR><INPUT TYPE=SUBMIT VALUE=Submit></form>");

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
