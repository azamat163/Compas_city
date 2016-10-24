package com.example.azamat.testaltarix.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.azamat.testaltarix.*;
import com.example.azamat.testaltarix.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_city.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_city#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_city extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener  {
    private static final int AvgRadius = 6371210;
    private static final Double Rad = 0.0174533;
    private String TAG = MainActivity.class.getSimpleName();
    private String url = "https://ru.wikipedia.org/w/api.php?action=query&generator=geosearch&ggslimit=10&format=json&prop=coordinates|pageimages&colimit=50&pithumbsize=144";
    private ProgressDialog mProgresDialog;
    private GeoDataModel geo;
    private double latitude;
    private double longitude;
    private String radius = "10000";
    public ArrayList<GeoDataModel> GeoDataModels;
    private CustomGeoListAdapter rvAdapter;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationSettingsRequest.Builder builder;
    private LocationRequest mLocationRequest;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    Boolean mRequestingLocationUpdates = false;
    private Double lat;
    private Double lon;
    private Double dist;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Button button;
    private OnFragmentInteractionListener mListener;

    public fragment_city() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_favority.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_city newInstance(String param1, String param2) {
        fragment_city fragment = new fragment_city();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.example.azamat.testaltarix.R.layout.fragment_city, container, false);
        RecyclerView rvv = (RecyclerView) rootView.findViewById(R.id.rv);
        rvv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvv.setLayoutManager(llm);
        rvv.setItemAnimator(new DefaultItemAnimator());
        GeoDataModels = new ArrayList<>();
        rvAdapter = new CustomGeoListAdapter(getActivity(),GeoDataModels);
        rvv.setAdapter(rvAdapter);
        if (Build.VERSION.SDK_INT >=23) {
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) &&  ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_ACCESS_COARSE_LOCATION);
                }
            }
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

        }
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)) {
                mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLocation != null) {
                    latitude = mLocation.getLatitude();
                    longitude = mLocation.getLongitude();
                    places();
                    Log.d(TAG, "latitude :" + latitude);
                    Log.d(TAG, "longitude :" + longitude);
                } else {
                    mLocationRequest = LocationRequest.create();
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    mLocationRequest.setInterval(10*1000);
                    mLocationRequest.setFastestInterval(10*1000);
                    startLocationUpdates();
                }
            }
        } else {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLocation != null) {
                latitude = mLocation.getLatitude();
                longitude = mLocation.getLongitude();
                Log.d(TAG, "latitude: " + latitude);
                Log.d(TAG, "latitude: " + longitude);
                places();
            }else {
                mLocationRequest = LocationRequest.create();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationRequest.setInterval(10*1000);
                mLocationRequest.setFastestInterval(10*1000);
                startLocationUpdates();
            }
        }
    }

    protected void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)) {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
            }
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(10*1000);
            mLocationRequest.setFastestInterval(10*1000);
            startLocationUpdates();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        places();
        Log.d(TAG, "latitude :" + latitude);
        Log.d(TAG, "longitude :" + longitude);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Can't connect to Google Play Services!");
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
        super.onStop();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void places() {
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        mProgresDialog = new ProgressDialog(getActivity());
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
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
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
        while (iterator.hasNext()) {
            geo = new GeoDataModel();
            String currentkey = (String) iterator.next();
            //JSONObject currentvalue = pages.getJSONObject(currentkey);
            if (pages.get(currentkey) instanceof JSONObject) {
                JSONObject currentvalue = pages.getJSONObject(currentkey);
                geo.setTitle(currentvalue.getString("title"));
                if (currentvalue.has("thumbnail")) {
                    JSONObject thumbnail = currentvalue.getJSONObject("thumbnail");
                    geo.setImage(thumbnail.getString("source"));
                }
                JSONArray coord = currentvalue.getJSONArray("coordinates");
                for (int i = 0 ; i < coord.length(); i ++){
                    JSONObject tt = coord.getJSONObject(i);
                    lat = tt.getDouble("lat");
                    lon = tt.getDouble("lon");
                    Log.d(TAG, "lat :" + lat);
                    Log.d(TAG, "lon :" + lon);
                    Double r = Math.acos(Math.sin(latitude*Rad)*Math.sin(lat*Rad) + Math.cos(latitude*Rad)*Math.cos(lat*Rad)*Math.cos(longitude*Rad - lon*Rad));
                    dist = AvgRadius*r;
                    int result =  (int) Math.round(dist);
                    geo.setDist(result);
                }
                GeoDataModels.add(geo);
                Collections.sort(GeoDataModels, new Comparator<GeoDataModel>() {
                    @Override
                    public int compare(GeoDataModel o1, GeoDataModel o2) {
                        return o1.getDist().compareTo(o2.getDist());
                    }
                });
                Log.d(TAG, "title :" + geo.getTitle());
                Log.d(TAG, "ImageUrl :" + geo.getImage());
                Log.d(TAG, "dist :" + geo.getDist());
            }
            rvAdapter.notifyDataSetChanged();
        }
    }


}
