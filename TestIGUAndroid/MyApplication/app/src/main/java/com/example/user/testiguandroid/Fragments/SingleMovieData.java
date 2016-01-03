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
        this.p=p;
        this.caratula=caratula;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.single_movie_data_fragment, container, false);
        ImageView poster=(ImageView)v.findViewById(R.id.caratulaSingleMovieData);
        poster.setImageBitmap(caratula);
        EditText plot=(EditText)v.findViewById(R.id.plotSingleMovieData);
        TextView title=(TextView)v.findViewById(R.id.tituloSingleMovieData);
        TextView year=(TextView)v.findViewById(R.id.anyoSingleMovieData);
        TextView director=(TextView)v.findViewById(R.id.directorSingleMovieData);
        TextView genero=(TextView)v.findViewById(R.id.generoSingleMovieData);
        TextView escritor=(TextView)v.findViewById(R.id.escritorSingleMovieData);
        TextView duracion=(TextView)v.findViewById(R.id.duracionSingleMovieData);

        genero.setText("Genre:"+p.getGenre());
        escritor.setText("Writter:"+p.getWriter());
        duracion.setText(p.getRuntime());
        title.setText(p.getTitle());
        year.setText("Year:"+p.getYear()+"");
        director.setText("Director:"+p.getDirector()+"");
        plot.setKeyListener(null);
        plot.setText(p.getPlot());
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
