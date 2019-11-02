package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.Item;
import ejb.serverbeans.ItemsEJBLocal;

@WebServlet("/search")
public class search extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    ItemsEJBLocal itemsEJBLocal;

    public void searchForm(HttpServletResponse response, boolean withErrorMessage, boolean isEmpty)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (isEmpty)
            out.println("<BR>Empty Search");

        if (withErrorMessage)
            out.println("<BR> Something wrong happened, try again");

        out.println("<TITLE>Search Items</TITLE>");

        out.println("<BR>Search");
        out.println("<BR><form method=get>");

        out.println("<BR> Name: <INPUT TYPE=TEXT NAME=name>");

        out.println("<BR>Category: <INPUT TYPE=TEXT NAME=category>");

        out.println(
                "<BR>Price Range:  min <INPUT TYPE=NUMBER MIN=0 VALUE=0 NAME=minPriceRange>  max <INPUT TYPE=NUMBER MIN=0 VALUE=0 NAME=maxPriceRange>");

        out.println("<BR><Input TYPE=CHECKBOX value=In Country NAME=inCountry>");

        out.println("<BR>After Date: <Input TYPE=DATE value=YYYY-MM-DD min=2018-01-01 max=2019-12-31 NAME=afterDate>");

        out.println("<BR><INPUT TYPE=SUBMIT VALUE=Submit></form>");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        String name = request.getParameter("name");
        String category = request.getParameter("category");

        int minPrice = Integer.parseInt(request.getParameter("minPriceRange"));
        int maxPrice = Integer.parseInt(request.getParameter("maxPriceRange"));
        if (minPrice > maxPrice)
            searchForm(response, true, false);

        String inCountry = request.getParameter("inCountry");
        if (!inCountry.equals("") || inCountry != null) {
            // Value is true
            // inCountry == session.getCountry()
        }

        String date = request.getParameter("afterDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date afterDate = null;
        try {
            afterDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Item> items = itemsEJBLocal.search(name, category, minPrice, maxPrice, inCountry, afterDate);
        if (items == null)
            searchForm(response, true, false);
        else {
            if (items.isEmpty())
                searchForm(response, false, true);
            else {
                PrintWriter out = response.getWriter();

                for (Item i : items) {
                    out.println(i.toString());
                }
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        searchForm(response, false, false);
    }
}
