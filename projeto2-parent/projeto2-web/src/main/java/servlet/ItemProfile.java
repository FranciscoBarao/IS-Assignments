
package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.*;
import ejb.serverbeans.ItemsEJBLocal;

@WebServlet("/profile/item")
public class ItemProfile extends Application {
    private static final long serialVersionUID = 1L;

    @EJB
    ItemsEJBLocal itemEJB;

    protected void itemDisplay(String itemID, HttpServletResponse response, boolean withErrorMessage)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<TITLE>Item Profile</TITLE>");

        if (itemID == null) {
            out.println("Wrong path. Needs an ID as parameter. Please try again.<BR>");
            return;
        }

        Item item = itemEJB.read(itemID);

        out.println("<BR>Name: " + item.getName());
        out.println("<BR>Category: " + item.getCategory());
        out.println("<BR>Country: " + item.getCountry());
        out.println("<BR>Price: " + item.getPrice());
        out.println("<BR>Date: " + item.getDate());
        out.println("<BR>Photo: <img src=\"/projeto2-web/images?id=" + item.getId() + "\">");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.header(request, response);
        super.checkLogin(request, response);
        String itemID = request.getParameter("id");

        itemDisplay(itemID, response, false);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
