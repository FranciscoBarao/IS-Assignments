
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

    public boolean login(String email, String password) {
        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE email= '" + email + "' AND password = '" + password + "'", User.class);

        try {
            User result = query.getSingleResult();
        } catch (NoResultException ne) {
            return false;
        }

        return true;
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
}
