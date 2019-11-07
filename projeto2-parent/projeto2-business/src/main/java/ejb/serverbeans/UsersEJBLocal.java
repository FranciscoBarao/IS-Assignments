
package ejb.serverbeans;

import javax.ejb.Local;
import java.util.HashMap;
import java.util.List;

import data.User;

@Local
public interface UsersEJBLocal {
    public User login(String email, String password);

    public User read(String email);

    public List<User> selectAllUsers();

    public boolean register(String email, String password, String name, String country);

    public boolean update(String id, HashMap<String, String> updateParams);

    public boolean delete(String id);
}
