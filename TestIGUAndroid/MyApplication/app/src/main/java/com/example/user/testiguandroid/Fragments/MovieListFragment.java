package com.example.user.testiguandroid.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.testiguandroid.Adapters.MovieListRecyclerViewAdapter;
import com.example.user.testiguandroid.Logica.Lista;
import com.example.user.testiguandroid.Logica.Pelicula;
import com.example.user.testiguandroid.R;

import java.util.List;

/**
 * A fragment representing a list of Peliculas de una lista.
 *
 * Activities containing this fragment MUST implement the {@link OnMovieListFragmentInteractionListener}
 * interface.
 */
public class MovieListFragment extends Fragment {
    public final static String TAG = MovieListFragment.class.getSimpleName();
    private OnMovieListFragmentInteractionListener mListener;
    private MovieListRecyclerViewAdapter adapter;
    private Lista lista;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieListFragment() {
    }

    @SuppressWarnings("unused")
    public static MovieListFragment newInstance(int id_lista) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putInt("id_lista", id_lista);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() == null) {
            Log.e(TAG, "No se han pasado argumentos!");
            return;
        }

        int id_lista = getArguments().getInt("id_lista");
        lista = Lista.getLista(id_lista);


        if (adapter == null) {
            adapter = new MovieListRecyclerViewAdapter(lista, mListener);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            //registerForContextMenu(recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnMovieListFragmentInteractionListener) {
            mListener = (OnMovieListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnMovieListFragmentInteractionListener) {
            mListener = (OnMovieListFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString() + " must implement OnListFragmentInteractionListener");
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
    public interface OnMovieListFragmentInteractionListener {
        void onMovieListFragmentInteraction(Pelicula p);
    }
}
