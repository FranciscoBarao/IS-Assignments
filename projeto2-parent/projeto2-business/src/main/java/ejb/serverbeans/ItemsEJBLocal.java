package ejb.serverbeans;

import javax.ejb.Local;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

import ejb.serverbeans.*;
import data.User;
import data.Item;

@Local
public interface ItemsEJBLocal {
    public boolean delete(String id);

    public boolean delete_all(String userId);

    public Item read(String id);

    public boolean create(String name, String category, String country, User user);

    public boolean update(String id, HashMap<String, String> updateParams);
}
