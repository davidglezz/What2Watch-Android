package com.example.user.testiguandroid.WhatToWatchListView;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.user.testiguandroid.Logica.Pelicula;
import com.example.user.testiguandroid.R;

import java.util.List;

/**
 * Created by USER on 02/01/2016.
 */
public class WTWListFragment extends ListFragment implements OnItemClickListener {

    WTWCustomAdapter adapter;
    private List<Pelicula> pelis;
    private View v;

    public WTWListFragment(){

    }
    @SuppressLint("ValidFragment")
    public WTWListFragment(List<Pelicula> lista){
        pelis=lista;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.movielayout, null, false);
        return v;

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        adapter = new WTWCustomAdapter(v.getContext(),pelis);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Pelicula p=pelis.get(position);
        Snackbar snackbar = Snackbar
                .make(v, p.getTitle()+" ya vere que hacer", Snackbar.LENGTH_LONG);

        snackbar.show();
    }
}
