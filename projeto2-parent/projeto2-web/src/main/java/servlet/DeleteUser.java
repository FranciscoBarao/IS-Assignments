
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

import ejb.serverbeans.*;

@WebServlet("/delete/user")
public class DeleteUser extends Application {
    private static final long serialVersionUID = 1L;

    @EJB
    ItemsEJBLocal itemEJB;
    @EJB
    UsersEJBLocal userEJB;

    public void userDelete(String userID, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<TITLE>Delete Item</TITLE>");

        if (userID == null) {
            out.println("Wrong path. Needs an ID as parameter. Please try again.<BR>");
            return;
        }

        if (itemEJB.delete_all(userID)) {
            if (userEJB.delete(userID)) {
                HttpSession session = request.getSession();
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/projecto2-web/");
            }
            out.println("Couldn't delete User. Please try again.<BR>");
            out.println("<BR><a href = '/projeto2-web/profile/user'> Return to profile </a>");
        } else {
            out.println("Couldn't delete all your items. Please try again.<BR>");
            out.println("<BR><a href = '/projeto2-web/profile/user'> Return to profile </a>");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/projecto2-web/home");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.header(request, response);
        super.checkLogin(request, response);
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        String userID = "" + user.getId();
        userDelete(userID, request, response);
    }
}
