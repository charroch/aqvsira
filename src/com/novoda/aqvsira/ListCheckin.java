package com.novoda.aqvsira;

import android.*;
import android.R;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static android.net.http.AndroidHttpClient.newInstance;

public class ListCheckin extends ListActivity {

    private String token;

    private JSONArray json;

    public void onCreate(Bundle bundle) {
        token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", "");
        new AsyncCal().execute();
        super.onCreate(bundle);
    }

    class AsyncCal extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... voids) {
            LocationManager manager = (LocationManager) ListCheckin.this.getSystemService(Context.LOCATION_SERVICE);
            Location loc = manager.getLastKnownLocation("gps");

            String url = "https://api.foursquare.com/v2/venues/search?ll=" + loc.getLatitude() + "," + loc.getLongitude() + "&oauth_token=" + token + "&v=20110930";
            AndroidHttpClient client = newInstance("test");
            try {
                HttpResponse response = client.execute(new HttpGet(url));
                JSONObject ob = new JSONObject(EntityUtils.toString(response.getEntity()));

                json = ob.getJSONObject("response").getJSONArray("venues");
                return json;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                client.close();
            }
            return null;
        }

        protected void onPostExecute(JSONArray result) {

            setListAdapter(new JSon(ListCheckin.this, result));
        }
    }

    public class JSon extends ArrayAdapter<JSONObject> {

        private JSONArray array;

        public JSon(Context context, JSONArray objects) {
            super(context, R.layout.simple_list_item_1);

            this.array = objects;
        }

        @Override
        public int getCount() {
            return array.length();    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public JSONObject getItem(int position) {
            try {
                return array.getJSONObject(position);    //To change body of overridden methods use File | Settings | File Templates.
            } catch (JSONException e) {
                return null;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(ListCheckin.this).inflate(R.layout.simple_list_item_2, null);
            TextView t = (TextView) v.findViewById(R.id.text1);
            TextView t2 = (TextView) v.findViewById(R.id.text2);

            try {
                Log.i("TEST", array.getJSONObject(position).getString("id"));
                t.setText(array.getJSONObject(position).getString("name"));
                t2.setText("Potential kill count of " + array.getJSONObject(position).getJSONObject("hereNow").getInt("count") + " ");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return v;
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, SinglePlace.class);
        String jsonId = null;
        try {
            jsonId = json.getJSONObject(position).getString("id");
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        intent.putExtra("id", jsonId);
        startActivity(intent);


        super.onListItemClick(l, v, position, id);
    }
}
