
package ejb.serverbeans;

import javax.ejb.Local;
import data.User;

@Local
public interface UsersEJBLocal {
    public User login(String email, String password);

    public boolean register(String email, String password, String name, String country);

    public boolean edit(String email, String password, String name, String country);
}
