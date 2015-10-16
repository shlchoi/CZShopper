package ca.uwaterloo.sh6choi.czshopper.services;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.MalformedURLException;
import java.net.URL;

import ca.uwaterloo.sh6choi.czshopper.R;
import ca.uwaterloo.sh6choi.czshopper.model.Item;

/**
 * Created by Samson on 2015-10-13.
 */
public class UpdateItemWebIntentService extends WebIntentService {
    private static final String TAG = UpdateItemWebIntentService.class.getCanonicalName();

    public UpdateItemWebIntentService() {
        super("UpdateItemWebIntentService");
    }

    @Override
    protected URL getUrl() throws MalformedURLException {
        return new URL(getString(R.string.url) + String.format(getString(R.string.endpoint_update_item), 1084));
    }

    @Override
    protected MethodType getMethodType() {
        return MethodType.PUT;
    }

    @Override
    public String getRequestString() {
        return new Item("Meat", "Pork").getJsonString();
    }

    @Override
    public void onResponse(String response) {
        Log.d(TAG, "Response retrieved");
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        Item item = new Gson().fromJson(jsonObject, Item.class);
        Log.d(TAG, "Item parsed");
    }

    @Override
    public void onError(Exception e) {

    }
}
