package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Blob;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.*;
import ejb.serverbeans.ItemsEJBLocal;

@WebServlet("/images")
public class Images extends HttpServlet {

    @EJB
    ItemsEJBLocal itemEJB;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            String itemID = request.getParameter("id");
            Item item = itemEJB.read(itemID);
            Blob blob = item.getPhoto();
            int blobLength = (int) blob.length();  
            byte[] content = blob.getBytes(1, blobLength);
            response.setContentType(getServletContext().getMimeType("teste.jpg"));
            response.setContentLength(content.length);
            response.getOutputStream().write(content);
        }catch (Exception e) {
            //TODO: handle exception
        }
    }

}