
package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

import data.Item;

import ejb.serverbeans.ItemsEJBLocal;

@WebServlet("/edit/item")
public class EditItem extends Application {
    private static final long serialVersionUID = 1L;

    @EJB
    ItemsEJBLocal itemEJB;

    public void itemForm(String itemID, HttpServletResponse response, boolean withErrorMessage)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<TITLE>Update Item</TITLE>");

        if (itemID == null) {
            out.println("Wrong path. Needs an ID as parameter. Please try again.<BR>");
            return;
        }
        if (withErrorMessage)
            out.println("Update failed. Please try again.<BR>");

        Item item = itemEJB.read(itemID);

        if (item == null) {
            out.println("You can't access that item. Please try with another id.<BR>");
            return;
        }

        out.println("<BR>Update Item Form");
        out.println("<BR><form method=post><BR>");
        out.println("<input name=itemID type=hidden value=" + itemID + ">");
        out.println("<BR>Name: <Input TYPE=TEXT VALUE='" + item.getName() + "' required NAME=name>");
        out.println("<BR>Category: <INPUT TYPE=TEXT VALUE='" + item.getCategory() + "' required NAME=category>");
        out.println("<BR>Country: <INPUT TYPE=TEXT VALUE='" + item.getCountry() + "' required NAME=country>");
        out.println("<BR>Price: <input type=number VALUE='" + item.getPrice() + "' step=any required name=price>");
        out.println("<BR><INPUT TYPE=SUBMIT VALUE=Submit></form>");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.header(request, response);
        super.checkLogin(request, response);
        String itemID = request.getParameter("id");
        itemForm(itemID, response, false);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.checkLogin(request, response);
        response.setContentType("text/html");
        HashMap<String, String> params = new HashMap();
        // Can be better, this way we update everything, even if things stay the same
        params.put("name", request.getParameter("name"));
        params.put("category", request.getParameter("category"));
        params.put("country", request.getParameter("country"));
        params.put("price", request.getParameter("price"));

        String itemID = request.getParameter("id");
        if (itemEJB.update(itemID, params))
            response.sendRedirect(request.getContextPath() + "/");
        else
            itemForm(itemID, response, true);

    }
}
