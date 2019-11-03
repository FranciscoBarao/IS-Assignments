
package ejb.serverbeans;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

import ejb.serverbeans.*;
import data.User;
import data.Item;

@Stateless
public class UsersEJB implements UsersEJBLocal {
    @PersistenceContext(name = "Players")
    EntityManager em;

    public UsersEJB() {
    }

    public User login(String email, String password) {
        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE email= '" + email + "' AND password = '" + password + "'", User.class);

        User result = null;
        try {
            result = query.getSingleResult();
        } catch (NoResultException ne) {
            return null;
        }

        return result;
    }

    public User select(String email) {
        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE email= '" + email + "'", User.class);

        User result = null;
        try {
            result = query.getSingleResult();
        } catch (NoResultException ne) {
            return null;
        }

        return result;
    }

    public boolean register(String email, String password, String name, String country) {
        User user = new User(email, password, name, country);
        try {
            em.persist(user);
            return true;
        } catch (EntityExistsException e) {
            // Already exists, need to reintroduce stuff
        }
        return false;
    }

    public boolean edit(String email, HashMap<String, String> updateParams){
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
            em.createQuery(sqlString).executeUpdate();

            return true;
        } catch (EntityExistsException e) {
        }
        return false;
    }
}
