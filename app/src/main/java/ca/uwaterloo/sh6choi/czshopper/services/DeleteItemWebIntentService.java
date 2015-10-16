package ca.uwaterloo.sh6choi.czshopper.services;

import android.content.Intent;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import ca.uwaterloo.sh6choi.czshopper.R;

/**
 * Created by Samson on 2015-10-13.
 */
public class DeleteItemWebIntentService extends WebIntentService {
    private static final String TAG = DeleteItemWebIntentService.class.getCanonicalName();

    public static final String EXTRA_ITEM_ID = TAG + ".extras.item_id";
    public static final String ACTION_ITEM_DELETED = TAG + ".action.item_deleted";

    private int mItemId = -1;

    public DeleteItemWebIntentService() {
        super("DeleteItemWebIntentService");
    }

    @Override
    protected URL getUrl() throws MalformedURLException {
        return new URL(getString(R.string.url) + String.format(getString(R.string.endpoint_update_item), mItemId));
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mItemId = intent.getIntExtra(EXTRA_ITEM_ID, -1);
        super.onHandleIntent(intent);
    }

    @Override
    protected MethodType getMethodType() {
        return MethodType.DELETE;
    }

    @Override
    public String getRequestString() {
        return null;
    }

    @Override
    public void onResponse(String response) {
        Log.d(TAG, "Response retrieved");
        sendBroadcast(new Intent(ACTION_ITEM_DELETED));
    }

    @Override
    public void onError(Exception e) {

    }
}
