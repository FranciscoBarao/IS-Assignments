package com.assign_1;

import java.io.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;

@XmlRootElement(name = "list")
public class OwnerList implements Serializable {
    private static final long serialversionUID = 1L;

    @XmlElement(name = "owner")
    private ArrayList<Owner> owners;

    OwnerList() {
        this.owners = new ArrayList<>();
    }

    OwnerList(ArrayList<Owner> list) {
        this.owners = list;
    }

    public String getOwnersStr() {
        String ans = "";
        for (Owner o : owners) {
            ans += o.toString() + " ";
        }
        return ans.substring(0, ans.length() - 1);
    }

    public void addOwner(Owner owner) {
        owners.add(owner);
    }

    public ArrayList<Owner> getOwners() {
        return owners;
    }
}
