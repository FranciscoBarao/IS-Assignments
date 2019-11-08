
package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.MultipartConfig;
import java.io.*;
import java.nio.file.Paths;
import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import javax.servlet.http.Part;

import data.Item;

import ejb.serverbeans.ItemsEJBLocal;

@WebServlet("/edit/item")
@MultipartConfig
public class EditItem extends Application {
    private static final long serialVersionUID = 1L;

    @EJB
    ItemsEJBLocal itemEJB;

    public void itemForm(String itemID, HttpServletResponse response, boolean withErrorMessage)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<TITLE>Update Item</TITLE>");

        if (itemID == null) {
            out.println(
                    "<div class=\"alert alert-danger\" role=\"alert\"> Wrong path. Needs an Id as parameter </div>");
            return;
        }
        if (withErrorMessage)
            out.println("<div class=\"alert alert-danger\" role=\"alert\"> Something wrong happened, Try Again </div>");

        Item item = itemEJB.read(itemID);

        if (item == null) {
            out.println("<div class=\"alert alert-danger\" role=\"alert\"> You can't access that item</div>");
            return;
        }

        out.println("<div class=\"d-flex justify-content-center align-items-center container\">");
        out.println("<form method=post enctype='multipart/form-data'>");
        out.println("<div class =\"form-group\">");
        out.println("<input name=itemID type=hidden value=" + itemID + ">");
        out.println("<label for=\"name\"> Name </label> <Input type=text class=form-control id=name name=name value=\""
                + item.getName() + "\"  required style=\"width: 300px;\"></div>");

        out.println("<div class =\"form-group\">");
        out.println("<label for=\"category\"> Category </label> <Input type=text class=form-control value=\""
                + item.getCategory() + "\" name=category required style=\"width: 300px;\"></div>");

        out.println("<div class =\"form-group\">");
        out.println("<label for=\"country\"> Country </label> <Input type=text class=form-control value=\""
                + item.getCountry() + "\" name=country required  style=\"width: 300px;\"></div>");

        out.println("<div class =\"form-group\">");
        out.println("<label for=\"price\"> Price </label> <Input type=number class=form-control value=\""
                + item.getPrice() + "\" name=price  required style=\"width: 300px;\"></div>");

        out.println("<br>File: <input type=\"file\" name=\"file\"/><br>");

        out.println("<button type=\"submit\" class=\"btn btn-primary\">Submit</button>");
        out.println("<a href=\"/projeto2-web/profile/user\" class=\"btn btn-primary\">Back</a>");
        out.println("</form></div>");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.header(request, response);
        super.checkLogin(request, response);
        String itemID = request.getParameter("id");
        itemForm(itemID, response, false);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.checkLogin(request, response);
        super.header(request, response);
        response.setContentType("text/html");
        String itemId = request.getParameter("id");

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
                itemForm(itemId, response, true);
                return;
            }
        }

        Item item = itemEJB.read(itemId);
        item.setName(request.getParameter("name"));
        item.setCategory(request.getParameter("category"));
        item.setCountry(request.getParameter("country"));
        String p = request.getParameter("price");
        if (p.contains(".") || Integer.parseInt(p) < 0) {
            itemForm(itemId, response, true);
            return;
        }
        item.setPrice(Integer.parseInt(p));
        if (image != null) {
            try {
                item.setFilename(fileName);
                item.setPhoto(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (itemEJB.update_criteria(item))
            response.sendRedirect(request.getContextPath() + "/profile/user");
        else
            itemForm(itemId, response, true);

        // Old way of update
        /*
         * HashMap<String, Object> params = new HashMap<String, Object>();
         * params.put("name", request.getParameter("name")); params.put("category",
         * request.getParameter("category")); params.put("country",
         * request.getParameter("country")); String p = request.getParameter("price");
         * if (p.contains(".")) { itemForm(itemId, response, true); return; }
         * params.put("price", p);
         * 
         * if(image != null){ try{ params.put("filename", fileName); //String photo =
         * new String(image.getBytes(1l, (int) image.length())); params.put("photo",
         * image); }catch (Exception e){ e.printStackTrace(); } }
         * 
         * if (itemEJB.update(itemId, params))
         * response.sendRedirect(request.getContextPath() + "/"); else itemForm(itemId,
         * response, true);
         */
    }
}
