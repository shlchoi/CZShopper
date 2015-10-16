package ca.uwaterloo.sh6choi.czshopper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import ca.uwaterloo.sh6choi.czshopper.R;
import ca.uwaterloo.sh6choi.czshopper.fragments.DrawerFragment;
import ca.uwaterloo.sh6choi.czshopper.fragments.ItemListFragment;
import ca.uwaterloo.sh6choi.czshopper.services.AddItemWebIntentService;
import ca.uwaterloo.sh6choi.czshopper.services.DeleteItemWebIntentService;
import ca.uwaterloo.sh6choi.czshopper.services.FetchItemWebIntentService;
import ca.uwaterloo.sh6choi.czshopper.services.UpdateItemWebIntentService;

/**
 * Created by Samson on 2015-10-13.
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getCanonicalName();

    public static final String ACTION_ITEM_LIST = TAG + ".action.item_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (getIntent() == null || TextUtils.isEmpty(getIntent().getAction())) {
            Intent start = new Intent(ACTION_ITEM_LIST);
            startActivity(start);
        } else {
            handleAction(getIntent());
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!TextUtils.isEmpty(intent.getAction())) {
            handleAction(intent);
            setIntent(intent);
        }
    }

    private void handleAction(Intent intent) {
        String action = intent.getAction();

        if (TextUtils.equals(action, ACTION_ITEM_LIST)) {
            onItemList();
        } else {
            onItemList();
        }
    }

    private void onItemList() {
        if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof ItemListFragment)){
            ItemListFragment fragment = ItemListFragment.getInstance(new Bundle());
            swapFragment(fragment);
        }
    }

    private <T extends Fragment & DrawerFragment> void swapFragment(T fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (fragment.shouldAddToBackstack()) {
            transaction.addToBackStack(fragment.getFragmentTag());
        }

        transaction.replace(R.id.fragment_container, fragment, fragment.getFragmentTag());
        transaction.commit();
    }
}
