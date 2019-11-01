
package ejb.serverbeans;

import javax.ejb.Local;

@Local
public interface UsersEJBLocal {
    public boolean login(String email, String password);

    public boolean register(String email, String password, String name, String country);

    public boolean edit(String email, String password, String name, String country);
}
