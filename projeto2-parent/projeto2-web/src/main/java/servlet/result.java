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

import data.Item;
import ejb.serverbeans.ItemsEJBLocal;

@WebServlet("/result")
public class result extends Application {
    private static final long serialVersionUID = 1L;

    @EJB
    ItemsEJBLocal itemsEJBLocal;

    public void resultDisplay(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<TITLE>Results</TITLE>");

        out.println("<div class=\"d-flex justify-content-center align-items-center container\">");
        out.println("<form method=post>");
        out.println("<div class =\"form-inline\">");
        out.println("<label class=\"my-1 mr-2\" for=\"sortingMethod\"> Sorting Method</label>");
        out.println("<select class=\"custom-select my-1 mr-sm-2\" id=sortingMethod name=sortingMethod>");
        out.println(
                "<option selected>Choose</option><option value=\"nameSort\">Name</option><option value=\"priceSort\">Price</option><option value=\"dateSort\">Date</option></select></div>");

        out.println("<div class=\"form-check\">");
        out.println(
                "<input type=\"checkbox\" class=\"form-check-input\" id=\"order\"><label class=\"form-check-label\" for=\"order\" name=order>Descending</label></div>");

        out.println("<button type=\"submit\" class=\"btn btn-primary\">Submit</button></form></div>");

        out.println("<BR> Results");
        HttpSession session = request.getSession(false);
        List<Item> items = (List<Item>) session.getAttribute("items");
        for (Item i : items) {
            out.println("<BR> <a  href=/projeto2-web/profile/item?id=" + i.getId() + "> " + i.toString() + "</a>");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        super.header(request, response);
        super.checkLogin(request, response);
        resultDisplay(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.header(request, response);
        super.checkLogin(request, response);

        HttpSession session = request.getSession(false);
        List<Item> items = (List<Item>) session.getAttribute("items");

        String sortingMethod = request.getParameter("sortingMethod");

        String order = request.getParameter("order");
        boolean o = true;
        if (order != null && !order.equals(""))
            o = false;

        items = itemsEJBLocal.sort(items, sortingMethod, o);
        session.setAttribute("items", items);
        resultDisplay(request, response);
    }
}
