
package servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.*;
import java.nio.file.Paths;
import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import java.util.Date;

import data.*;

import ejb.serverbeans.ItemsEJBLocal;

@WebServlet("/create/item")
@MultipartConfig
public class CreateItem extends Application {
    private static final long serialVersionUID = 1L;

    @EJB
    ItemsEJBLocal itemEJB;

    public void itemForm(HttpServletResponse response, boolean withErrorMessage) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<TITLE>Create Item</TITLE>");
        if (withErrorMessage)
            out.println("<div class=\"alert alert-danger\" role=\"alert\"> Something wrong happened, Try Again </div>");

        out.println("<div class=\"d-flex justify-content-center align-items-center container\">");
        out.println("<form method=post enctype='multipart/form-data'>");
        out.println("<div class =\"form-group\">");
        out.println(
                "<label for=\"name\"> Name </label> <Input type=text class=form-control id=name name=name placeholder=\"Enter Name\"  required style=\"width: 300px;\"></div>");

        out.println("<div class =\"form-group\">");
        out.println(
                "<label for=\"category\"> Category </label> <Input type=text class=form-control placeholder=\"Enter Category\" name=category required style=\"width: 300px;\"></div>");

        out.println("<div class =\"form-group\">");
        out.println(
                "<label for=\"country\"> Country </label> <Input type=text class=form-control placeholder=\"Enter Country\" name=country required  style=\"width: 300px;\"></div>");

        out.println("<div class =\"form-group\">");
        out.println(
                "<label for=\"price\"> Price </label> <Input type=number class=form-control placeholder=\"Enter Price\" name=price  required style=\"width: 300px;\"></div>");

        out.println("<br>File: <input type=\"file\" name=\"file\"/><br>");

        out.println("<button type=\"submit\" class=\"btn btn-primary\">Submit</button>");
        out.println("<a href=\"/projeto2-web/profile/user\" class=\"btn btn-primary\">Back</a>");
        out.println("</form></div>");
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
        super.header(request, response);
        response.setContentType("text/html");
        // Upload file Multi config code
        Blob image = null;
        String fileName = "";
        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        if (filePart != null) {
            fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
            InputStream fileContent = filePart.getInputStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[10240];
            for (int length = 0; (length = fileContent.read(buffer)) > 0;)
                output.write(buffer, 0, length);
            byte[] image_byte = output.toByteArray();
            try {
                image = new SerialBlob(image_byte);
            } catch (Exception e) {
                e.printStackTrace();
                itemForm(response, true);
                return;
            }
        }
        HttpSession session = request.getSession(false);

        String name = request.getParameter("name");
        String category = request.getParameter("category");
        String country = request.getParameter("country");
        String p = request.getParameter("price");
        if (p.contains(".") || Integer.parseInt(p) < 0) {
            itemForm(response, true);
            return;
        }
        int price = Integer.parseInt(p);

        Date date = new Date();
        User user = (User) session.getAttribute("user");

        if (itemEJB.create(name, category, country, price, date, image, fileName, user))
            response.sendRedirect(request.getContextPath() + "/profile");
        else
            itemForm(response, true);

    }
}
