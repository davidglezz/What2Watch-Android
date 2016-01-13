package com.example.user.testiguandroid.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.testiguandroid.Fragments.PopularsFragment;
import com.example.user.testiguandroid.Logica.PopularsContent;
import com.example.user.testiguandroid.Logica.PopularsContent.PopularMovie;
import com.example.user.testiguandroid.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PopularMovie} and makes a call to the
 * specified {@link PopularsFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PopularsRecyclerViewAdapter extends RecyclerView.Adapter<PopularsRecyclerViewAdapter.ViewHolder> {

    private final List<PopularMovie> mValues;
    private final PopularsFragment.OnListFragmentInteractionListener mListener;

    public PopularsRecyclerViewAdapter(List<PopularMovie> items, PopularsFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_populars_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
        public final TextView mIdView;
        public final TextView mContentView;
        public PopularsContent.PopularMovie mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.txvNombre);
            mContentView = (TextView) view.findViewById(R.id.txvDescripcion);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
