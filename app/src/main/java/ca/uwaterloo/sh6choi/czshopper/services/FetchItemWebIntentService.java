package ca.uwaterloo.sh6choi.czshopper.services;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import ca.uwaterloo.sh6choi.czshopper.R;
import ca.uwaterloo.sh6choi.czshopper.database.ItemDataSource;
import ca.uwaterloo.sh6choi.czshopper.model.Item;

/**
 * Created by Samson on 2015-10-13.
 */
public class FetchItemWebIntentService extends WebIntentService {
    private static final String TAG = FetchItemWebIntentService.class.getCanonicalName();

    public static final String ACTION_ITEMS_FETCHED = TAG + ".action.items_fetched";


    public FetchItemWebIntentService() {
        super("FetchItemWebIntentService");
    }

    @Override
    protected URL getUrl() throws MalformedURLException {
        return new URL(getString(R.string.url) + getString(R.string.endpoint_fetch_items));
    }

    @Override
    protected MethodType getMethodType() {
        return MethodType.GET;
    }

    @Override
    public String getRequestString() {
        return null;
    }

    @Override
    public void onResponse(String response) {
        Log.d(TAG, "Response retrieved");
        JsonArray jsonArray = new JsonParser().parse(response).getAsJsonArray();
        Item[] items = new Gson().fromJson(jsonArray, Item[].class);
        ItemDataSource dataSource = new ItemDataSource(this);
        dataSource.open();
        for (Item item : items) {
            dataSource.addItem(item);
        }
        dataSource.close();
        Log.d(TAG, "Items parsed and added");

        sendBroadcast(new Intent(ACTION_ITEMS_FETCHED));
    }

    @Override
    public void onError(Exception e) {

    }
}
