package ca.uwaterloo.sh6choi.czshopper.events;

import java.util.List;

import ca.uwaterloo.sh6choi.czshopper.model.Item;

/**
 * Created by Samson on 2015-10-15.
 */
public class ItemsFetchedEvent {

    private List<Item> mItems;

    public ItemsFetchedEvent(List<Item> items) {
        mItems = items;
    }

    public List<Item> getItems() {
        return mItems;
    }
}
