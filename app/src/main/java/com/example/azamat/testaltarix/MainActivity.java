package com.example.azamat.testaltarix;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private String TAG = MainActivity.class.getSimpleName();
    private String url = "https://ru.wikipedia.org/w/api.php?action=query&generator=geosearch&ggslimit=10&format=json&prop=coordinates|pageimages&colimit=50&pithumbsize=144";
    private ProgressDialog mProgresDialog;
    private GeoDataModel geo;
    private double latitude;
    private double longitude;
    private String radius = "10000";
    public  ArrayList<GeoDataModel> GeoDataModels;
    private CustomGeoListAdapter rvAdapter;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationRequest mLocationRequest;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    Boolean mRequestingLocationUpdates = false;


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

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_ACCESS_COARSE_LOCATION);
            }
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
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


    @Override
    public void onStart(){
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop(){
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected( Bundle bundle) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)) {
                mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLocation != null) {
                    latitude = mLocation.getLatitude();
                    longitude = mLocation.getLongitude();
                  places();
                   Log.d(TAG, "latitude :" + latitude);
                   Log.d(TAG, "longitude :" + longitude);
                }
                if (mRequestingLocationUpdates){
                   mLocationRequest = LocationRequest.create();
                   mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                   mLocationRequest.setInterval(5000);
                    mLocationRequest.setFastestInterval(3000);
                    startLocationUpdates();
               }
            }
    }

    protected void startLocationUpdates() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        places();
        Log.d(TAG, "latitude1 :" + latitude);
        Log.d(TAG, "longitude1 :" + longitude);
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Can't connect to Google Play Services!");
    }

    private void places() {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        mProgresDialog = new ProgressDialog(MainActivity.this);
        mProgresDialog.setMessage("Информацию о вашем местоположении загружаю. Это может занять несколько минут...");
        mProgresDialog.show();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url + "&" + "ggscoord=" + latitude + "|" + longitude + "&" + "ggsradius=" + radius, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    parseJSON(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "POST");
                Log.d(TAG, response.toString());
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
        JSONObject query = json.getJSONObject("query");
        JSONObject pages = query.getJSONObject("pages");
        Iterator<String> iterator = pages.keys();
        GeoDataModels.clear();
        while (iterator.hasNext()){
            geo = new GeoDataModel();
            String currentkey = (String)iterator.next();
            JSONObject currentvalue = pages.getJSONObject(currentkey);
            geo.setTitle(currentvalue.getString("title"));
            JSONObject thumbnail = currentvalue.getJSONObject("thumbnail");
            geo.setImage(thumbnail.getString("source"));
            GeoDataModels.add(geo);
            Log.d(TAG, "title :" + geo.getTitle());
            Log.d(TAG, "ImageUrl :" + geo.getImage());
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
                Double lid = 0.0;
                try {
                    lid = lhs.getDouble("dist");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Double rid = 0.0;
                try {
                    rid = rhs.getDouble("dist");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return lid.compareTo(rid);
            }
        });
        return new JSONArray(jsons);
    }

}
