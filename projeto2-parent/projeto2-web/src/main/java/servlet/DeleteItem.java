
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

@WebServlet("/delete/item")
public class DeleteItem extends Application {
    private static final long serialVersionUID = 1L;

    @EJB
    ItemsEJBLocal itemEJB;

    public void itemDelete(String itemID, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<TITLE>Delete Item</TITLE>");

        if (itemID == null) {
            out.println("Wrong path. Needs an ID as parameter. Please try again.<BR>");
            return;
        }

        // Check if item belongs to user
        if (false) {
            out.println("You can't access that item. Please try with another id.<BR>");
            return;
        }

        if(itemEJB.delete(itemID)){
            response.sendRedirect(request.getContextPath() + "/projecto2-web/profile/user");
        }else{
            out.println("Delete failed. Please try again.<BR>");
            out.println("<BR><a href = '/projeto2-web/profile/user'> Return to profile </a>");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.header(request, response);
        super.checkLogin(request, response);
        String itemID = request.getParameter("id");
        itemDelete(itemID, request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
