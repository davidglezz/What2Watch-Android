package com.example.user.testiguandroid.Activities;

import com.example.user.testiguandroid.Logica.Pelicula;

import java.net.*;
import java.util.List;


public class APICalls {


    //private XMLDecoder decoder;
    private GeneradorURLsAPI generator;





    public APICalls() {
        generator=new GeneradorURLsAPI();
      //  decoder= new XMLDecoder();
    }

    public String obtenerURLListaPeliculas() throws MalformedURLException {
       return generator.obtenerURLListaPeliculas();
    }

    public String obtenerURLSoloUnaPeli(String IMDBID) throws MalformedURLException {
        return generator.obtenerURLBuscarInfoUnaSolaPelicula(IMDBID);
    }
    public void buscarTitulos(String titulo){
        generator.nombreDePeliculaABuscar(titulo);
    }
    public void buscarPorAnyo(String anyo){
        if(anyo!=null && !anyo.isEmpty() && anyo!=null)
            generator.anyoPeliculaABuscar(anyo);
    }

/*
    public Pelicula obtenerInfoUnaPelicula(String IMDBID) throws MalformedURLException{
        String urlPreparada=generator.obtenerURLBuscarInfoUnaSolaPelicula(IMDBID);
        //xml=new XMLGetter(urlLista);
      //  List<Pelicula> resul= (new XMLDecoder().doInBackground("una", urlPreparada));
        return resul.get(0);

    }

    public List<Pelicula> obtenerListadoPeliculas() throws MalformedURLException{
        String urlPreparada=generator.obtenerURLListaPeliculas();
        //xml=new XMLGetter(urlLista);
        return (new XMLDecoder().doInBackground("varias", urlPreparada));
    }
    */
}