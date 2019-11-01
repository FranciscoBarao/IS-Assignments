
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
public class ItemsEJB implements ItemsEJBLocal {
    @PersistenceContext(name = "Items")
    EntityManager em;

    public ItemsEJB() {
    }

    // Delete an item
    public boolean delete(String id){
        Query query = em.createQuery(
            "DELETE FROM Items c WHERE c.id = '" + id "'");
        int deletedCount = query.executeUpdate();
    }

    // Delete all Items of an User
    public boolean delete_all(String userId){
        Query query = em.createQuery(
            "DELETE FROM Items c WHERE c.user_id = '" + userId "'");
        int deletedCount = query.executeUpdate();
    }

    public Item read(String id) {
        TypedQuery<Item> query = em.createQuery(
                "SELECT i FROM Item i WHERE id= '" + id + "'", Item.class);
        try {
            Item result = query.getSingleResult();
        } catch (NoResultException ne) {
            return null;
        }
        return result;
    }

    public boolean create(String name, String category, String country, User user) {
        Item item = new Item(name, category, country, user);
        try {
            em.persist(item);
            return true;
        } catch (EntityExistsException e) {
            // Already exists, need to reintroduce stuff
        }
        return false;
    }

    public boolean update(Hashmap<String, String> updateParams) {
        try {
            String sqlString = "UPDATE Items SET "; population = population * 11 / 10  +
            "WHERE population < :p")
            em.getTransaction().begin();
            User u = em.find(Item.class, id);
            Iterator it = updateParams.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                sqlString += pair.getKey() + " = " + pair.getValue() + " ";
                it.remove(); // avoids a ConcurrentModificationException
            }
            em.createQuery(sqlString).executeUpdate();

            return true;
        } catch (EntityExistsException e) {
        }
        return false;
    }

}
