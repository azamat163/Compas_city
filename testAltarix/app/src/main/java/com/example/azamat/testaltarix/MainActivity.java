package com.example.azamat.testaltarix;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.azamat.testaltarix.geosearch;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private String url = "https://ru.wikipedia.org/w/api.php?action=query&list=geosearch&gslimit=10&format=json&prop=coordinates|pageimages|pageterms&piprop=thumbnail&pithumbsize=144";
    private ProgressDialog mProgresDialog;
    private double latitude;
    private double longitude;
    private String radius = "10000";
    private List<geosearch> geosearches;
    private CustomList rvAdapter;
    private int[] previewsImages =
            {R.drawable.icon,
                    R.drawable.icon,
                    R.drawable.icon,
                    R.drawable.icon,
                    R.drawable.icon,
                    R.drawable.icon,
                    R.drawable.icon,
                    R.drawable.icon,
                    R.drawable.icon,
                    R.drawable.icon,
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rvAdapter = new CustomList(geosearches);
        rv.setAdapter(rvAdapter);
        LocationManag loc = new LocationManag(MainActivity.this);
        if (loc.canGetLocation()) {
            latitude = loc.latitude;
            longitude = loc.longitude;
            places();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            loc.showSettingsAlert();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // if (id == R.id.action_settings) {
        //     return true;
        //  }

        return super.onOptionsItemSelected(item);
    }

    private void places() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        mProgresDialog = new ProgressDialog(MainActivity.this);
        mProgresDialog.setMessage("Загружаю.Подождите...");
        mProgresDialog.show();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url + "&" + "gscoord=" + latitude + "|" + longitude + "&" + "gsradius=" + radius, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    parseJSON(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "request :" + response.toString());
                mProgresDialog.hide();
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "error :" + error.getMessage());
                        error.printStackTrace();
                        mProgresDialog.hide();
                    }
                }) {
        };

        requestQueue.add(jsonRequest);

    }


    private void parseJSON(JSONObject json) throws JSONException {
        // JSONObject value = json.getJSONObject("value");
        JSONObject tt = json.getJSONObject("query");
        JSONArray items = tt.getJSONArray("geosearch");
            geosearch geo = new geosearch();
            geosearches.clear();
            for (int i = 0; i < items.length(); i++) {
                JSONObject c = (JSONObject) items.get(i);
                if (!c.isNull("title")) {
                    geo.title = c.getString("title");
                }
                if (!c.isNull("dist")) {
                    geo.dist = c.getDouble("dist");
                }
                for (int j = 0; j < previewsImages.length; j++){
                    geo.image = previewsImages[j];
                }

                geosearches.add(i, geo);
                Log.d(TAG, "title :" + geo.title);
                Log.d(TAG, "dist :" + geo.dist);
                //   Log.d(TAG,"image :" + geo.getImage());


            }
            rvAdapter.notifyDataSetChanged();

    }
}
