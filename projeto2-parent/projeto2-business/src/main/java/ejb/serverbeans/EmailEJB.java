
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
import javax.ejb.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.Item;
import data.User;

@Singleton
@Startup
public class EmailEJB {

    @EJB
    UsersEJBLocal userEJB;

    @EJB
    ItemsEJBLocal itemEJB;

    private static Logger LOGGER = LoggerFactory.getLogger(ItemsEJB.class);

    public EmailEJB() {

    }

    // Sends email with 3 most recent items to all users every 5 minutes
    // @Schedule(second = "0", minute = "*/5", hour = "*", persistent = false)
    @Schedule(second="0", minute = "*/5", hour = "*", persistent = false)
    public void sendMail() {
        LOGGER.info("Sending Mail Function");

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.googlemail.com");
        properties.put("mail.from", "info.projecto.2.mail@gmail.com");
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.host", "smtp.gmail.com");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.debug", "true");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        List<User> users = userEJB.selectAllUsers();
        Address[] addresses = new Address[users.size()];
        int i = 0;
        for (User u : users) {

            String to = u.getEmail();
            LOGGER.debug("Adding email {} to Adress List", to);

            Address toAddress;
            try {
                toAddress = new InternetAddress(to);
                addresses[i] = toAddress;
            } catch (AddressException e) {
                LOGGER.error(e.getMessage(), e);
                e.printStackTrace();
            }
            i++;
        }

        try {
            Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("info.projecto.2.mail@gmail.com", "ohobbiteumbanana");
                }
            });
            MimeMessage message = new MimeMessage(session);
            message.setFrom();

            message.addRecipients(Message.RecipientType.TO, addresses);
            message.setSubject("Most Recent Items");

            String s = "Most recent Items!\n";
            List<Item> items = itemEJB.searchRecentItems();
            for (Item j : items)
                s += "Item: " + j.toString() + "\n";

            message.setText(s);

            LOGGER.debug("Sending emails");
            Transport.send(message);
        } catch (MessagingException mex) {
            LOGGER.error(mex.getMessage(), mex);
            mex.printStackTrace();
        }
    }
}
