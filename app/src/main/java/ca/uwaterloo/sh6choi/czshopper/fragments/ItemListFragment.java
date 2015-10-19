package ca.uwaterloo.sh6choi.czshopper.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.uwaterloo.sh6choi.czshopper.R;
import ca.uwaterloo.sh6choi.czshopper.activities.MainActivity;
import ca.uwaterloo.sh6choi.czshopper.adapters.ItemAdapter;
import ca.uwaterloo.sh6choi.czshopper.database.ItemDataSource;
import ca.uwaterloo.sh6choi.czshopper.model.Item;
import ca.uwaterloo.sh6choi.czshopper.model.ItemSet;
import ca.uwaterloo.sh6choi.czshopper.services.AddItemWebIntentService;
import ca.uwaterloo.sh6choi.czshopper.services.DeleteItemWebIntentService;
import ca.uwaterloo.sh6choi.czshopper.services.FetchItemWebIntentService;
import ca.uwaterloo.sh6choi.czshopper.services.UpdateItemWebIntentService;

/**
 * Created by Samson on 2015-10-15.
 */
public class ItemListFragment extends Fragment implements DrawerFragment, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, ItemDataSource.ItemsPresenter, ItemAdapter.OnItemClickListener {

    private static final String TAG = ItemListFragment.class.getCanonicalName();
    public static final String FRAGMENT_TAG = MainActivity.TAG + ".fragment.item_list";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mItemListRecyclerView;
    private ItemAdapter mItemAdapter;

    private ItemDataSource mItemDataSource;

    private LinearLayout mAddEditLinearLayout;
    private EditText mAddEditNameEditText;
    private EditText mAddEditCategoryEditText;
    private Button mAddEditSave;
    private Button mAddEditCancel;
    private EditMode mEditMode = EditMode.NONE;
    private Item mEditItem;

    private FloatingActionButton mAddItemFab;

