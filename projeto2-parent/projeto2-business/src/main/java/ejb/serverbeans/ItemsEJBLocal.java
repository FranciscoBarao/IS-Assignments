package ejb.serverbeans;

import java.util.*;
import javax.ejb.Local;
import java.sql.Blob;

import data.Item;
import data.User;

@Local
public interface ItemsEJBLocal {

    public boolean create(String name, String category, String country, int price, Date date, Blob photo, String filename, User user);

    public Item read(String id);

    public List<Item> search(String name, String category, int minPrice, int maxPrice, String inCountry,
            String afterDate);

    public List<Item> searchRecentItems();

    public boolean checkUserItem(String itemId, int userId);

    public List<Item> searchByUser(String userId);

    public List<Item> sort(List<Item> items, String method, boolean isAscending);

    public boolean update(String id, HashMap<String, String> updateParams);

    public boolean delete(String id);

    public boolean deleteAll(String userId);

}
