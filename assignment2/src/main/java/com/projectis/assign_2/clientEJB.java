package com.projectis.assign_2;

import com.projectis.assign_2.*;
import java.io.BufferedReader;
import java.util.Properties;
import java.util.Scanner;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class clientEJB {
    BufferedReader brConsoleReader = null;
    Properties props;
    InitialContext ctx;

    public static void main(String[] args) throws NamingException {
        InitialContext ctx = new InitialContext();

        clientEJB tester = new clientEJB();

        tester.testStatelessEjb();
    }

    private void showGUI() {
        System.out.println("I am god\n 1 -> exit");
    }

    private void testStatelessEjb() {
        Scanner sc = new Scanner(System.in);
        String input;
        try {
            // WHAT DA FUCK DO I PUT ON LOOKUP
            sessionBeanRemote bean = (sessionBeanRemote) ctx.lookup("sessionBeanRemote");
            while (true) {
                showGUI();
                input = sc.nextLine();
                if (Integer.parseInt(input) != 1) {
                    System.out.print("value\n");
                    int x = Integer.parseInt(sc.nextLine());
                    bean.xplus(x);
                } else
                    break;
            }

            int y = bean.getX();
            System.out.println("Y -> " + y);

            sessionBeanRemote bean1 = (sessionBeanRemote) ctx.lookup("sessionBeanRemote/remote");
            int z = bean1.getX();
            System.out.println("Z -> " + z);

        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}