package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ejb.serverbeans.UsersEJBLocal;

@WebServlet("/editUser")
public class editUser extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    UsersEJBLocal userEJB;

    public void editUserForm(HttpServletResponse response, boolean withErrorMessage)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<TITLE>Edit User</TITLE>");

        out.println("<BR>User Form");
        out.println("<BR><form method=post>");
        out.println("<BR>Password: <INPUT TYPE=PASSWORD required NAME=password>");
        out.println("<BR>Name: <Input TYPE=TEXT required NAME=name>");
        out.println("<BR>Country: <Input TYPE=TEXT required NAME=country>");

        out.println("<BR><INPUT TYPE=SUBMIT VALUE=Submit></form>");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        editUserForm(response, false);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        // Get Email from Session!!
        String email = "asd";
        String pass = request.getParameter("password");
        String name = request.getParameter("name");
        String country = request.getParameter("country");

        if (userEJB.edit(email, pass, name, country))
            response.sendRedirect(request.getContextPath() + "/someway");
        else
            editUserForm(response, true);

    }
}
