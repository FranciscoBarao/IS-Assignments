package com.projectis.assign_2;

import javax.ejb.Stateless;

@Stateless
public class statelessEJB implements sessionBeanRemote {

    public String getHelloWorld() {
        return "Hello World";
    }
}