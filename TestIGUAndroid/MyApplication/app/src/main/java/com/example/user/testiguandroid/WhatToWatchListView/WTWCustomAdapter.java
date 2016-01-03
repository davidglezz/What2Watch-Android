package com.example.user.testiguandroid.WhatToWatchListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.testiguandroid.Logica.Pelicula;
import com.example.user.testiguandroid.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by USER on 02/01/2016.
 */
public class WTWCustomAdapter extends ArrayAdapter<Pelicula> {
    private List<Pelicula> data;
    private final Context context;


    public WTWCustomAdapter(Context context, List<Pelicula> values) {
        super(context, R.layout.movielayout, values);
        this.context = context;
        this.data = values;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }



    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowMovies;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.movielayout, parent, false);

        TextView titulo = (TextView) rowView.findViewById(R.id.tituloLayout);
        TextView anyo = (TextView) rowView.findViewById(R.id.yearLayout);

        Pelicula p=data.get(position);

        titulo.setText(p.getTitle().toString()+"");
        anyo.setText(p.getYear()+"");

        //TODO poner icono
        return rowView;
    }
}
