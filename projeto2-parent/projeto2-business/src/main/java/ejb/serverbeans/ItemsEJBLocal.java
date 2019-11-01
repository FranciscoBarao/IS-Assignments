package ejb.serverbeans;

import javax.ejb.Local;

@Local
public interface UsersEJBLocal {
    public boolean delete(String id);

    public boolean delete_all(String userId);

    public Item read(String id);

    public boolean create(String name, String category, String country, User user);

    public boolean update(Hashmap<String, String> updateParams);
}
