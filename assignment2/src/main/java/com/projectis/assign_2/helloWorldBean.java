package com.projectis.assign_2;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

@Stateless(name = "HelloWorld")

public class helloWorldBean implements sessionBeanRemote {

    // injects the session context into the remote bean.
    @Resource
    private SessionContext context;

    @Override
    public String getHelloWorld() {
        return "Hello World";
    }
}