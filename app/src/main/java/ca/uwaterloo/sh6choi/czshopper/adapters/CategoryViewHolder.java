package ca.uwaterloo.sh6choi.czshopper.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ca.uwaterloo.sh6choi.czshopper.R;

/**
 * Created by Samson on 2015-10-19.
 */
public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView mTextView;

    private OnCategoryClickListener mOnCategoryClickListener;

    public CategoryViewHolder(View v) {
        super(v);
        mTextView = (TextView) v.findViewById(R.id.category_name);
        v.setOnClickListener(this);
    }

    public TextView getTextView() {
        return mTextView;
    }

    public void setOnCategoryClickListener(OnCategoryClickListener onItemClickListener) {
        mOnCategoryClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.category_layout) {
            if (mOnCategoryClickListener != null) {
                mOnCategoryClickListener.onCategoryClick(v);
            }
        }
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(View view);
    }
}
