
package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.User;
import ejb.serverbeans.ItemsEJBLocal;

@WebServlet("/delete/item")
public class DeleteItem extends Application {
    private static final long serialVersionUID = 1L;

    @EJB
    ItemsEJBLocal itemEJB;

    public void itemDelete(String itemID, HttpServletRequest request, HttpServletResponse response, boolean isError)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<TITLE>Delete Item</TITLE>");

        if (itemID == null) {
            out.println("Wrong path. Needs an ID as parameter. Please try again.<BR>");
            return;
        } else if (isError) { // checks if user has item
            out.println("You can't access that item. Please try with another id.<BR>");
            return;
        } else {
            if (itemEJB.delete(itemID))
                response.sendRedirect(request.getContextPath() + "/profile/user");
            else
                out.println("Delete failed. Please try again.");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.header(request, response);
        super.checkLogin(request, response);
        response.sendRedirect(request.getContextPath() + "/profile/user");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.header(request, response);
        super.checkLogin(request, response);

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        String itemId = request.getParameter("id");

        if (itemEJB.checkUserItem(itemId, user.getId()))
            itemDelete(itemId, request, response, false);
        else
            itemDelete(itemId, request, response, true);
    }
}
