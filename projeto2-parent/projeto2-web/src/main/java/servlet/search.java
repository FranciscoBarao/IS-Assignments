package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ejb.serverbeans.UsersEJBLocal;

@WebServlet("/search")
public class search extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    UsersEJBLocal userEJB;

    public void searchForm(HttpServletResponse response, boolean withErrorMessage)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<TITLE>Search Items</TITLE>");

        out.println("<BR>Search");
        out.println("<BR><form method=get>");

        out.println("<BR> Name: <INPUT TYPE=TEXT NAME=name>");

        out.println("<BR>Category: <INPUT TYPE=TEXT NAME=category>");

        out.println(
                "<BR>Price Range:  min <INPUT TYPE=NUMBER min=0 NAME=minPriceRange>  max <INPUT TYPE=NUMBER min=0 NAME=maxPriceRange>");

        out.println("<BR><Input TYPE=CHECKBOX value=In Country NAME=inCountry>");

        out.println("<BR>After Date: <Input TYPE=DATE value=YYYY-MM-DD min=2018-01-01 max=2019-12-31 NAME=afterDate>");

        out.println("<BR><INPUT TYPE=SUBMIT VALUE=Submit></form>");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        // Get Email from Session!!
        String name = request.getParameter("name");
        String Category = request.getParameter("category");
        int minPrice = Integer.parseInt(request.getParameter("minPriceRange"));
        int maxPrice = Integer.parseInt(request.getParameter("maxPriceRange"));

        /*
         * String startDateStr = request.getParameter("startDate"); SimpleDateFormat sdf
         * = new SimpleDateFormat("yyyy-MM-dd"); //surround below line with try catch
         * block as below code throws checked exception Date startDate =
         * sdf.parse(startDateStr); //do further processing with Date object
         * 
         * out.println(sdf.format(startDate)); //this is what you want yyyy-MM-dd
         */
        if (userEJB.edit(email, pass, name, country))
            response.sendRedirect(request.getContextPath() + "/someway");
        else
            searchForm(response, true);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        searchForm(response, false);
    }
}
