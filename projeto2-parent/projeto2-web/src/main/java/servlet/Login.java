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

        out.println("<BR>Login Form");
        out.println("<BR><form method=post>");
        out.println("<BR>Email: <Input TYPE=TEXT NAME=email>");
        out.println("<BR>Password: <INPUT TYPE=PASSWORD NAME=password>");
        out.println("<BR><INPUT TYPE=SUBMIT VALUE=Submit></form>");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        super.header(request, response);

        response.setContentType("text/html");
        String email = request.getParameter("email");
        byte[] salt = new byte[16];
        String pass = super.hash(request.getParameter("password"), salt);

        User user = userEJB.login(email, pass);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/");
        } else
            loginForm(response, true);
    }
}
