
package ejb.serverbeans;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import java.util.*;
import java.util.regex.Pattern;

import data.User;
import data.Item;

@Stateless
public class ItemsEJB implements ItemsEJBLocal {
    @PersistenceContext(name = "Items")
    EntityManager em;

    public ItemsEJB() {
    }

    // Delete an item
    public boolean delete(String id) {
        Query query = em.createQuery("DELETE FROM Items c WHERE c.id = '" + id + "'");
        try {
            int deletedCount = query.executeUpdate();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // Delete all Items of an User
    public boolean delete_all(String userId) {
        Query query = em.createQuery("DELETE FROM Items c WHERE c.user_id = '" + userId + "'");
        try {
            int deletedCount = query.executeUpdate();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Item read(String id) {
        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i WHERE id= '" + id + "'", Item.class);
        Item result = null;
        try {
            result = query.getSingleResult();
        } catch (NoResultException ne) {
            return null;
        }
        return result;
    }

    public List<Item> search(String name, String category, int minPrice, int maxPrice, String inCountry,
            Date afterDate) {

        String query = "FROM Item i WHERE ";

        if (!name.equals("") && name != null)
            query += "AND name LIKE ''%" + name + "%' ";

        if (!category.equals("") && name != null)
            query += "AND category LIKE '% " + category + "%' ";

        if (minPrice != 0)
            query += "AND price > " + minPrice + " ";

        if (maxPrice != 0)
            query += "AND price < " + maxPrice + " ";

        if (!inCountry.equals("") && inCountry != null)
            query += "AND country = '" + inCountry + "' ";

        if (afterDate != null)
            query += "AND date > '" + afterDate + "' ";

        query += ";";

        String s = query.replaceFirst(Pattern.quote("AND"), "");
        System.out.println(s);

        Query q = em.createQuery(s, Item.class);

        try {
            List<Item> results = q.getResultList();
            return results;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public boolean create(String name, String category, String country, int price, Date date, User user) {
        Item item = new Item(name, category, country, price, date, user);
        try {
            em.persist(item);
            return true;
        } catch (EntityExistsException e) {
            // Already exists, need to reintroduce stuff
        }
        return false;
    }

    public boolean update(String id, HashMap<String, String> updateParams) {
        try {
            String sqlString = "UPDATE Items SET ";
            em.getTransaction().begin();
            Iterator it = updateParams.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                sqlString += pair.getKey() + " = " + pair.getValue() + " ";
                it.remove(); // avoids a ConcurrentModificationException
            }
            sqlString += "WHERE id  = '" + id + "'";
            em.createQuery(sqlString).executeUpdate();

            return true;
        } catch (EntityExistsException e) {
        }
        return false;
    }

}
