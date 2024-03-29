
package ejb.serverbeans;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.*;
import data.User;

@Stateless
public class UsersEJB implements UsersEJBLocal {
    @PersistenceContext(name = "Players")
    EntityManager em;

    @EJB
    ItemsEJBLocal itemEJB;

    private static Logger LOGGER = LoggerFactory.getLogger(UsersEJB.class);

    public UsersEJB() {
    }

    // Login user
    public User login(String email, String password) {
        LOGGER.info("Logging an User");

        LOGGER.debug("Check for hashed password");
        byte[] salt = new byte[16];
        String pass = hash(password, salt);

        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE email= '" + email + "' AND password = '" + pass + "'", User.class);

        User result = null;
        try {
            LOGGER.debug("Getting user result from database with email = {}", email);
            result = query.getSingleResult();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }

        return result;
    }

    // Create user
    public boolean register(String email, String password, String name, String country) {
        LOGGER.info("Registering an user");

        if (read(email) != null) // User with that email already exists
            return false;

        LOGGER.debug("Hashing password");
        byte[] salt = new byte[16];
        String pass = hash(password, salt);
        LOGGER.debug("Password hashed");

        User user = new User(email, pass, name, country);
        try {
            LOGGER.debug("Persisting User = {}", user);

            em.persist(user);
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    // Reads user by email
    public User read(String email) {
        LOGGER.info("Selecting a User");

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE email= '" + email + "'", User.class);

        User result = null;
        try {
            LOGGER.debug("Getting user result from database with email = {}", email);
            result = query.getSingleResult();
        } catch (NoResultException ne) {
            LOGGER.debug("No user found");
            return null;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return result;
    }

    // Reads all users for sending email
    public List<User> selectAllUsers() {
        LOGGER.info("Selecting all users");

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);

        List<User> result = null;
        try {
            LOGGER.debug("Getting users list result from database");
            result = query.getResultList();
        } catch (NoResultException ne) {
            LOGGER.error(ne.getMessage(), ne);

            return null;
        }

        return result;
    }

    // Updates user
    public boolean update(String email, HashMap<String, String> updateParams) {
        LOGGER.info("Editing an user");

        if (email.equals(updateParams.get("email")) && read(updateParams.get("email")) == null)
            return false;

        try {
            String sqlString = "UPDATE User SET ";
            Iterator it = updateParams.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                sqlString += pair.getKey() + " = '" + pair.getValue() + "', ";
                it.remove(); // avoids a ConcurrentModificationException
            }
            sqlString = sqlString.substring(0, sqlString.length() - 2);
            sqlString += " WHERE email  = '" + email + "'";

            LOGGER.debug("Updating User in database with email = {}", email);
            em.createQuery(sqlString).executeUpdate();

            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    // Deletes a user and all its items
    public boolean delete(String id) {
        LOGGER.info("Deleting an user");

        if (!itemEJB.deleteAll(id))
            return false;

        Query query = em.createQuery("DELETE FROM User c WHERE c.id = '" + id + "'");
        try {
            LOGGER.debug("Deleting User from database with id = {}", id);
            query.executeUpdate();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    // Hashing function for password
    protected String hash(String passwordToHash, byte[] salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
