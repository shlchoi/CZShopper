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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import ca.uwaterloo.sh6choi.czshopper.R;
import ca.uwaterloo.sh6choi.czshopper.activities.MainActivity;
import ca.uwaterloo.sh6choi.czshopper.adapters.ItemAdapter;
import ca.uwaterloo.sh6choi.czshopper.bus.BusProvider;
import ca.uwaterloo.sh6choi.czshopper.database.ItemDataSource;
import ca.uwaterloo.sh6choi.czshopper.events.ItemsFetchedEvent;
import ca.uwaterloo.sh6choi.czshopper.model.Item;
import ca.uwaterloo.sh6choi.czshopper.services.DeleteItemWebIntentService;
import ca.uwaterloo.sh6choi.czshopper.services.FetchItemWebIntentService;

/**
 * Created by Samson on 2015-10-15.
 */
public class ItemListFragment extends Fragment implements DrawerFragment, SwipeRefreshLayout.OnRefreshListener {

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

    private FloatingActionButton mAddFab;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mItemDataSource.queryItems();
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

        mItemAdapter = new ItemAdapter(new ArrayList<Item>());
        mItemListRecyclerView.setAdapter(mItemAdapter);

        ItemTouchHelper.Callback listItemTouchHelper = new ItemTouchHelper.Callback() {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.RIGHT || direction == ItemTouchHelper.LEFT) {
                    Item item = mItemAdapter.getItems().get(viewHolder.getAdapterPosition());

                    mItemAdapter.removeItem(item);
                    mItemAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                    Intent intent = new Intent(getActivity(), DeleteItemWebIntentService.class);
                    intent.putExtra(DeleteItemWebIntentService.EXTRA_ITEM_ID, item.getId());
                    getContext().startService(intent);

               }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(listItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(mItemListRecyclerView);

        mAddEditLinearLayout = (LinearLayout) view.findViewById(R.id.add_edit_layout);

        mAddEditNameEditText = (EditText) view.findViewById(R.id.add_edit_item_name_edittext);
        mAddEditCategoryEditText = (EditText) view.findViewById(R.id.add_edit_item_category_edittext);

        mAddEditSave = (Button) view.findViewById(R.id.add_edit_save_button);
        mAddEditCancel = (Button) view.findViewById(R.id.add_edit_cancel_button);

        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_item_list, menu);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);

        Intent intent = new Intent(getContext(), FetchItemWebIntentService.class);
        getContext().startService(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(FetchItemWebIntentService.ACTION_ITEMS_FETCHED);
        filter.addAction(DeleteItemWebIntentService.ACTION_ITEM_DELETED);

        getContext().registerReceiver(mBroadcastReceiver, filter);

        BusProvider.getDatabaseBus().register(this);
        mItemDataSource = new ItemDataSource(getContext());
        mItemDataSource.open();


        onRefresh();
    }

    @Override
    public void onPause() {
        mItemDataSource.close();
        BusProvider.getDatabaseBus().unregister(this);
        super.onPause();
    }

    public void onAddItem() {

    }

    @Subscribe
    public void onDatabaseQueried(ItemsFetchedEvent event) {
        mItemAdapter.setItems(event.getItems());
        mSwipeRefreshLayout.setRefreshing(false);
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
}
