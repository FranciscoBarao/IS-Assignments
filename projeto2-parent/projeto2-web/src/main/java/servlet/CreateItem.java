
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
            out.println("Create failed. Please try again.<BR>");

        out.println("<BR>Create Item Form");
        out.println("<BR><form method=post enctype='multipart/form-data'><BR>");
        out.println("<BR>Name: <Input TYPE=TEXT required NAME=name>");
        out.println("<BR>Category: <INPUT TYPE=TEXT required NAME=category>");
        out.println("<BR>Country: <INPUT TYPE=TEXT required NAME=country>");
        out.println("<BR>Price: <input type=number step=any required name=price>");
        out.println("<br>File: <input type=\"file\" name=\"file\"/>");
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
        super.header(request, response);
        response.setContentType("text/html");
        // Upload file Multi config code
        Blob image = null;
        String fileName = "";
        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        if (filePart != null){
            fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
            InputStream fileContent = filePart.getInputStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[10240];
            for (int length = 0; (length = fileContent.read(buffer)) > 0;) output.write(buffer, 0, length);
            byte[] image_byte = output.toByteArray();
            try{
                image = new SerialBlob(image_byte);
            }catch (Exception e){
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
        if (p.contains(".")) {
            itemForm(response, true);
            return;
        }
        int price = Integer.parseInt(p);

        Date date = new Date();
        User user = (User) session.getAttribute("user");

        if (itemEJB.create(name, category, country, price, date, image, fileName, user))
            response.sendRedirect(request.getContextPath() + "/home");
        else
            itemForm(response, true);
        
    }
}
