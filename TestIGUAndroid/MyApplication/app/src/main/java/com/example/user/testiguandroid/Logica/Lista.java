package com.example.user.testiguandroid.Logica;


import java.util.ArrayList;
import java.util.List;

public class Lista {

    public static List<Lista> listas = new ArrayList<Lista>();

    private int id; //TODO hablar lo del id
    private String nombre;
    private String descripcion;
    private List<Pelicula> peliculas;

    public Lista(String nombre) {
        this.nombre = nombre;
        listas.add(this);
        this.peliculas = new ArrayList<Pelicula>();
    }

    public Lista(String nombre, String descripcion) {
        this(nombre);
        this.descripcion = descripcion;
    }

    public Lista(int id, String nombre, String descripcion) {
        this(nombre, descripcion);
        this.id = id;
    }

    public void addPelicula(Pelicula p) {
        if (peliculas.contains(p)) {
            //TODO lanzar excepcion? no hacer nada?
        } else {
            peliculas.add(p);
        }
    }

    public void removePelicula(Pelicula p) {
        if (peliculas.contains(p)) {
            peliculas.remove(p);
        }
    }

    public static boolean isLista(int id){
        for(Lista lista : listas){
            if(lista.getId() == id)
                return true;
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public List<Pelicula> getPeliculas() {
        return peliculas;
    }
}
