package com.example.user.testiguandroid.Logica;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class Pelicula {
    private String title;
    private int year;
    private String imdbID; //TODO redefinir equals con esto
    private String type;
    private String poster;
    private Bitmap posterBMP;
    private String rated;
    private String released;
    private String runtime;
    private String genre;
    private String director;
    private String writer;
    private String actors;
    private String plot;
    private String country;
    private String awards;
    private String imdbRating;
    private boolean vista = false;
    private int nota;


    public Pelicula(String title, int year, String imdbID, String type,
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
        this.actors = actors;//actors.split(", ");
        this.plot = plot;
        this.country = country;
        this.awards = awards;
        this.imdbRating = imdbRating;
    }

    public Pelicula(String title, int year, String imdbID, String type, String poster) {
        super();
        this.title = title;
        this.year = year;
        this.imdbID = imdbID;
        this.type = type;
        this.poster = poster;
    }

    public void setVista(boolean b) {
        this.vista = b;
    }

    private void setNota(int n) {
        this.nota = n;
    }

    public String getRated() { return rated;}

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

    public String getActors() { return actors; }

    public String getPlot() {
        return plot;
    }

    public String getCountry() { return country;  }

    public String getAwards() { return awards; }

    public String getImdbRating() { return imdbRating;  }

    public String getTitle() {
        return title;
    }

    public int getYear() {
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


    /* Deprecated */
    public void setPosterToView(ImageView imgPoster) {
        if (posterBMP == null) {
            new DownloadImageTask(imgPoster).execute(poster);
        } else {
            imgPoster.setImageBitmap(posterBMP);
        }
    }

    /* Deprecated */
    public void setBigPosterToView(ImageView imgPoster) {
        if (posterBMP == null) {
            new DownloadImageTask(imgPoster).execute(getBigPoster());
        } else {
            imgPoster.setImageBitmap(posterBMP);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                Log.v("-------", "Descargando");
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            posterBMP = result;
            bmImage.setImageBitmap(result);
        }
    }
}
