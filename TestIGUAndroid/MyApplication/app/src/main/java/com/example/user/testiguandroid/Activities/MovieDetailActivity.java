package com.example.user.testiguandroid.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.testiguandroid.Logica.Pelicula;
import com.example.user.testiguandroid.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MovieDetailActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Inicializar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        CollapsingToolbarLayout collapsing_container = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsing_container.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsing_container.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        // FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "TODO: Añadir a lista", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Obtener ID la película que se quiere mostrar
        String imdbID = getIntent().getStringExtra("imdbID");

        // Obtener de la base de datos
        // TODO

        // Si no esta en la base de datos: de internet...
        progressDialog = ProgressDialog.show(MovieDetailActivity.this, "", "Loading. Please wait...", true);
        new getMovieInfo().execute(imdbID);

    }

    // Establecer valores
    private void setPelicula(Pelicula pelicula) {

        ImageView poster = (ImageView) findViewById(R.id.imgPoster);
        CollapsingToolbarLayout collapsing_container = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        TextView genre = (TextView) findViewById(R.id.txvGenre);
        TextView plot = (TextView) findViewById(R.id.txvPlot);
        TextView year = (TextView) findViewById(R.id.txvYear);
        TextView rated = (TextView) findViewById(R.id.txvRated);
        TextView relased = (TextView) findViewById(R.id.txvReleased);
        TextView duration = (TextView) findViewById(R.id.txvRunTime);
        TextView director = (TextView) findViewById(R.id.txvDirector);
        TextView writer = (TextView) findViewById(R.id.txvWriter);
        TextView actors = (TextView) findViewById(R.id.txvActors);
        TextView awards = (TextView) findViewById(R.id.txvAwards);
        TextView language = (TextView) findViewById(R.id.txvLanguage);
        TextView country = (TextView) findViewById(R.id.txvCountry);
        TextView rating = (TextView) findViewById(R.id.txvRating);
        TextView metascore = (TextView) findViewById(R.id.txvMetascore);
        TextView votes = (TextView) findViewById(R.id.txvVotes);

        pelicula.setBigPosterToView(poster);

        collapsing_container.setTitle(pelicula.getTitle());
        genre.setText(pelicula.getGenre());
        plot.setText(pelicula.getPlot());
        year.setText("" + pelicula.getYear());
        rated.setText(pelicula.getRated());
        relased.setText(pelicula.getReleased());
        duration.setText(pelicula.getRuntime());
        director.setText(pelicula.getDirector());
        writer.setText(pelicula.getWriter());
        actors.setText(pelicula.getActors());
        awards.setText(pelicula.getAwards());
        //language.setText(p.getLanguage());
        country.setText(pelicula.getCountry());
        rating.setText(pelicula.getImdbRating());
        //metascore.setText(p.getMetascore());
        //votes.setText(p.getVotes());

    }

    // Obtener información de la pelicula
    class getMovieInfo extends AsyncTask<String, Void, Pelicula> {

        @Override
        protected Pelicula doInBackground(String... params) {
            Pelicula pelicula = null;
            String url = "http://www.omdbapi.com/?i="+params[0]+"&plot=full&r=xml";

            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(url);
                doc.getDocumentElement().normalize();

                NodeList nList = doc.getElementsByTagName("movie");
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

                        pelicula = new Pelicula(eElement.getAttribute("title"),
                                Integer.parseInt(eElement.getAttribute("year")),
                                eElement.getAttribute("imdbID"),
                                eElement.getAttribute("type"),
                                eElement.getAttribute("poster"),
                                eElement.getAttribute("rated"),
                                eElement.getAttribute("released"),
                                eElement.getAttribute("runtime"),
                                eElement.getAttribute("genre"),
                                eElement.getAttribute("director"),
                                eElement.getAttribute("writer"),
                                eElement.getAttribute("actors"),
                                eElement.getAttribute("plot"),
                                eElement.getAttribute("country"),
                                eElement.getAttribute("awards"),
                                eElement.getAttribute("imdbRating"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return pelicula;
        }

        @Override
        protected void onPostExecute(Pelicula result) {
            // Update
            // Guarda en base de datos

            setPelicula(result);
            progressDialog.dismiss();

        }
    }

}
