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
public class WTWCustomAdapter extends BaseAdapter {
    private List<Pelicula> data;
    private static LayoutInflater inf=null;
    private Context context;



    public WTWCustomAdapter(Context context, List<Pelicula> lista){

        this.data=lista;
        this.context=context;
        inf = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
      return data.get(position);
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public static class PeliculaHolder{

        public ImageView caratula;
        public TextView titulo;
        public TextView amyo;




    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowMovies;
        rowMovies=inf.inflate(R.layout.movielayout, null);
        PeliculaHolder ph=new PeliculaHolder();
        ph.caratula=(ImageView)rowMovies.findViewById(R.id.caratulaLayout);
        ph.titulo=(TextView) rowMovies.findViewById(R.id.tituloLayout);
        ph.amyo=(TextView) rowMovies.findViewById(R.id.yearLayout);

        ph.titulo.setText(data.get(position).getTitle());
        ph.amyo.setText(data.get(position).getYear());
        return rowMovies;
    }
}
