
package ejb.serverbeans;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Blob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;

import data.User;
import data.Item;

@Stateless
public class ItemsEJB implements ItemsEJBLocal {
    @PersistenceContext(name = "Items")
    EntityManager em;

    private static Logger LOGGER = LoggerFactory.getLogger(ItemsEJB.class);

    public ItemsEJB() {
    }

    // Create item
    public boolean create(String name, String category, String country, int price, Date date, Blob photo, String filename, User user) {
        LOGGER.debug("Creating Item");

        Item item = new Item(name, category, country, price, date, photo, filename, user);
        try {
            LOGGER.debug("Persisting Item  = {}", item);
            em.persist(item);
            return true;
        } catch (EntityExistsException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    // Reads item by id
    public Item read(String id) {
        LOGGER.info("Reading Item");

        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i WHERE id= '" + id + "'", Item.class);
        Item result = null;
        try {
            LOGGER.debug("Getting Item result from database with id = {}", id);
            result = query.getSingleResult();
        } catch (NoResultException ne) {
            LOGGER.error(ne.getMessage(), ne);
        }
        return result;
    }

    // Reads items with search parameters
    public List<Item> search(String name, String category, int minPrice, int maxPrice, String inCountry,
            String afterDate) {

        LOGGER.info("Searching Items");

        String query = "FROM Item i WHERE";

        if (!name.equals("") && name != null)
            query += " AND name LIKE '%" + name + "%' ";

        if (!category.equals("") && name != null)
            query += " AND category LIKE '%" + category + "%' ";

        if (minPrice != 0)
            query += " AND price >= " + minPrice + " ";

        if (maxPrice != 0)
            query += " AND price <= " + maxPrice + " ";

        if (!inCountry.equals("") && inCountry != null)
            query += " AND country = '" + inCountry + "' ";

        if (afterDate != null)
            query += " AND date > '" + afterDate + "' ";

        String lastWord = query.substring(query.lastIndexOf(" ") + 1);
        String s = "";
        if (lastWord.equals("WHERE"))
            s = query.replaceFirst(Pattern.quote("WHERE"), "");
        else
            s = query.replaceFirst(Pattern.quote("AND"), "");

        Query q = em.createQuery(s, Item.class);

        try {
            LOGGER.debug("Getting Items list result from database");

            List<Item> results = q.getResultList();
            return results;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    // Reads items of User
    public List<Item> searchByUser(String userId) {
        LOGGER.info("Searching Items By User");

        String query = "FROM Item i WHERE user_id=" + userId;
        Query q = em.createQuery(query, Item.class);

        try {
            LOGGER.debug("Getting items list result from Database with userId = {}", userId);
            List<Item> results = q.getResultList();
            return results;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    // Reads 3 most recent items
    public List<Item> searchRecentItems() {
        LOGGER.info("Searching 3 most recent items");

        String query = "FROM Item i ORDER BY id DESC";
        Query q = em.createQuery(query, Item.class);
        q.setMaxResults(3);

        try {
            LOGGER.debug("Getting items list result from Database");
            List<Item> results = q.getResultList();
            return results;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    // Checks if item belongs to user
    public boolean checkUserItem(String itemId, int userId) {
        LOGGER.info("Checking ownership of item");

        String query = "FROM Item i WHERE user_id=" + userId + " AND itemId=" + itemId;
        Query q = em.createQuery(query, Item.class);

        try {
            LOGGER.debug("Getting item result from Database with userId = {} and itemId = {}", userId, itemId);
            q.getSingleResult();
            return true;
        } catch (NoResultException ne) {
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    // Sort list of items
    public List<Item> sort(List<Item> items, String method, boolean isAscending) {
        LOGGER.info("Sorting Item list");

        LOGGER.debug("Sorting by {}", method);
        switch (method) {
        case "nameSort":
            if (isAscending)
                Collections.sort(items, new sortName());
            else
                Collections.sort(items, new sortName().reversed());
            break;
        case "priceSort":
            if (isAscending)
                Collections.sort(items, new sortPrice());
            else
                Collections.sort(items, new sortPrice().reversed());
            break;
        case "dateSort":
            if (isAscending)
                Collections.sort(items, new sortDate());
            else
                Collections.sort(items, new sortDate().reversed());
            break;
        default:
            if (isAscending)
                Collections.sort(items, new sortName());
            break;
        }
        return items;
    }

    // Update an item
    public boolean update(String id, HashMap<String, Object> updateParams) {
        LOGGER.info("Updating Item");

        try {
            String sqlString = "UPDATE Item SET ";
            Iterator it = updateParams.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                if (pair.getKey().equals("price")) {
                    sqlString += pair.getKey() + " = " + pair.getValue() + ", ";
                } else {
                    sqlString += pair.getKey() + " = '" + pair.getValue() + "', ";
                }
                it.remove(); // avoids a ConcurrentModificationException
            }
            sqlString = sqlString.substring(0, sqlString.length() - 2);
            sqlString += " WHERE id  = '" + id + "'";

            LOGGER.debug("Updating Item on database with id = {}", id);
            em.createQuery(sqlString).executeUpdate();

            return true;
        } catch (EntityExistsException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    // Delete an item
    public boolean delete(String id) {
        LOGGER.info("Deleting Item");
        Query query = em.createQuery("DELETE FROM Item c WHERE c.id = " + id);
        try {
            LOGGER.debug("Deleting Item from database with id={}", id);
            query.executeUpdate();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    // Delete all Items of an User
    public boolean deleteAll(String userId) {
        LOGGER.info("Deleting all Items from userId");

        Query query = em.createQuery("DELETE FROM Item WHERE user_id = " + userId);
        try {
            LOGGER.debug("Deleting Items from database with userId = {}", userId);

            query.executeUpdate();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
        return true;
    }
}
