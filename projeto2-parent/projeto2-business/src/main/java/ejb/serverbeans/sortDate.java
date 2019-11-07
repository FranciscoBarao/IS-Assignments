package ejb.serverbeans;

import java.util.*;
import data.Item;

public class sortDate implements Comparator<Item> {
    // Sorting by date Ascending
    @Override
    public int compare(Item a, Item b) {
        if (a.getDate().before(b.getDate()))
            return -1;
        else if (a.getDate().after(b.getDate()))
            return 1;
        else
            return 0;
    }

}
