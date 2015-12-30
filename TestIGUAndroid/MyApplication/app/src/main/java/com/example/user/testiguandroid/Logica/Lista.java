package com.example.user.testiguandroid.Logica;


import java.util.ArrayList;
import java.util.List;

public class Lista {

    //private String id; //TODO hablar lo del id
    private String nombre;
    private String descripcion;
    private List<Pelicula> listaPelis;

    public Lista(String nombre){
        this.nombre=nombre;
        this.listaPelis=new ArrayList<Pelicula>();
    }



    public void addPelicula(Pelicula p){
       if(listaPelis.contains(p)){
           //TODO lanzar excepcion? Permitir repetidcas?
       }
        else{
           listaPelis.add(p);
       }
    }

    public void removePelicula(Pelicula p){
    if(listaPelis.contains(p)){
        listaPelis.remove(p);
        }
    }


}
