package com.projectis.assign_2;

import javax.ejb.Stateless;

@Stateless
public class statelessEJB implements sessionBeanRemote {

    int x;

    /*
     * public statelessEJB() { }
     * 
     * public void xplus(int x) { this.x += x; }
     * 
     * public int getX() { return x; }
     */
    public String getHelloWorld() {
        return "Hello World";
    }
}