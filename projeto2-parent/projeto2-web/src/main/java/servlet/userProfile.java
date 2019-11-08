package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.*;
import ejb.serverbeans.ItemsEJBLocal;

@WebServlet("/profile/user")
public class userProfile extends Application {
    private static final long serialVersionUID = 1L;

    @EJB
    ItemsEJBLocal itemsEJB;

    protected void userDisplay(User user, List<Item> items, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        out.println("<HEAD><TITLE>Profile</TITLE></HEAD><BODY>");
        out.println("<div class = \"\">");
        // User information
        out.println("<div class=\"card\">");
        out.println("<div class=\"card-header\"> Profile </div>");
        out.println("<div class=\"card-body\">");
        out.println("<BR> Name: " + user.getName());
        out.println("<BR> Email: " + user.getEmail());
        out.println("<BR> Country: " + user.getCountry());

        out.println("<BR> Item List");
        if (!items.isEmpty()) {
            for (Item i : items) {
                out.println("<div class = \"container\">");
                out.println("<p>" + i.getName() + "</p>");
                out.println("<a class=\"btn btn-primary btn-sm\" href = /projeto2-web/profile/item?id=" + i.getId()
                        + "> View </a>");
                out.println("<a class=\"btn btn-primary btn-sm\" href = /projeto2-web/edit/item?id=" + i.getId()
                        + "> edit </a>");
                out.println("<form class=\"form-horizontal\" method=\"POST\" action=\"/projeto2-web/delete/item\">");
                out.println("<input name=id type=hidden value=" + i.getId() + ">");
                out.println(
                        "<input class=\"btn btn-outline-danger btn-sm\" type=\"submit\" value=\"delete\" onclick=\"return confirm('Are you sure?')\"/>");
                out.println("</form>");
                out.println("</div>");
            }
        } else {
            out.println("<BR>You have no items");
        }
        out.println("<div class = \"row\">");
        // Create item button
        out.println("<BR><a class=\"btn btn-primary btn-sm\" href=/projeto2-web/create/item>Create Item</a> ");
        // Edit user button
        out.println("<BR><a class=\"btn btn-primary btn-sm\" href=/projeto2-web/edit/user>Edit User</a> ");
        // Delete user button
        out.println("<form method=\"POST\" action=\"/projeto2-web/delete/user\">");
        out.println(
                "<input class=\"btn btn-outline-danger btn-sm\" type=\"submit\" value=\"delete user\" onclick=\"return confirm('Are you sure?')\"/></form>");
        out.println("</div>");
        out.println("</div>");
        out.println("</div>");
        out.println("</div>");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        super.header(request, response);
        super.checkLogin(request, response);

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        List<Item> items = itemsEJB.searchByUser("" + user.getId());
        items = itemsEJB.sort(items, "dateSort", false);
        userDisplay(user, items, request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
