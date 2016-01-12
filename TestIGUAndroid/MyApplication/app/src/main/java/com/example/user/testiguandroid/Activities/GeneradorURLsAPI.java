package com.example.user.testiguandroid.Activities;

import java.net.MalformedURLException;
import java.net.URL;


class GeneradorURLsAPI {

    private String api = "http://www.omdbapi.com/";

    private String s = ""; //S es para Serchear peliculas (por titulo

    private String y = ""; //y es para el Year (AÑO)

    private String r = "&r=xml"; //r es como Retorna los datos. Usaremos XML.

    private String i = ""; //para buscar un ID de IMDB

    private String t = ""; //para obtener informacion sobre UNA pelicula si te sabes el titulo

    private String type = "&type=movie"; //para obtener series, juegos o peliculas. Nosotros tiramos de PELICULAS

    private String plot = "&plot=full"; //full te da la descripcion del plot completa. short te la da resumida.

    private String tomatoes = ""; //nota que le da una pagina web a la pelicula (no lo usaremos anyways)


    public String obtenerURLListaPeliculas() throws MalformedURLException {
        return api + s + y + type + r;
    }

    public String obtenerURLBuscarInfoUnaSolaPelicula(String IMDBID) throws MalformedURLException {
        i = "?i=" + IMDBID;
        String link = "http://www.omdbapi.com/" + i + plot + r;


        return link;
    }

    public void nombreDePeliculaABuscar(String nombre) {
        this.s = "?s=" + normalizarNombreABuscar(nombre);
    }

    public void anyoPeliculaABuscar(String anyo) {
        this.y = "&y=anyo";
    }

    private String normalizarNombreABuscar(String normalizar) {
        String normalizado;
        String paso1;
        paso1 = normalizar.replaceAll("&", " ");
        normalizado = paso1.replaceAll(" ", "+");
        return normalizado;
    }

}
