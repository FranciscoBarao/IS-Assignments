package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ejb.serverbeans.ItemsEJBLocal;
import ejb.serverbeans.UsersEJBLocal;

@WebServlet("/search")
public class search extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    ItemsEJBLocal itemsEJBLocal;

    public void searchForm(HttpServletResponse response, boolean withErrorMessage)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

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
        String Category = request.getParameter("category");

        int minPrice = Integer.parseInt(request.getParameter("minPriceRange"));
        int maxPrice = Integer.parseInt(request.getParameter("maxPriceRange"));
        if (minPrice > maxPrice)
            searchForm(response, true);
            
        String inCountry = request.getParameter("inCountry");
        if(!inCountry.equals("") || inCountry != null ){
            //Value is true
            //inCountry == session.getCountry()
        }

        String startDateStr = request.getParameter("startDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(startDateStr);
        // sdf.format(startDate)

      
        if (itemsEJBLocal.)
            response.sendRedirect(request.getContextPath() + "/someway");
        else
            searchForm(response, true);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        searchForm(response, false);
    }
}
