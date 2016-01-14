package com.example.user.testiguandroid.Adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.testiguandroid.BaseDatos.MyDataSource;
import com.example.user.testiguandroid.Fragments.MyListsFragment;
import com.example.user.testiguandroid.Logica.Lista;
import com.example.user.testiguandroid.R;

import java.util.List;

public class MyListsRecyclerViewAdapter extends RecyclerView.Adapter<MyListsRecyclerViewAdapter.ViewHolder> {
    public final static String TAG = MyListsRecyclerViewAdapter.class.getSimpleName();
    private final List<Lista> mValues;
    private final MyListsFragment.OnListFragmentInteractionListener mListener;

    public MyListsRecyclerViewAdapter(List<Lista> items, MyListsFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_mylists_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNombre.setText(mValues.get(position).getNombre());
        holder.mDescripcion.setText(mValues.get(position).getDescripcion());
        holder.mContador.setText("" + mValues.get(position).getPeliculas().size());
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public final View mView;
        public final TextView mNombre;
        public final TextView mDescripcion;
        public final TextView mContador;
        public Lista mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            view.setOnLongClickListener(this);
            mNombre = (TextView) view.findViewById(R.id.txvNombre);
            mDescripcion = (TextView) view.findViewById(R.id.txvDescripcion);
            mContador = (TextView) view.findViewById(R.id.txvContador);
        }

        @Override
        public boolean onLongClick(View view) {
            Log.v(TAG, "onLongClick: " + mItem.getNombre());
            final CharSequence[] list = {"Delete"};
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Remove list?");
            builder.setItems(list, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    MyDataSource db = MyDataSource.getInstance();
                    db.removeAllMoviesInList(mItem.getId());
                    db.eliminarLista(mItem);
                    Lista.listas.remove(mItem);
                    Log.v(TAG, "Eliminada lista: " + mItem.getNombre());
                    notifyDataSetChanged();
                    Snackbar.make(mView, "List removed", Snackbar.LENGTH_LONG).show();
                }
            });
            builder.create().show();
            return true;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNombre.getText() + "'";
        }
    }
}
