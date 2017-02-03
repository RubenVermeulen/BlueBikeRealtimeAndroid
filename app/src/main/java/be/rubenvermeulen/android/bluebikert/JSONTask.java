package be.rubenvermeulen.android.bluebikert;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by ruben on 8/10/2016.
 */

public class JSONTask extends AsyncTask<String, Void, JSONObject> {

    private DetailsActivity activity;
    private Exception exception;


    public JSONTask(DetailsActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if ( ! activity.hasInternetConnection()) {
            activity.failedToConnectToTheInternet();
            activity.getSwipeRefreshLayout().setRefreshing(false);

            if (activity.linearLayout.getVisibility() == View.GONE) {
                activity.btnRefresh.setVisibility(View.VISIBLE);
            }

            cancel(true);
        }
        else {
            activity.getSwipeRefreshLayout().setRefreshing(true);
        }
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        HttpsURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);

            connection = (HttpsURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();

            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            String jsonString = buffer.toString();

            JSONObject jsonObject = new JSONObject(jsonString);

            // makes sure the swipe to refresh icon is show (prevents flickering)
            Thread.sleep(1000);

            return jsonObject;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException | JSONException | InterruptedException e) {
            exception = e;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        super.onPostExecute(s);

        try {
            if (exception != null) {
                throw exception;
            }

            JSONArray coordinates = s.getJSONObject("geometry").getJSONArray("coordinates");
            JSONObject properties = s.getJSONObject("properties");
            JSONArray attributes = properties.getJSONArray("attributes");

            // Coordinates
            activity.setGmmIntentUri(Uri.parse(String.format(Locale.getDefault(), "geo:%s,%s?q=%s,%s(Blue+bikes)",
                    coordinates.getString(1), coordinates.getString(0), coordinates.getString(1), coordinates.getString(0)
            )));

            // Details
            activity.tvTitle.setText(activity.getName());
            activity.tvDescription.setText(properties.getString("description"));

            String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());

            activity.tvLastUpdated.setText(activity.getResources().getString(R.string.last_checked) + ": " + currentDateTime);

            // ListView
            ListView listView = (ListView) activity.findViewById(R.id.list);

            List<CustomObject> values = new ArrayList<>();

            int[] priorities = new int[] {4,2,1,3};
            int priority;

            for (int i = 0; i < 4; i++) {
                JSONObject attr = attributes.getJSONObject(i);

                values.add(new CustomObject(
                        attr.getString("attributeName"),
                        attr.getString("value"),
                        priorities[i]
                ));
            }

            Collections.sort(values, new Comparator<CustomObject>() {
                @Override
                public int compare(CustomObject o1, CustomObject o2) {
                    return o1.getPriority() - o2.getPriority();
                }
            });

            // Adapter
            CustomAdapter adapter = new CustomAdapter(activity, values);

            listView.setAdapter(adapter);

            if ( ! activity.linearLayout.isShown()) {
                activity.linearLayout.setVisibility(View.VISIBLE);
            }

            if (activity.btnRefresh.isShown()) {
                activity.btnRefresh.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();

            activity.somethingWentWrong();
        }
        finally {
            activity.getSwipeRefreshLayout().setRefreshing(false);
        }
    }
}
