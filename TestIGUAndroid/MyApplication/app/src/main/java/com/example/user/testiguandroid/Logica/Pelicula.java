package com.example.user.testiguandroid.Logica;


public class Pelicula {
    private String Title;
    private int Year;
    private String imdbID; //TODO redefinir equals con esto
    private String Type;
    private String poster;

    private String rated;
    private String released;
    private String runtime;
    private String genre;
    private String director;
    private String writer;
    private String[] actors;

    private boolean vista=false;
    private  int nota;
    public void setVista(boolean b){
        this.vista=b;
    }
    private void setNota(int n){
        this.nota=n;
    }

    public String getRated() {
        return rated;
    }

    public String getReleased() {
        return released;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public String getWriter() {
        return writer;
    }

    public String[] getActors() {
        return actors;
    }

    public String getPlot() {
        return plot;
    }

    public String getCountry() {
        return country;
    }

    public String getAwards() {
        return awards;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    private String plot;
    private String country;
    private String awards;
    private String imdbRating;


    public Pelicula(String title, int year, String imdbID, String type,
                    String poster, String rated, String released, String runtime,
                    String genre, String director, String writer, String actors,
                    String plot, String country, String awards, String imdbRating) {
        super();
        Title = title;
        Year = year;
        this.imdbID = imdbID;
        Type = type;
        this.poster = poster;
        this.rated = rated;
        this.released = released;
        this.runtime = runtime;
        this.genre = genre;
        this.director = director;
        this.writer = writer;
        this.actors = actors.split(", ");
        this.plot = plot;
        this.country = country;
        this.awards = awards;
        this.imdbRating = imdbRating;
    }

    public Pelicula(String title, int year, String imdbID, String type,
                    String poster) {
        super();
        Title = title;
        Year = year;
        this.imdbID = imdbID;
        Type = type;
        this.poster = poster;
    }

    public String getTitle() {
        return Title;
    }

    public int getYear() {
        return Year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getType() {
        return Type;
    }

    public String getPoster() {
        return poster;
    }




}
