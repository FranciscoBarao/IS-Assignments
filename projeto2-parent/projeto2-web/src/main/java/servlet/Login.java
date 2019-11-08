package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.*;

import ejb.serverbeans.UsersEJBLocal;

@WebServlet("/login")
public class Login extends Application {
    private static final long serialVersionUID = 1L;

    @EJB
    UsersEJBLocal userEJB;

    public void loginForm(HttpServletResponse response, boolean withErrorMessage) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<TITLE>Login</TITLE>");

        if (withErrorMessage)
            out.println("Login failed. Please try again.<BR>");
        out.println("<div class=\"d-flex justify-content-center align-items-center container\">");
        out.println("<form method=post>");
        out.println("<div class =\"form-group\">");
        out.println(
                "<label for=\"email\"> Email </label> <Input type=email class=form-control id=email placeholder=\"Enter Email\" name=email style=\"width: 300px;\"></div>");
        out.println("<div class =\"form-group\">");
        out.println(
                "<label for=\"password\"> Password </label> <Input type=password class=form-control placeholder=\"Enter Password\" name=password style=\"width: 300px;\"></div>");

        out.println("<button type=\"submit\" class=\"btn btn-primary\">Submit</button></form></div>");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.header(request, response);

        loginForm(response, false);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        super.header(request, response);

        response.setContentType("text/html");
        String email = request.getParameter("email");
        String pass = request.getParameter("password");

        User user = userEJB.login(email, pass);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/");
        } else
            loginForm(response, true);
    }
}
