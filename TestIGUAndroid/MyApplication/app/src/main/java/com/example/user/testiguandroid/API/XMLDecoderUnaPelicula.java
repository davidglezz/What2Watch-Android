package com.example.user.testiguandroid.API;

import android.os.AsyncTask;

import com.example.user.testiguandroid.Activities.MainActivity;
import com.example.user.testiguandroid.Logica.Pelicula;

import java.util.ArrayList;
import java.util.List;

public class XMLDecoderUnaPelicula extends AsyncTask<String, Void, List<Pelicula>> {

    private MainActivity main;
    private String codigo;

    public XMLDecoderUnaPelicula(MainActivity mainActivity) {
        this.main = mainActivity;
    }

    private Pelicula decodearXMLUnaPelicula(String url) {

        //Pelicula pelicula = ApiRequests.getMovie();

        return null;
    }


    @Override
    protected List<Pelicula> doInBackground(String... params) {

        Pelicula p = decodearXMLUnaPelicula(params[0]);
        List<Pelicula> toRet = new ArrayList<Pelicula>();
        //toRet.add(p);
        return toRet;

    }

}

