
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
import java.util.HashMap;
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
            out.println("Wrong path. Needs an ID as parameter. Please try again.<BR>");
            return;
        }
        if (withErrorMessage)
            out.println("Update failed. Please try again.<BR>");

        Item item = itemEJB.read(itemID);

        if (item == null) {
            out.println("You can't access that item. Please try with another id.<BR>");
            return;
        }

        out.println("<BR>Update Item Form");
        out.println("<BR><form method=post enctype='multipart/form-data'><BR>");
        out.println("<input name=itemID type=hidden value=" + itemID + ">");
        out.println("<BR>Name: <Input TYPE=TEXT VALUE='" + item.getName() + "' required NAME=name>");
        out.println("<BR>Category: <INPUT TYPE=TEXT VALUE='" + item.getCategory() + "' required NAME=category>");
        out.println("<BR>Country: <INPUT TYPE=TEXT VALUE='" + item.getCountry() + "' required NAME=country>");
        out.println("<BR>Price: <input type=number VALUE='" + item.getPrice() + "' step=any required name=price>");
        out.println("<br>File: <input type=\"file\" name=\"file\"/>");
        out.println("<BR><INPUT TYPE=SUBMIT VALUE=Submit></form>");

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
                itemForm(itemId, response, true);
                return;
            }
        }

        Item item = itemEJB.read(itemId);
        item.setName(request.getParameter("name"));
        item.setCategory(request.getParameter("category"));
        item.setCountry(request.getParameter("country"));
        if(image != null){
            try{
                item.setFilename(fileName);
                item.setPhoto(image);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if (itemEJB.update_criteria(item))
            response.sendRedirect(request.getContextPath() + "/");
        else
            itemForm(itemId, response, true);


        // Old way of update
        /*
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("name", request.getParameter("name"));
        params.put("category", request.getParameter("category"));
        params.put("country", request.getParameter("country"));
        String p = request.getParameter("price");
        if (p.contains(".")) {
            itemForm(itemId, response, true);
            return;
        }
        params.put("price", p);

        if(image != null){
            try{
                params.put("filename", fileName);
                //String photo = new String(image.getBytes(1l, (int) image.length()));
                params.put("photo", image);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if (itemEJB.update(itemId, params))
            response.sendRedirect(request.getContextPath() + "/");
        else
            itemForm(itemId, response, true);
        */
    }
}
