package ca.uwaterloo.sh6choi.czshopper.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.uwaterloo.sh6choi.czshopper.database.ItemDataSource;

/**
 * Created by Samson on 2015-10-18.
 */
public class ItemSet {
    private static ItemSet sInstance;

    private List<String> mCategories;
    private HashMap<String, List<Item>> mItemMap;

    public static ItemSet getInstance() {
        if (sInstance == null) {
            sInstance = new ItemSet();
        }
        return sInstance;
    }

    private ItemSet() {
        mCategories = new ArrayList<>();
        mItemMap = new HashMap<>();
    }

    public List<String> getCategories() {
        return mCategories;
    }

    public void setCategories(List<String> categories) {
        mCategories = categories;
    }

    public HashMap<String, List<Item>> getItemMap() {
        return mItemMap;
    }

    public void setItemMap(HashMap<String, List<Item>> itemMap) {
        mItemMap = itemMap;
    }

    public int getCount() {
        int count = 0;
        for (String category : mCategories) {
            count += mItemMap.get(category).size();
        }

        return count;
    }

    public void addItem(Item item) {
        if (mItemMap.containsKey(item.getCategory())) {
            List<Item> itemList = new ArrayList<>();
            itemList.add(item);
            mItemMap.put(item.getCategory(), itemList);
        } else {
            mItemMap.get(item.getCategory()).add(item);
        }
    }

    public boolean removeItem(Item item) {
        List<Item> itemList = mItemMap.get(item.getCategory());

        if (itemList.contains(item)) {
            itemList.remove(item);
        } else {
            return false;
        }

        if (itemList.size() == 0) {
            mItemMap.remove(item.getCategory());
            mCategories.remove(item.getCategory());
        }
        return true;
    }

    public void update(ItemDataSource dataSource, ItemDataSource.ItemsPresenter presenter) {
        dataSource.queryItems(presenter);
    }

}
