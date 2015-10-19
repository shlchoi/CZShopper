package ca.uwaterloo.sh6choi.czshopper.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;

import ca.uwaterloo.sh6choi.czshopper.R;
import ca.uwaterloo.sh6choi.czshopper.model.Item;
import ca.uwaterloo.sh6choi.czshopper.model.ItemSet;

/**
 * Created by Samson on 2015-10-15.
 */
public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ItemViewHolder.OnItemClickListener, CategoryViewHolder.OnCategoryClickListener {

    private OnItemClickListener mOnItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_category_header, parent, false);
            return new CategoryViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;

            String category = getCategoryNameForPosition(position);
            categoryViewHolder.getTextView().setText(category);
            holder.itemView.setTag(category);
            categoryViewHolder.setOnCategoryClickListener(this);

        } else {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            Item item = getItemForPosition(position);
            itemViewHolder.getTextView().setText(item.getName());

            holder.itemView.setTag(item);
            itemViewHolder.setOnItemClickListener(this);
        }
    }

    @Override
    public int getItemViewType(int position) {
        HashMap<String, List<Item>> itemMap = ItemSet.getInstance().getItemMap();
        List<String> categories = ItemSet.getInstance().getCategories();

        int remaining = position;
        for (int i = 0; i < categories.size(); i ++) {
            remaining --;
            if (remaining < 0) {
                return 0;
            } else if (itemMap.get(categories.get(i)).size() > remaining) {
                return 1;
            } else {
                remaining -= itemMap.get(categories.get(i)).size();
            }
        }
        return 0;
    }

    public void addItem(Item item) {
        ItemSet.getInstance().addItem(item);
    }

    public boolean removeItem(Item item) {
        return ItemSet.getInstance().removeItem(item);
    }

    @Override
    public int getItemCount() {
        return ItemSet.getInstance().getCount() + ItemSet.getInstance().getCategories().size();
    }

    private Item getItemForPosition(int position) {
        HashMap<String, List<Item>> itemMap = ItemSet.getInstance().getItemMap();
        List<String> categories = ItemSet.getInstance().getCategories();

        int remaining = position;
        for (int i = 0; i < categories.size(); i ++) {
            remaining --;
            if (remaining < 0) {
                return null;
            } else if (itemMap.get(categories.get(i)).size() > remaining) {
                return itemMap.get(categories.get(i)).get(remaining);
            } else {
                remaining -= itemMap.get(categories.get(i)).size();
            }
        }
        return null;
    }

    public String getCategoryNameForPosition(int position) {
        HashMap<String, List<Item>> itemMap = ItemSet.getInstance().getItemMap();
        List<String> categories = ItemSet.getInstance().getCategories();

        int remaining = position;
        for (int i = 0; i < categories.size(); i ++) {
            remaining --;
            if (remaining < 0) {
                return categories.get(i);
            } else {
                remaining -= itemMap.get(categories.get(i)).size();
            }
        }
        return null;
    }

    private int getItemPosition(Item item) {
        HashMap<String, List<Item>> itemMap = ItemSet.getInstance().getItemMap();
        List<String> categories = ItemSet.getInstance().getCategories();

        int position = -1;
        if (itemMap.containsKey(item.getCategory()) && itemMap.get(item.getCategory()).contains(item)) {
            position ++;
            for (int i = 0; i < categories.indexOf(item.getCategory()); i ++) {
                position += (itemMap.get(categories.get(i)).size() + 1);
            }

            position ++;
            position += categories.indexOf(item);
        }
        return position;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onItemClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view);
        }
    }

    @Override
    public void onCategoryClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onCategoryClick(view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view);
        void onCategoryClick(View view);
    }
}

