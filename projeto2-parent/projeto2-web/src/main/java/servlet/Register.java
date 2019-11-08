
package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ejb.serverbeans.UsersEJBLocal;

@WebServlet("/register")
public class Register extends Application {
    private static final long serialVersionUID = 1L;

    @EJB
    UsersEJBLocal userEJB;

    public void registerForm(HttpServletResponse response, boolean withErrorMessage)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<TITLE>Register</TITLE>");

        if (withErrorMessage)
            out.println("<div class=\"alert alert-danger\" role=\"alert\"> Something wrong happened, Try Again </div>");

        out.println("<div class=\"d-flex justify-content-center align-items-center container\">");
        out.println("<form method=post>");
        out.println("<div class =\"form-group\">");
        out.println(
                "<label for=\"email\"> Email </label> <Input type=email class=form-control id=email placeholder=\"Enter Email\" name=email style=\"width: 300px;\"></div>");
        out.println("<div class =\"form-group\">");
        out.println(
                "<label for=\"password\"> Password </label> <Input type=password class=form-control placeholder=\"Enter Password\" name=password style=\"width: 300px;\"></div>");
        out.println("<div class =\"form-group\">");
        out.println(
                "<label for=\"name\"> Name </label> <Input type=text class=form-control placeholder=\"Enter Name\" name=name style=\"width: 300px;\"></div>");
        out.println("<div class =\"form-group\">");
        out.println(
                "<label for=\"country\"> Country </label> <Input type=text class=form-control placeholder=\"Enter Country\" name=country style=\"width: 300px;\"></div>");

        out.println("<button type=\"submit\" class=\"btn btn-primary\">Submit</button></form></div>");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.header(request, response);
        registerForm(response, false);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.header(request, response);
        response.setContentType("text/html");
        String email = request.getParameter("email");
        String pass = request.getParameter("password");
        String name = request.getParameter("name");
        String country = request.getParameter("country");

        if (userEJB.register(email, pass, name, country))
            response.sendRedirect(request.getContextPath() + "/");
        else
            registerForm(response, true);

    }
}
