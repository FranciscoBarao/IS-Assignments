
package ejb.serverbeans;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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

    public boolean edit(String email, String password, String name, String country) {
        User user = new User(email, password, name, country);
        try {
            em.getTransaction().begin();

            User u = em.find(User.class, email);
            u.setPassword(password);
            u.setName(name);
            u.setCountry(country);
            // log.info("Before commit");
            em.getTransaction().commit();
            em.close();

            return true;
        } catch (EntityExistsException e) {
        }
        return false;
    }

}
