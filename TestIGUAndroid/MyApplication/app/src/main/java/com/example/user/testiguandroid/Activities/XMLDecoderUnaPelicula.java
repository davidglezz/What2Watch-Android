package com.example.user.testiguandroid.Activities;

import android.os.AsyncTask;

import com.example.user.testiguandroid.Logica.ApiRequests;
import com.example.user.testiguandroid.Logica.Pelicula;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

class XMLDecoderUnaPelicula extends AsyncTask<String, Void, List<Pelicula>> {

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

