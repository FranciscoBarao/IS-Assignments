package ejb.serverbeans;

import java.util.*;
import data.Item;

public class sortPrice implements Comparator<Item> {
    // Sorting by Price Ascending
    @Override
    public int compare(Item a, Item b) {
        return a.getPrice() - b.getPrice();
    }
}
