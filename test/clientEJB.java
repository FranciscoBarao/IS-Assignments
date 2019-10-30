package test.clientejb;

import ejb.serverbeans;
import javax.naming.InitialContext;
import Exception.*;
import java.io.FileInputStream;

public class EJBTester {
    InitialContext ctx;
    {
        try {
            ctx = new InitialContext();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {

        EJBTester ejbTester = new EJBTester();

        ejbTester.testStatelessEjb();
    }

    private void testStatelessEjb() {
        try {
            SessionBean bean = (SessionBean) ctx.lookup("SessionBean/local");
            System.out.println(bean.getHellowWorld());

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}