package ejb.serverbeans;

import java.util.HashMap;

import javax.ejb.Local;
import data.Item;
import data.User;

@Local
public interface ItemsEJBLocal {
    public boolean delete(String id);

    public boolean delete_all(String userId);

    public Item read(String id);

    public boolean create(String name, String category, String country, User user);

    public boolean update(HashMap<String, String> updateParams);
}
