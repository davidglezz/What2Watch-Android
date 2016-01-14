package com.example.user.testiguandroid.Fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.example.user.testiguandroid.Activities.MainActivity;
import com.example.user.testiguandroid.R;
import com.example.user.testiguandroid.ThemeChanger;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Configuration.OnFragmentInteractionListener} interface
 * to handle interaction events.

 * create an instance of this fragment.
 */
public class Configuration extends Fragment {

    private OnFragmentInteractionListener mListener;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v =inflater.inflate(R.layout.configuration_fragment, container, false);


        Switch interruptor=(Switch)v.findViewById(R.id.cinemaModeConfiguration);

        Switch interruptor2=(Switch)v.findViewById(R.id.lightModeConfiguration);

        SharedPreferences datos=((MainActivity) getActivity()).getDatos();
        boolean preferencias=datos.getBoolean("CinemaMode",false);
        interruptor.setChecked(preferencias);



        boolean preferenciasLuz = datos.getBoolean("LightMode",false);
        interruptor2.setChecked(preferenciasLuz);


        return v;
    }


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
