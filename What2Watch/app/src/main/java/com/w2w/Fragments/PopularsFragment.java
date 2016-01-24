package com.w2w.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.w2w.Adapters.PopularsRecyclerViewAdapter;
import com.w2w.DataBase.MyDataSource;
import com.w2w.API.ApiRequests;
import com.w2w.Logic.Lista;
import com.w2w.Logic.Pelicula;
import com.w2w.Other.TaskDownloadMovieInfo;
import com.w2w.R;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnPopularsFragmentInteractionListener}
 * interface.
 */
public class PopularsFragment extends Fragment {

    private OnPopularsFragmentInteractionListener mListener;
    private PrepararPopulares tarea;
    private PopularsRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PopularsFragment() {
    }

    @SuppressWarnings("unused")
    public static PopularsFragment newInstance() {
        PopularsFragment fragment = new PopularsFragment();
        //Bundle args = new Bundle();
        //args.putInt(ARG_COLUMN_COUNT, columnCount);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getActivity().setTitle("Populars");

        if (getArguments() != null) {
            //mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        if (adapter == null) {
            adapter = new PopularsRecyclerViewAdapter(Lista.populares, mListener);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_populars_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        }

        if (tarea == null && Lista.populares.size() == 0) {
            tarea = new PrepararPopulares();
            tarea.execute();
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnPopularsFragmentInteractionListener) {
            mListener = (OnPopularsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnPopularsFragmentInteractionListener) {
            mListener = (OnPopularsFragmentInteractionListener) activity;
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
    public interface OnPopularsFragmentInteractionListener {
        void onPopularsFragmentInteraction(Pelicula p);
    }

    /**
     * Preparar lista
     */
    private class PrepararPopulares extends AsyncTask<String, Integer, List<Pelicula>> {
        protected List<Pelicula> doInBackground(String... urls) {

            List<String> ids = ApiRequests.getPopularMovies();

            MyDataSource db = MyDataSource.getInstance();

            for (int i = 0, z = ids.size(); i < z; i++) {

                String id = ids.get(i);
                if (db.existPelicula(id)) {
                    Lista.populares.add(db.getPelicula(id));
                    publishProgress(i);
                } else {
                    Pelicula p = ApiRequests.getMovieByImdbId(id);
                    if (p != null) {
                        Lista.populares.add(p);
                        // TODO: Descargar poster.
                        db.saveMovie(p);
                        publishProgress(i);
                    }
                }

                // Escape early if cancel() is called
                if (isCancelled()) break;
            }

            return Lista.populares;
        }

        protected void onProgressUpdate(Integer... progress) {
            adapter.notifyItemChanged(progress[0]);
        }

        protected void onPostExecute(List<Pelicula> result) {
            adapter.notifyDataSetChanged();
        }
    }
}
