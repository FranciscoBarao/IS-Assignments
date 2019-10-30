package ejb.serverbeans;

import javax.ejb.Stateless;

@Stateless(name = "HelloWorld")
public class helloWorldBean implements sessionBean {

    // injects the session context into the remote bean.
    public String getHelloWorld() {
        return "Hello World";
    }
}