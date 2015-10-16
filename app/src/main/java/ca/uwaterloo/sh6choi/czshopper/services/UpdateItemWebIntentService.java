package ca.uwaterloo.sh6choi.czshopper.services;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.MalformedURLException;
import java.net.URL;

import ca.uwaterloo.sh6choi.czshopper.R;
import ca.uwaterloo.sh6choi.czshopper.database.ItemDataSource;
import ca.uwaterloo.sh6choi.czshopper.model.Item;

/**
 * Created by Samson on 2015-10-13.
 */
public class UpdateItemWebIntentService extends WebIntentService {
    private static final String TAG = UpdateItemWebIntentService.class.getCanonicalName();

    public static final String EXTRA_ITEM = TAG  + ".extras.item";
    public static final String ACTION_ITEM_UPDATED = TAG + ".action.item_updated";

    private Item mItem;

    public UpdateItemWebIntentService() {
        super("UpdateItemWebIntentService");
    }

    @Override
    protected URL getUrl() throws MalformedURLException {
        return new URL(getString(R.string.url) + String.format(getString(R.string.endpoint_update_item), mItem.getId()));
    }

    @Override
    protected MethodType getMethodType() {
        return MethodType.PUT;
    }

    @Override
    public String getRequestString() {
        return mItem.getJsonString();
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
    public void onResponse(String response) {
        Log.d(TAG, "Response retrieved");
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        Item item = new Gson().fromJson(jsonObject, Item.class);

        ItemDataSource dataSource = new ItemDataSource(this);
        dataSource.open();
        dataSource.updateItem(item);
        dataSource.close();

        sendBroadcast(new Intent(ACTION_ITEM_UPDATED));
        Log.d(TAG, "Item parsed");
    }

    @Override
    public void onError(Exception e) {

    }
}
