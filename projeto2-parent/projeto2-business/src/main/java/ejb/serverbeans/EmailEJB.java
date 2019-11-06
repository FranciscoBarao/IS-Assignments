
package ejb.serverbeans;

import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.SessionContext;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import data.User;

@Stateless
public class EmailEJB implements EmailEJBLocal {

    @EJB
    UsersEJBLocal userEJB;

    public EmailEJB() {

    }
    public void sendMail() { 
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.googlemail.com");
        properties.put("mail.from", "info.projecto.2.mail@gmail.com"); 
        properties.setProperty("mail.transport.protocol", "smtp");     
        properties.setProperty("mail.host", "smtp.gmail.com");  
        properties.put("mail.smtp.auth", "true");  
        properties.put("mail.smtp.port", "465");  
        properties.put("mail.debug", "true");  
        properties.put("mail.smtp.socketFactory.port", "465");  
        properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
        properties.put("mail.smtp.socketFactory.fallback", "false"); 
        List<User> users = userEJB.selectAllUsers(); Address[] addresses = new
        Address[users.size()]; 
        int i = 0; 
        for (User u : users) { 
            String to = u.getEmail(); 
            Address toAddress; 
            try { 
                toAddress = new InternetAddress(to);
                addresses[i] = toAddress; 
            } catch (AddressException e) { 
                e.printStackTrace();
            }
            i++; 
        } 
        try { 
            Session session = Session.getDefaultInstance(properties, new
                    javax.mail.Authenticator() { protected PasswordAuthentication
                        getPasswordAuthentication() { return new
                            PasswordAuthentication("info.projecto.2.mail@gmail.com", "ohobbiteumbanana");
            }});
            MimeMessage message = new MimeMessage(session);
            message.setFrom();

            message.addRecipients(Message.RecipientType.TO, addresses); 
            message.setSubject("This is the Subject Line!");
            message.setText("This is actual message");
            Transport.send(message); 
        } catch (MessagingException mex) {
            mex.printStackTrace(); 
        } 
    }
}
