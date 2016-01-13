package com.example.user.testiguandroid.Logica;


import java.util.ArrayList;
import java.util.List;

public class Lista {

    // lista especial: Populares // No puede ser de tipo lista por que la añade como lista de usuario
    public static List<Pelicula> populares = new ArrayList<Pelicula>();

    // Lista de listas
    public static List<Lista> listas = new ArrayList<Lista>();

    private int id = 0;
    private String nombre;
    private String descripcion;
    private List<Pelicula> peliculas;

    public Lista(String nombre) {
        this.nombre = nombre;
        this.peliculas = new ArrayList<Pelicula>();
        listas.add(this);
    }

    public Lista(String nombre, String descripcion) {
        this(nombre);
        this.descripcion = descripcion;
    }

    public Lista(int id, String nombre, String descripcion) {
        this(nombre, descripcion);
        this.id = id;
    }

    public static CharSequence[] getNames() {
        CharSequence[] names = new CharSequence[listas.size()];
        for (int i = 0; i < names.length; i++)
            names[i] = listas.get(i).getNombre();
        return names;
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
