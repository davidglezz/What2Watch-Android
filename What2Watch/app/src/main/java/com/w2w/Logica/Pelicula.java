package com.w2w.Logica;


public class Pelicula {
    /* imdb api*/
    private String title;
    private String year;
    private String rated;
    private String released;
    private String runtime;
    private String genre;
    private String director;
    private String writer;
    private String actors;
    private String plot;
    private String language;
    private String country;
    private String awards;
    private String poster;
    private String metascore;
    private String imdbRating;
    private String imdbVotes;
    private String imdbID;
    private String type;

    /* nuestros */
    private long ID; // id en la base de datos?
    //private Date timestamp; // Timestamp de la hora de descarga
    private boolean vista = false;
    private int nota;
    private String comment;

    public Pelicula(String title, String year, String rated, String released, String runtime, String genre, String director, String writer, String actors, String plot, String language, String country, String awards, String poster, String metascore, String imdbRating, String imdbVotes, String imdbID, String type) {
        this.title = title;
        this.year = year;
        this.rated = rated;
        this.released = released;
        this.runtime = runtime;
        this.genre = genre;
        this.director = director;
        this.writer = writer;
        this.actors = actors;
        this.plot = plot;
        this.language = language;
        this.country = country;
        this.awards = awards;
        this.poster = poster;
        this.metascore = metascore;
        this.imdbRating = imdbRating;
        this.imdbVotes = imdbVotes;
        this.imdbID = imdbID;
        this.type = type;
    }

    public Pelicula(String title, String year, String rated, String released, String runtime, String genre, String director, String writer, String actors, String plot, String language, String country, String awards, String poster, String metascore, String imdbRating, String imdbVotes, String imdbID, String type, long ID, /*Date timestamp,*/ boolean vista, int nota, String comment) {
        this.title = title;
        this.year = year;
        this.rated = rated;
        this.released = released;
        this.runtime = runtime;
        this.genre = genre;
        this.director = director;
        this.writer = writer;
        this.actors = actors;
        this.plot = plot;
        this.language = language;
        this.country = country;
        this.awards = awards;
        this.poster = poster;
        this.metascore = metascore;
        this.imdbRating = imdbRating;
        this.imdbVotes = imdbVotes;
        this.imdbID = imdbID;
        this.type = type;
        this.ID = ID;
        //this.timestamp = timestamp;
        this.vista = vista;
        this.nota = nota;
        this.comment = comment;
    }

    /*public Pelicula(String title, String year, String imdbID, String type,
                    String poster, String rated, String released, String runtime,
                    String genre, String director, String writer, String actors,
                    String plot, String country, String awards, String imdbRating) {
        super();
        this.title = title;
        this.year = year;
        this.imdbID = imdbID;
        this.type = type;
        this.poster = poster;
        this.rated = rated;
        this.released = released;
        this.runtime = runtime;
        this.genre = genre;
        this.director = director;
        this.writer = writer;
        this.actors = actors;
        this.plot = plot;
        this.country = country;
        this.awards = awards;
        this.imdbRating = imdbRating;
    }*/


    public Pelicula(String title, String year, String imdbID, String type, String poster) {
        super();
        this.title = title;
        this.year = year;
        this.imdbID = imdbID;
        this.type = type;
        this.poster = poster;
    }


    public String getLanguage() {
        return language;
    }

    public String getMetascore() {
        return metascore;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public boolean isVista() {
        return vista;
    }

    public void setVista(boolean b) {
        this.vista = b;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
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

    public String getActors() {
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

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getType() {
        return type;
    }

    public String getPoster() {
        return poster;
    }

    public String getBigPoster() {
        return poster.replace("_SX300.jpg", "_SX1000.jpg");
    }

    public Boolean getVista() {
        return vista;
    }

    public long getID() {
        return ID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Pelicula) {
            Pelicula p = (Pelicula) o;
            return imdbID.equals(p.imdbID);
        }
        return false;
    }

    public void setID(long ID) {
        this.ID = ID;
    }
}
