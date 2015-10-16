package ca.uwaterloo.sh6choi.czshopper.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ca.uwaterloo.sh6choi.czshopper.R;
import ca.uwaterloo.sh6choi.czshopper.model.Item;

/**
 * Created by Samson on 2015-10-15.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> implements ItemViewHolder.OnItemClickListener {
    private List<Item> mItems;

    private OnItemClickListener mOnItemClickListener;

    public ItemAdapter(List<Item> items) {
        mItems = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.getTextView().setText(mItems.get(position).getName());

        holder.itemView.setTag(position);
        holder.setOnItemClickListener(this);
    }

    public List<Item> getItems() {
        return mItems;
    }

    public void setItems(List<Item> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public boolean removeItem(Item item) {
        return mItems.remove(item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
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

    public interface OnItemClickListener {
        void onItemClick(View view);
    }
}

