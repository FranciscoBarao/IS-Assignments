
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

        out.println("<div class=\"d-flex justify-content-center align-items-center container\">");
        out.println("<div class=\"card\" style=\"width: 18rem;\">");
        out.println("<img class=\"card-img-top\" src=");
        if (item.getPhoto() == null) {
            out.println("> alt=\"No Image\">");
        } else {
            out.println("\"/projeto2-web/images?id=" + item.getId() + "\"  alt=\"Card image cap\">");
        }
        out.println("<div class=\"card-body\">");
        out.println("<p class=\"card-text\">Name: " + item.getName() + "<br>Category: " + item.getCategory()
                + "<br>Country: " + item.getCountry() + "<br>Price: " + item.getPrice() + "<br>Date: " + item.getDate()
                + "</p></div></div></div>");
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
