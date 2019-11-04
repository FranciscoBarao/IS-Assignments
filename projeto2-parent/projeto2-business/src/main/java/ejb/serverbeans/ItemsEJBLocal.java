package ejb.serverbeans;

import java.util.*;
import javax.ejb.Local;

import data.Item;
import data.User;

@Local
public interface ItemsEJBLocal {
    public boolean delete(String id);

    public boolean delete_all(String userId);

    public Item read(String id);

    public boolean create(String name, String category, String country, int price, Date date, User user);

    public boolean update(String id, HashMap<String, String> updateParams);

    public List<Item> search(String name, String category, int minPrice, int maxPrice, String inCountry,
            Date afterDate);

    public List<Item> searchByUser(String userId);

    public List<Item> sort(List<Item> items, String method, boolean isAscending);
}
