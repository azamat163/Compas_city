package com.example.azamat.testaltarix;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private String url = "https://ru.wikipedia.org/w/api.php?action=query&list=geosearch&gslimit=10&format=json&prop=coordinates|pageimages|pageterms&piprop=thumbnail&pithumbsize=144";
    private ProgressDialog mProgresDialog;
    private GeoDataModel geo;
    private double latitude;
    private double longitude;
    private String radius = "10000";
    public  ArrayList<GeoDataModel> GeoDataModels;
    private CustomGeoListAdapter rvAdapter;
    private int[] previewsImages =
            {       R.drawable.icon,
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

    private String title;
    private String dist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvv = (RecyclerView) findViewById(R.id.rv);
        rvv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvv.setLayoutManager(llm);
        rvv.setItemAnimator(new DefaultItemAnimator());
        GeoDataModels = new ArrayList<>();
        rvAdapter = new CustomGeoListAdapter(GeoDataModels);
        rvv.setAdapter(rvAdapter);
        LocationManag loc = new LocationManag(MainActivity.this);
        if (loc.canGetLocation()) {
            latitude = loc.latitude;
            longitude = loc.longitude;
            places();
        } else {
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
        mProgresDialog.setMessage("Информацию о вашем местоположении загружаю. Это может занять несколько минут...");
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
        JSONArray itemsorts = sortJsonArray(items);
            geo = new GeoDataModel();
            GeoDataModels.clear();
            for (int i = 0; i < itemsorts.length(); i++) {
                JSONObject c = (JSONObject) itemsorts.get(i);
                if (!c.isNull("title")) {
                    geo.setTitle(c.getString("title"));
                }
                if (!c.isNull("dist")) {
                    geo.setDist(c.getDouble("dist"));
                }
                for (int j = 0; j < previewsImages.length; j++){
                    geo.setImage(previewsImages[j]);
                }

                GeoDataModels.add(i,geo);
                Log.d(TAG, "title :" + geo.getTitle());
                Log.d(TAG, "dist :" + geo.getDist());
                //   Log.d(TAG,"image :" + geo.getImage());


            }
            rvAdapter.notifyDataSetChanged();

    }

    public static JSONArray sortJsonArray(JSONArray array) throws JSONException {
        List<JSONObject> jsons = new ArrayList<JSONObject>();
        for (int i = 0; i < array.length(); i++) {
            jsons.add(array.getJSONObject(i));
        }
        Collections.sort(jsons, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject lhs, JSONObject rhs) {
                String lid = null;
                try {
                    lid = lhs.getString("dist");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String rid = null;
                try {
                    rid = rhs.getString("dist");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return lid.compareTo(rid);
            }
        });
        return new JSONArray(jsons);
    }
}
