
package ejb.serverbeans;

import javax.ejb.Local;
import java.util.HashMap;
import data.User;

@Local
public interface UsersEJBLocal {
    public User login(String email, String password);

    public User select(String email);

    public boolean register(String email, String password, String name, String country);

    public boolean edit(String id, HashMap<String, String> updateParams);

    public boolean delete(String id);
}
