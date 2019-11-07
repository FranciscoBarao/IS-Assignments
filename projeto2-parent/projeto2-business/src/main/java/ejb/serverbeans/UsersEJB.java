
package ejb.serverbeans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

import data.Item;
import data.User;

@Stateless
public class UsersEJB implements UsersEJBLocal {
    @PersistenceContext(name = "Players")
    EntityManager em;

    private static Logger LOGGER = LoggerFactory.getLogger(UsersEJB.class);

    public UsersEJB() {
    }

    public User login(String email, String password) {
        LOGGER.debug("Logging an User");

        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE email= '" + email + "' AND password = '" + password + "'", User.class);

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

    public User select(String email) {
        LOGGER.debug("Selecting a User");

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE email= '" + email + "'", User.class);

        User result = null;
        try {
            LOGGER.debug("Getting user result from database with email = {}", email);
            result = query.getSingleResult();
        } catch (NoResultException ne) {
            LOGGER.error(ne.getMessage(), ne);
            return null;
        }

        return result;
    }

    public List<User> selectAllUsers() {
        LOGGER.debug("Selecting all users");

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

    public boolean register(String email, String password, String name, String country) {
        LOGGER.debug("Registering an user");

        User user = new User(email, password, name, country);
        try {
            LOGGER.debug("Persisting User = {}", user);

            em.persist(user);
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean edit(String email, HashMap<String, String> updateParams) {
        LOGGER.debug("Editing an user");

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

    public boolean delete(String id) {
        LOGGER.debug("Deleting an user");

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
}
