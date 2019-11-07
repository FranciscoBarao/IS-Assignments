package ejb.serverbeans;

import java.util.*;
import data.Item;

public class sortName implements Comparator<Item> {
    // Sorting by name Ascending
    @Override
    public int compare(Item a, Item b) {
        if (a.getName().toUpperCase() == b.getName().toUpperCase())
            return 0;

        if (a.getName() == null)
            return -1;

        if (b.getName() == null)
            return 1;

        return a.getName().toUpperCase().compareTo(b.getName().toUpperCase());
    }
}
