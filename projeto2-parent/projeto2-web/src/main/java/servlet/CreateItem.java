
package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

import data.*;

import ejb.serverbeans.ItemsEJBLocal;

@WebServlet("/create/item")
public class CreateItem extends Application {
    private static final long serialVersionUID = 1L;

    @EJB
    ItemsEJBLocal itemEJB;

    public void itemForm(HttpServletResponse response, boolean withErrorMessage)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<TITLE>Create Item</TITLE>");

        if (withErrorMessage)
            out.println("Create failed. Please try again.<BR>");

        out.println("<BR>Create Item Form");
        out.println("<BR><form method=post><BR>");
        out.println("<BR>Name: <Input TYPE=TEXT required NAME=name>");
        out.println("<BR>Category: <INPUT TYPE=TEXT required NAME=category>");
        out.println("<BR>Country: <INPUT TYPE=TEXT required NAME=country>");
        out.println("<BR>Price: <input type=number step=any required name=price>");
        out.println("<BR><INPUT TYPE=SUBMIT VALUE=Submit></form>");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.header(request, response);
        super.checkLogin(request, response);
        itemForm(response, false);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.checkLogin(request, response);
        response.setContentType("text/html");
        HttpSession session = request.getSession(false);

        String name = request.getParameter("name");
        String category = request.getParameter("category");
        String country = request.getParameter("country");
        int price = Integer.parseInt(request.getParameter("price"));
        Date date = new Date();
        User user = (User)session.getAttribute("user");

        if (itemEJB.create(name, category, country, price, date, user))
            response.sendRedirect(request.getContextPath() + "/home");
        else
            itemForm(response, true);
    }
}
