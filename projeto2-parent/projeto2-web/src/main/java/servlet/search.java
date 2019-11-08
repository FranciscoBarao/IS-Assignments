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
import data.User;
import ejb.serverbeans.ItemsEJBLocal;

@WebServlet("/search")
public class search extends Application {
    private static final long serialVersionUID = 1L;

    @EJB
    ItemsEJBLocal itemsEJBLocal;

    public void searchForm(HttpServletResponse response, boolean withErrorMessage, boolean isEmpty)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (isEmpty)
            out.println("<div class=\"alert alert-info\" role=\"alert\"> Empty Search </div>");

        if (withErrorMessage) {

            out.println("<div class=\"alert alert-danger\" role=\"alert\"> Something wrong happened, Try Again </div>");
        }
        out.println("<TITLE>Search Items</TITLE>");
        out.println("<div class=\"d-flex justify-content-center align-items-center container\">");

        out.println("<br>Search</div>");

        out.println("<div class=\"d-flex justify-content-center align-items-center container\">");
        out.println("<form method=post>");
        out.println("<div class =\"form-group\">");
        out.println(
                "<label for=\"name\"> Name</label> <Input type=text class=form-control id=name placeholder=\"Enter Name\" name=name style=\"width: 300px;\"></div>");
        out.println("<div class =\"form-group\">");

        out.println(
                "<label for=\"category\"> Category</label> <Input type=text class=form-control id=category placeholder=\"Enter Category\" name=category style=\"width: 300px;\"></div>");

        out.println("<div class =\"form-group\">");

        out.println(
                "<label for=\"minPriceRange\"> min</label> <Input type=number value=0 required class=form-control id=minPriceRange placeholder=\"Enter Min\" name=minPriceRange style=\"width: 150px;\"><label for=\"maxPriceRange\"> max</label> <Input type=number value=0 required=true class=form-control id=maxPriceRange placeholder=\"Enter Max\" name=maxPriceRange style=\"width: 150px;\"></div>");

        out.println("<div class=\"form-check\">");
        out.println(
                "<input type=\"checkbox\" class=\"form-check-input\" id=\"inCountry\"><label class=\"form-check-label\" for=\"inCountry\" name=inCountry>inCountry</label></div>");

        out.println("<br>After Date: <Input TYPE=DATE min=2018-01-01 max=2019-12-31 NAME=afterDate>");

        out.println("<br><button type=\"submit\" class=\"btn btn-primary\">Submit</button></form></div>");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.header(request, response);
        super.checkLogin(request, response);
        searchForm(response, false, false);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        super.header(request, response);
        super.checkLogin(request, response);
        HttpSession session = request.getSession(false);

        response.setContentType("text/html");
        String name = request.getParameter("name");
        if (name == null || name.equals(""))
            name = "";

        String category = request.getParameter("category");
        if (category == null || category.equals(""))
            category = "";

        String inCountry = request.getParameter("inCountry");
        if (inCountry != null && !inCountry.equals("")) {
            // Value is true
            User user = (User) session.getAttribute("user");
            inCountry = user.getCountry();
        } else
            inCountry = "";

        String afterDate = request.getParameter("afterDate");

        int minPrice = Integer.parseInt(request.getParameter("minPriceRange"));
        int maxPrice = Integer.parseInt(request.getParameter("maxPriceRange"));
        if (minPrice > maxPrice) {
            searchForm(response, true, false);
            return;
        }

        List<Item> items = itemsEJBLocal.search(name, category, minPrice, maxPrice, inCountry, afterDate);
        if (items == null)
            searchForm(response, true, false);
        else {
            if (items.isEmpty())
                searchForm(response, false, true);
            else {
                // Store list on array
                session.setAttribute("items", items);
                response.sendRedirect(request.getContextPath() + "/result");
            }
        }
    }
}
