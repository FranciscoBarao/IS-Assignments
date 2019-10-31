
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

@WebServlet("/register")
public class register extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    UsersEJBLocal userEJB;

    public void registerForm(HttpServletResponse response, boolean withErrorMessage)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<TITLE>Register</TITLE>");

        if (withErrorMessage)
            out.println("Register failed. Please try again.<BR>");

        out.println("<BR>Register Form");
        out.println("<BR><form method=post>");
        out.println("<BR>Email: <Input TYPE=TEXT NAME=email>");
        out.println("<BR>Password: <INPUT TYPE=PASSWORD NAME=password>");
        out.println("<BR>Name: <INPUT TYPE=TEXT NAME=name>");
        out.println("<BR>Country: <INPUT TYPE=TEXT NAME=country>");
        out.println("<BR><INPUT TYPE=SUBMIT VALUE=Submit></form>");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        registerForm(response, false);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        String email = request.getParameter("email");
        String pass = request.getParameter("password");
        String name = request.getParameter("name");
        String country = request.getParameter("country");

        if (userEJB.register(email, pass, name, country))
            response.sendRedirect(request.getContextPath() + "/someway");
        else
            registerForm(response, true);

    }
}
