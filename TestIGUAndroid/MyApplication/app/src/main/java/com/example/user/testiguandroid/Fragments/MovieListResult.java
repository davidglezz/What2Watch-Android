package com.example.user.testiguandroid.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.user.testiguandroid.Activities.MainActivity;
import com.example.user.testiguandroid.Activities.MovieDetailActivity;
import com.example.user.testiguandroid.Logica.Pelicula;
import com.example.user.testiguandroid.R;
import com.example.user.testiguandroid.WhatToWatchListView.WTWCustomAdapter;


import java.util.List;


public class MovieListResult extends ListFragment {


    private OnFragmentInteractionListener mListener;

    private List<Pelicula> result;
    private MainActivity main;

    //WTWListFragment listviewMovies;
    Context context;
    ListAdapter adapter;


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        main.searchSingleMovie(result.get(position));
    }

    public MovieListResult() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public MovieListResult(List<Pelicula> p, MainActivity main) {
        this.result=p;
        this.main=main;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {


        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista= inflater.inflate(R.layout.movie_list_result_fragment, container, false);

        adapter=new WTWCustomAdapter(vista.getContext(),result);

        setListAdapter(adapter);

        return vista;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
