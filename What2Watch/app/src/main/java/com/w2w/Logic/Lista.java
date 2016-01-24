package com.w2w.Logic;


import java.util.ArrayList;
import java.util.List;

public class Lista {

    // lista especial: Populares // No puede ser de tipo lista por que la añade como lista de usuario
    public static List<Pelicula> populares = new ArrayList<Pelicula>();

    // Lista de listas
    public static List<Lista> listas = new ArrayList<Lista>();

    // Lista Seleccionada
    public static Lista current;

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

    public static boolean[] getInList(Pelicula pelicula) {
        boolean[] values = new boolean[listas.size()];
        for (int i = 0; i < values.length; i++)
            values[i] = listas.contains(pelicula);
        return values;
    }

    public boolean addPelicula(Pelicula p) {
        return peliculas.contains(p) ? false : peliculas.add(p);
    }

    public boolean removePelicula(Pelicula p) {
        return peliculas.remove(p);
    }

    public static boolean existLista(int id) {
        for (Lista lista : listas) {
            if (lista.getId() == id)
                return true;
        }
        return false;
    }

    public static Lista getLista(int id_lista) {
        for (Lista lista : listas) {
            if (lista.getId() == id_lista)
                return lista;
        }
        return null;
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

    public Pelicula get(int index) {
        return peliculas.get(index);
    }

    public int numPelis() {
        return peliculas.size();
    }



    public void setCurrent() {
        current = this;
    }
}
