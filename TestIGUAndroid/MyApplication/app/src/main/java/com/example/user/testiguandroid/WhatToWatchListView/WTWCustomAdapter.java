package com.example.user.testiguandroid.WhatToWatchListView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
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

import java.io.InputStream;
import java.util.List;

/**
 * Created by USER on 02/01/2016.
 */
public class WTWCustomAdapter extends ArrayAdapter<Pelicula> {

    public WTWCustomAdapter(Context context, List<Pelicula> values) {
        super(context, R.layout.movielayout, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movielayout, null);

        Pelicula movie = getItem(position);

        if (movie != null) {

            ImageView imgPoster  = (ImageView) convertView.findViewById(R.id.imgPoster);
            TextView txvTitle    = (TextView) convertView.findViewById(R.id.txvTitle);
            TextView txvYear     = (TextView) convertView.findViewById(R.id.txvYear);
            //TextView txvRating   = (TextView) convertView.findViewById(R.id.txvRating);
            //TextView txvGenre    = (TextView) convertView.findViewById(R.id.txvGenre);

            movie.setPosterToView(imgPoster);
            txvTitle.setText(movie.getTitle());
            txvYear.setText("" + movie.getYear());
            //txvRating.setText("" + movie.getImdbRating());
            //txvGenre.setText("" + movie.getGenre());

        }

        return convertView;
    }




}
