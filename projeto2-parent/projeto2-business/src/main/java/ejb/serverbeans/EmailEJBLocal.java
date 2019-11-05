package ejb.serverbeans;

import javax.ejb.Local;

@Local
public interface EmailEJBLocal {
    public void createTimer(long duration);
}
