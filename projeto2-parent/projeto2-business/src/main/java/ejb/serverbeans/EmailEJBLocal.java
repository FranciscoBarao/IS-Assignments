package ejb.serverbeans;

import java.util.*;
import javax.ejb.Local;

@Local
public interface EmailEJBLocal {
    public void sendMail();
}
