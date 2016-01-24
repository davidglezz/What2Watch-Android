package com.w2w.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.w2w.Logic.Pelicula;
import com.w2w.R;

import java.util.List;

public class SearchResultsAdapter extends ArrayAdapter<Pelicula> {

    public SearchResultsAdapter(Context context, List<Pelicula> values) {
        super(context, R.layout.fragment_search_result_item, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_search_result_item, null);

        Pelicula movie = getItem(position);

        if (movie != null) {

            ImageView imgPoster = (ImageView) convertView.findViewById(R.id.imgPoster);
            TextView txvTitle = (TextView) convertView.findViewById(R.id.txvTitle);
            TextView txvYear = (TextView) convertView.findViewById(R.id.txvYear);
            //TextView txvRating   = (TextView) convertView.findViewById(R.id.txvRating);
            //TextView txvGenre    = (TextView) convertView.findViewById(R.id.txvGenre);

            Glide.with(getContext())
                    .load(movie.getPoster())
                    .placeholder(R.drawable.ic_perm_media_white_48dp)
                    .error(R.drawable.ic_perm_media_white_48dp)
                    .into(imgPoster);

            txvTitle.setText(movie.getTitle());
            txvYear.setText("" + movie.getYear());
            //txvRating.setText("" + movie.getImdbRating());
            //txvGenre.setText("" + movie.getGenre());

        }

        return convertView;
    }


}
