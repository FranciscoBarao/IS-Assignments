package ejb.serverbeans;

import javax.ejb.Local;

@Local
public interface sessionBean {

    String getHelloWorld();
}