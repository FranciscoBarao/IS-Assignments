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

@WebServlet("/result")
public class result extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    ItemsEJBLocal itemsEJBLocal;

    public void resultDisplay(HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<TITLE>Results</TITLE>");

        out.println("<BR>Results");
        out.println("<BR><form method=get>");

        out.println(
                "<BR<select VALUE=sortingMethod> <option value=nameSort>Name</option> <option value=priceSort>Price</option> <option value=dateSort>Date</option></select>");

        out.println("<BR><Input TYPE=CHECKBOX value=Ascending NAME=order>");

        out.println("<BR><INPUT TYPE=SUBMIT VALUE=Submit></form>");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        resultDisplay(response);
    }
}
