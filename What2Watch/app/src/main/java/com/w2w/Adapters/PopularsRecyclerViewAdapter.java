package com.w2w.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.w2w.Fragments.PopularsFragment;
import com.w2w.Logica.Pelicula;
import com.w2w.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Pelicula} and makes a call to the
 * specified {@link PopularsFragment.OnPopularsFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PopularsRecyclerViewAdapter extends RecyclerView.Adapter<PopularsRecyclerViewAdapter.ViewHolder> {

    private final List<Pelicula> mValues;
    private final PopularsFragment.OnPopularsFragmentInteractionListener mListener;
    private Context context;

    public PopularsRecyclerViewAdapter(List<Pelicula> items, PopularsFragment.OnPopularsFragmentInteractionListener listener) {
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
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onPopularsFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imgPoster;
        public final TextView mTitle;
        public final TextView mRate;
        public Pelicula mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imgPoster = (ImageView) view.findViewById(R.id.imgPoster);
            mTitle = (TextView) view.findViewById(R.id.txvTitle);
            mRate = (TextView) view.findViewById(R.id.txvYear);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitle.getText() + "'";
        }
    }
}