    private BroadcastReceiver mSuccessReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ItemSet.getInstance().update(mItemDataSource, ItemListFragment.this);
        }
    };

    private BroadcastReceiver mErrorReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ItemSet.getInstance().update(mItemDataSource, ItemListFragment.this);
        }
    };

    public static ItemListFragment getInstance(Bundle args) {
        ItemListFragment fragment = new ItemListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.item_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipeRefreshOne,
                R.color.swipeRefreshTwo,
                R.color.swipeRefreshThree,
                R.color.swipeRefreshFour);

        mItemListRecyclerView = (RecyclerView) view.findViewById(R.id.item_recycler_view);
        mItemListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mItemAdapter = new ItemAdapter();
        mItemListRecyclerView.setAdapter(mItemAdapter);

        ItemTouchHelper.Callback listItemTouchHelper = new ItemTouchHelper.Callback() {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (viewHolder.itemView.getTag() instanceof Item) {
                    return makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
                } else {
                    return 0;
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.RIGHT || direction == ItemTouchHelper.LEFT) {
                    Item item = (Item) viewHolder.itemView.getTag();

                    Intent intent = new Intent(getActivity(), DeleteItemWebIntentService.class);
                    intent.putExtra(DeleteItemWebIntentService.EXTRA_ITEM, item);
                    getContext().startService(intent);
               }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(listItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(mItemListRecyclerView);
        mItemAdapter.setOnItemClickListener(this);

        mAddEditLinearLayout = (LinearLayout) view.findViewById(R.id.add_edit_layout);

        mAddEditNameEditText = (EditText) view.findViewById(R.id.add_edit_item_name_edittext);
        mAddEditCategoryEditText = (EditText) view.findViewById(R.id.add_edit_item_category_edittext);

        mAddEditSave = (Button) view.findViewById(R.id.add_edit_save_button);
        mAddEditCancel = (Button) view.findViewById(R.id.add_edit_cancel_button);

        mAddItemFab = (FloatingActionButton) view.findViewById(R.id.add_item_fab);

        mAddEditLinearLayout.setOnClickListener(this);
        mAddEditSave.setOnClickListener(this);
        mAddEditCancel.setOnClickListener(this);
        mAddItemFab.setOnClickListener(this);

        onRefresh();
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);

        Intent intent = new Intent(getContext(), FetchItemWebIntentService.class);
        getContext().startService(intent);
    }

    @Override
    public void onItemClick(View view) {
        mEditMode = EditMode.UPDATE;
        mEditItem = (Item) view.getTag();

        mAddEditNameEditText.setText(mEditItem.getName());
        mAddEditCategoryEditText.setText(mEditItem.getCategory());
        showAddEditDialog();
    }

    @Override
    public void onCategoryClick(View view) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_item_fab:
                onAddItemFabClicked();
                break;
            case R.id.add_edit_save_button:
                onAddEditSaveClicked();
                break;
            case R.id.add_edit_cancel_button:
            case R.id.add_edit_layout:
                onAddEditCancelClicked();
                break;
        }
    }

    private void onAddItemFabClicked() {
        mEditMode = EditMode.ADD;
        showAddEditDialog();
    }

    private void onAddEditSaveClicked() {
        if (TextUtils.isEmpty(mAddEditNameEditText.getText())) {
            mAddEditNameEditText.setError("Please enter a name.");
        } else {
            mAddEditNameEditText.setError(null);
        }

        if (TextUtils.isEmpty(mAddEditCategoryEditText.getText())) {
            mAddEditCategoryEditText.setError("Please enter a category.");
        } else {
            mAddEditCategoryEditText.setError(null);
        }

        String name = mAddEditNameEditText.getText().toString();
        String category = mAddEditCategoryEditText.getText().toString();


        if (mEditMode == EditMode.ADD) {
            Item newItem = new Item(category, name);
            Intent intent = new Intent(getContext(), AddItemWebIntentService.class);
            intent.putExtra(AddItemWebIntentService.EXTRA_ITEM, newItem);
            getContext().startService(intent);
        } else {
            if (mEditItem != null) {
                mEditItem.setName(name);
                mEditItem.setCategory(category);
                Intent intent = new Intent(getContext(), UpdateItemWebIntentService.class);
                intent.putExtra(UpdateItemWebIntentService.EXTRA_ITEM, mEditItem);
                getContext().startService(intent);
            }
        }

        //TODO: SHOW MOCK

        hideAddEditDialog();
    }

    private void onAddEditCancelClicked() {
        hideAddEditDialog();
    }

    private void clearFields() {
        mAddEditNameEditText.setText("");
        mAddEditCategoryEditText.setText("");

        mAddEditNameEditText.setError(null);
        mAddEditCategoryEditText.setError(null);
        mEditItem = null;
    }

    private void showAddEditDialog() {
        mAddEditLinearLayout.setVisibility(View.VISIBLE);
        mAddItemFab.setVisibility(View.GONE);
    }

    private void hideAddEditDialog() {
        clearFields();
        mAddEditLinearLayout.setVisibility(View.GONE);
        mAddItemFab.setVisibility(View.VISIBLE);
        hideSoftInputKeyboard();
        mEditMode = EditMode.NONE;
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter successFilter = new IntentFilter();
        successFilter.addAction(FetchItemWebIntentService.ACTION_ITEMS_FETCHED);
        successFilter.addAction(AddItemWebIntentService.ACTION_ITEM_ADDED);
        successFilter.addAction(UpdateItemWebIntentService.ACTION_ITEM_UPDATED);
        successFilter.addAction(DeleteItemWebIntentService.ACTION_ITEM_DELETED);

        IntentFilter errorFilter = new IntentFilter();
        errorFilter.addAction(FetchItemWebIntentService.ACTION_ERROR);
        errorFilter.addAction(AddItemWebIntentService.ACTION_ERROR);
        errorFilter.addAction(UpdateItemWebIntentService.ACTION_ERROR);
        errorFilter.addAction(DeleteItemWebIntentService.ACTION_ERROR);

        getContext().registerReceiver(mSuccessReceiver, successFilter);
        getContext().registerReceiver(mErrorReceiver, errorFilter);

        mItemDataSource = new ItemDataSource(getContext());
        mItemDataSource.open();
        mItemDataSource.queryItems(this);

        onRefresh();
    }

    @Override
    public void onPause() {
        mItemDataSource.close();
        getContext().unregisterReceiver(mSuccessReceiver);

        super.onPause();
    }

    @Override
    public void onItemsFetched() {
        mSwipeRefreshLayout.setRefreshing(false);
        mItemAdapter.notifyDataSetChanged();
    }

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG;
    }

    @Override
    public int getTitleStringResId() {
        return 0;
    }

    @Override
    public boolean shouldShowUp() {
        return false;
    }

    @Override
    public boolean shouldAddToBackstack() {
        return false;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    private void hideSoftInputKeyboard() {
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    private enum EditMode {
        ADD,
        UPDATE,
        NONE
    }
}
