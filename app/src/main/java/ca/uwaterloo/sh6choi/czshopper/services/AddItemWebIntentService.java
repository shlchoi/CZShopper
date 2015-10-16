package ca.uwaterloo.sh6choi.czshopper.services;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import ca.uwaterloo.sh6choi.czshopper.R;
import ca.uwaterloo.sh6choi.czshopper.database.ItemDataSource;
import ca.uwaterloo.sh6choi.czshopper.model.Item;

/**
 * Created by Samson on 2015-10-13.
 */
public class AddItemWebIntentService extends WebIntentService {
    private static final String TAG = AddItemWebIntentService.class.getCanonicalName();

    public static final String EXTRA_ITEM = TAG  + ".extras.item";
    public static final String ACTION_ITEM_ADDED = TAG + ".action.item_added";

    private Item mItem;

    public AddItemWebIntentService() {
        super("AddItemWebIntentService");
    }

    @Override
    protected URL getUrl() throws MalformedURLException {
        return new URL(getString(R.string.url) + getString(R.string.endpoint_add_item));
    }

    @Override
    protected MethodType getMethodType() {
        return MethodType.POST;
    }

    @Override
    public String getRequestString() {
        return mItem.getJsonString();
    }

    @Override
    public void onResponse(String response) {
        Log.d(TAG, "Response retrieved");
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        Item item = new Gson().fromJson(jsonObject, Item.class);

        ItemDataSource dataSource = new ItemDataSource(this);
        dataSource.open();
        dataSource.addItem(item);
        dataSource.close();

        sendBroadcast(new Intent(ACTION_ITEM_ADDED));

        Log.d(TAG, "Item parsed");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mItem = intent.getParcelableExtra(EXTRA_ITEM);

        if (mItem != null) {
            super.onHandleIntent(intent);
        } else {
            onError(new NullPointerException("No item provided"));
        }
    }

    @Override
    public void onError(Exception e) {

    }
}
