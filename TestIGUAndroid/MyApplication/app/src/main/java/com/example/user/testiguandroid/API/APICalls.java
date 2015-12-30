package com.example.user.testiguandroid.API;

import com.example.user.testiguandroid.Logica.Pelicula;

import java.net.*;
import java.util.List;


public class APICalls {


    private XMLDecoder decoder;
    private GeneradorURLsAPI generator;





    public APICalls() {
        generator=new GeneradorURLsAPI();
        decoder= new XMLDecoder();
    }

    public void buscarTitulos(String titulo){
        generator.nombreDePeliculaABuscar(titulo);
    }
    public void buscarPorAnyo(String anyo){
        generator.anyoPeliculaABuscar(anyo);
    }


    public Pelicula obtenerInfoUnaPelicula(String IMDBID) throws MalformedURLException{
        String urlPreparada=generator.obtenerURLBuscarInfoUnaSolaPelicula(IMDBID);
        //xml=new XMLGetter(urlLista);
        return decoder.decodearXMLUnaPelicula(urlPreparada);

    }

    public List<Pelicula> obtenerListadoPeliculas() throws MalformedURLException{
        String urlPreparada=generator.obtenerURLListaPeliculas();
        //xml=new XMLGetter(urlLista);
        return decoder.decodearXMLVariasPeliculas(urlPreparada);
    }
}