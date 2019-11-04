
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.*;

@WebServlet("/profile/user")
public class userProfile extends Application {
    private static final long serialVersionUID = 1L;

    protected void userDisplay(User user, List<Item> items, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        out.println("<HEAD><TITLE>Profile</TITLE></HEAD><BODY>");
        // Edit user button
        out.println("<BR> <a href=/projeto2-web/edit/user>Edit User</a> ");
        // User information
        out.println("<BR> Name: " + user.getName());
        out.println("<BR> Email: " + user.getEmail());
        out.println("<BR> Country: " + user.getCountry());

        out.println("<BR> Item List");
        if (!items.isEmpty()) {
            out.println("EMPTYYYYYYY");
            for (Item i : items) {
                out.println("\nYellow Submarine");
                out.println("<BR> " + i.getName() + " <a href = projeto2-web/edit/item?id=" + i.getId() + "> edit </a");
                out.println("<a href = '/projeto2-web/delete/item?id=" + i.getId() + "'> delete </a>");
            }
        } else {
            out.println("You don't have any items, click here to create one.");
            out.println("<BR><a href = '/projeto2-web/create/item'> Create an item </a>");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        super.header(request, response);
        super.checkLogin(request, response);

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        List<Item> items = user.getItems();

        userDisplay(user, items, request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
