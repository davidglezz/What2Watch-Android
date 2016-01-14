package com.example.user.testiguandroid.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.testiguandroid.BaseDatos.MyDataSource;
import com.example.user.testiguandroid.Fragments.MovieListFragment;
import com.example.user.testiguandroid.Fragments.PopularsFragment;
import com.example.user.testiguandroid.Logica.Lista;
import com.example.user.testiguandroid.Logica.Pelicula;
import com.example.user.testiguandroid.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Pelicula} and makes a call to the
 * specified {@link PopularsFragment.OnPopularsFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MovieListRecyclerViewAdapter extends RecyclerView.Adapter<MovieListRecyclerViewAdapter.ViewHolder> {
    public final static String TAG = MovieListRecyclerViewAdapter.class.getSimpleName();
    private final Lista mValues;
    private final MovieListFragment.OnMovieListFragmentInteractionListener mListener;
    private Context context;

    public MovieListRecyclerViewAdapter(Lista items, MovieListFragment.OnMovieListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.fragment_populars_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //holder.itemView.setLongClickable(true);
        holder.mItem = mValues.get(position);

        Glide.with(context)
                .load(holder.mItem.getBigPoster())
                .placeholder(R.drawable.ic_insert_photo_white_48dp)
                .error(R.drawable.ic_insert_photo_black_48dp)
                .into(holder.imgPoster);

        holder.mTitle.setText(holder.mItem.getTitle());
        holder.mRate.setText(holder.mItem.getYear());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onMovieListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.numPelis();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public final View mView;
        public final ImageView imgPoster;
        public final TextView mTitle;
        public final TextView mRate;
        public Pelicula mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            view.setOnLongClickListener(this);
            imgPoster = (ImageView) view.findViewById(R.id.imgPoster);
            mTitle = (TextView) view.findViewById(R.id.txvTitle);
            mRate = (TextView) view.findViewById(R.id.txvYear);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitle.getText() + "'";
        }

        @Override
        public void onClick(View view) {
            /*Context context = view.getContext();*/
        }

        @Override
        public boolean onLongClick(View view) {
            // No funciona
            /*if (null != mListener) {
                mListener.onMovieListFragmentInteraction(mItem);
            }*/

            Log.v(TAG, "onLongClick: " + mItem.getTitle());
            final CharSequence[] list = {"Delete"};
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Remove from list?");
            builder.setItems(list, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    Lista lista = Lista.current;
                    lista.removePelicula(mItem);
                    MyDataSource.getInstance().removeMovieInList(mItem.getID(), lista.getId());
                    Log.v(TAG, "Eliminada pelicula \"" + mItem.getTitle() + "\" de la lista " + lista.getNombre());
                    notifyDataSetChanged();
                    Snackbar.make(mView, "Movie removed from list", Snackbar.LENGTH_LONG).show();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }
    }
}
