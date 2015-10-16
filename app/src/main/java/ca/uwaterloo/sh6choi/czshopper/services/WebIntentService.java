package ca.uwaterloo.sh6choi.czshopper.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Samson on 2015-10-13.
 */
public abstract class WebIntentService extends IntentService {
    private final static String TAG = WebIntentService.class.getCanonicalName();

    public WebIntentService(String name) {
        super(name);
    }

    /** Returns the URL to be used. */
    protected abstract URL getUrl() throws MalformedURLException;

    /** Reads the content of the HTML page */
    private String readStream(InputStream inputStream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return total.toString();
    }

    private void writeStream(OutputStream out) {
        try {
            out.write(getRequestString().getBytes());
        } catch (IOException exception) {
            onError(exception);
        }
    }

    protected abstract MethodType getMethodType();

    protected abstract String getRequestString();

    @Override
    protected void onHandleIntent(Intent intent) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) getUrl().openConnection();
            urlConnection.setRequestProperty("X-CZ-Authorization", "npPhh6Spsh1Lwb2xRDDw");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Content-type", "application/json");
            urlConnection.setRequestMethod(getMethodType().getTypeString());

            if (getMethodType() == MethodType.POST || getMethodType() == MethodType.PUT) {
                urlConnection.setDoOutput(true);

                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                writeStream(out);
                out.flush();
                out.close();
            }

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            onResponse(readStream(in));
        } catch (Exception e) {
            Log.e(TAG, "Could not perform the operation! " + e.getMessage());
            e.printStackTrace();
            onError(e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    public abstract void onResponse(String response);

    public abstract void onError(Exception e);

    public enum MethodType {
        GET ("GET"),
        POST ("POST"),
        PUT ("PUT"),
        DELETE ("DELETE");

        private String mTypeString;

        MethodType(String typeString) {
            mTypeString = typeString;
        }

        public String getTypeString(){
            return mTypeString;
        }
    }
}
