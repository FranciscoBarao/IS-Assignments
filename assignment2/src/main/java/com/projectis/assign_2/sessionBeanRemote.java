package com.projectis.assign_2;

import javax.ejb.Local;
import javax.ejb.Remote;

@Remote
public interface sessionBeanRemote {
    // void xplus(int x);

    // int getX();

    String getHelloWorld();
}