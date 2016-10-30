package com.example.azamat.testaltarix.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.azamat.testaltarix.CustomGeoListAdapter;
import com.example.azamat.testaltarix.FavouriteGeoListAdapter;
import com.example.azamat.testaltarix.GeoDataModel;
import com.example.azamat.testaltarix.R;
import com.example.azamat.testaltarix.db.DatabaseHandler;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_favourite.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_favourite#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_favourite extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FavouriteGeoListAdapter rvAdapter;
    private OnFragmentInteractionListener mListener;

    public fragment_favourite() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_favourite.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_favourite newInstance(String param1, String param2) {
        fragment_favourite fragment = new fragment_favourite();
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
        View rootView = inflater.inflate(com.example.azamat.testaltarix.R.layout.fragment_favourite, container, false);
        RecyclerView rv_fav= (RecyclerView) rootView.findViewById(R.id.rv_fav);
        rv_fav.setHasFixedSize(true);
        LinearLayoutManager llm_fav = new LinearLayoutManager(getActivity());
        rv_fav.setLayoutManager(llm_fav);
        rv_fav.setItemAnimator(new DefaultItemAnimator());
        DatabaseHandler db = new DatabaseHandler(getActivity());
        ArrayList<GeoDataModel> geo = db.getAllGeoDataModel();
        rvAdapter = new FavouriteGeoListAdapter(getActivity(),geo);
        rv_fav.setAdapter(rvAdapter);
        rvAdapter.notifyDataSetChanged();
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
}
