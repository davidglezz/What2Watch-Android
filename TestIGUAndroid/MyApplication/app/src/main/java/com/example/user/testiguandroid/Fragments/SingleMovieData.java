package com.example.user.testiguandroid.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.testiguandroid.Logica.Pelicula;
import com.example.user.testiguandroid.R;


public class SingleMovieData extends Fragment {


    private OnFragmentInteractionListener mListener;

    private Pelicula p;
    private Bitmap caratula;


    @SuppressLint("ValidFragment")
    public SingleMovieData(Pelicula p, Bitmap caratula) {
        this.p = p;
        this.caratula = caratula;
    }


    public SingleMovieData() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.single_movie_data_fragment, container, false);

        ImageView poster = (ImageView) v.findViewById(R.id.imgPoster);
        poster.setImageBitmap(caratula);

        TextView title = (TextView) v.findViewById(R.id.txvTitle);
        TextView genre = (TextView) v.findViewById(R.id.txvGenre);
        TextView plot = (TextView) v.findViewById(R.id.txvPlot);
        TextView year = (TextView) v.findViewById(R.id.txvYear);
        TextView rated = (TextView) v.findViewById(R.id.txvRated);
        TextView relased = (TextView) v.findViewById(R.id.txvReleased);
        TextView duration = (TextView) v.findViewById(R.id.txvRunTime);
        TextView director = (TextView) v.findViewById(R.id.txvDirector);
        TextView writer = (TextView) v.findViewById(R.id.txvWriter);
        TextView actors = (TextView) v.findViewById(R.id.txvActors);
        TextView awards = (TextView) v.findViewById(R.id.txvAwards);
        TextView language = (TextView) v.findViewById(R.id.txvLanguage);
        TextView country = (TextView) v.findViewById(R.id.txvCountry);
        TextView rating = (TextView) v.findViewById(R.id.txvRating);
        TextView metascore = (TextView) v.findViewById(R.id.txvMetascore);
        TextView votes = (TextView) v.findViewById(R.id.txvVotes);

        title.setText(p.getTitle());
        genre.setText(p.getGenre());
        plot.setText(p.getPlot());
        year.setText("" + p.getYear());
        rated.setText(p.getRated());
        relased.setText(p.getReleased());
        duration.setText(p.getRuntime());
        director.setText(p.getDirector());
        writer.setText(p.getWriter());

        actors.setText(p.getActors());
        awards.setText(p.getAwards());
        //language.setText(p.getLanguage());
        country.setText(p.getCountry());
        rating.setText(p.getImdbRating());
        //metascore.setText(p.getMetascore());
        //votes.setText(p.getVotes());

        return v;
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
