package com.novoda.aqvsira;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import static com.novoda.aqvsira.R.layout.*;

public class SinglePlace extends ListActivity {

    private static String URL =
            "https://api.foursquare.com/v2/venues/%s?oauth_token=%s&v=20111001";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String id = getIntent().getStringExtra("id");
        setContentView(single_place);
        new SinglePlaceTask(this, id).execute();
    }

    public class SinglePlaceTask extends AsyncTask<Void, Void, List<UserHolder>> {

        private HttpGet get;

        public SinglePlaceTask(Context context, String id) {
            String token = PreferenceManager.getDefaultSharedPreferences(context).getString("token", "");
            get = new HttpGet(String.format(URL, id, token));
        }

        @Override
        protected List<UserHolder> doInBackground(Void... voids) {
            AndroidHttpClient client = AndroidHttpClient.newInstance("test");
            List<UserHolder> holders = new ArrayList<UserHolder>();

            try {
                HttpResponse r = client.execute(get);
                JSONObject json = new JSONObject(EntityUtils.toString(r.getEntity()));
                JSONArray groups = json.getJSONObject("response").getJSONObject("venue").getJSONObject("hereNow").getJSONArray("groups");
                for (int i = 0; i < groups.length(); i++) {
                    JSONArray a = groups.getJSONObject(i).getJSONArray("items");
                    for (int j = 0; j < a.length(); j++) {
                        JSONObject o = a.getJSONObject(j).getJSONObject("user");
                        String name = o.getString("firstName") + " " + o.optString("lastName");
                        String photo = o.optString("photo");
                        UserHolder holder = new UserHolder();
                        holder.name = name;
                        try {
                            URI uri = new URI(photo);
                            holder.bit = BitmapFactory.decodeStream(client.execute(new HttpGet(photo)).getEntity().getContent());

                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        holders.add(holder);
                    }
                }

                return holders;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                client.close();
            }
            return holders;
        }

        @Override
        protected void onPostExecute(List<UserHolder> userHolders) {
            setListAdapter(new UserHolderAdapter(SinglePlace.this, userHolders));
            super.onPostExecute(userHolders);
        }
    }

    public void onBomb(View view) {
        startActivity(new Intent(this, Bomb.class));
    }

    private class UserHolder {
        public Bitmap bit;
        public String name;
    }

    public class UserHolderAdapter extends ArrayAdapter<UserHolder> {

        private List<UserHolder> a;

        public UserHolderAdapter(Context context, List<UserHolder> a) {
            super(context, user);
            this.a = a;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(SinglePlace.this).inflate(user, null);
            ((TextView) v.findViewById(R.id.name)).setText(a.get(position).name);
            ((ImageView) v.findViewById(R.id.img)).setImageBitmap(a.get(position).bit);
            return v;
        }

        @Override
        public int getCount() {
            return a.size();
        }
    }
}
