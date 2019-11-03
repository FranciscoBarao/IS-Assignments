package ejb.serverbeans;

import java.util.*;

import javax.ejb.Stateless;

import data.Item;

@Stateless
public class sortPrice implements Comparator<Item> {
    // Ascending
    @Override
    public int compare(Item a, Item b) {
        return a.getPrice() - b.getPrice();
    }
}
