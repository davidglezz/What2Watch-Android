package com.w2w.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.w2w.BaseDatos.MyDataSource;
import com.w2w.API.ApiRequests;
import com.w2w.Logica.Lista;
import com.w2w.Logica.Pelicula;
import com.w2w.Logica.Util;
import com.w2w.Logica.YoutubeService;
import com.w2w.R;

public class MovieDetailActivity extends AppCompatActivity {
    public final static String TAG = MovieDetailActivity.class.getSimpleName();
    private Pelicula pelicula;
    private MyDataSource db;

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

        if (db == null) {
            db = MyDataSource.getInstance();
        }

        if (pelicula == null) {
            // Obtener ID la película que se quiere mostrar
            Intent intent = getIntent();
            String imdbID = intent.getStringExtra("imdbID");

            if (imdbID != null) {
                // Obtener de la base de datos o de de internet...
                if (db.existPelicula(imdbID)) {
                    setPelicula(db.getPelicula(imdbID));
                } else if (Util.isNetworkAvailable(this)) {
                    new getMovieInfo().execute(imdbID);
                } else {
                    alertInternetConnectionRequired();
                }
            } else {
                // Obtener de la base de datos o de de internet...
                String title = intent.getStringExtra("title");
                String year = intent.getStringExtra("year");
                Pelicula p = db.getPelicula(title, year);
                if (p != null) {
                    setPelicula(p);
                } else if (Util.isNetworkAvailable(this)) {
                    new getMovieInfo().execute(title, year != null ? year : "");
                } else {
                    alertInternetConnectionRequired();
                }
            }
        }
    }

    // Establecer valores
    private void setPelicula(Pelicula pelicula) {

        this.pelicula = pelicula;

        // Movie Info
        Glide.with(this).load(pelicula.getBigPoster()).into((ImageView) findViewById(R.id.imgPoster));
        ((CollapsingToolbarLayout) findViewById(R.id.toolbar_layout)).setTitle(pelicula.getTitle());
        ((TextView) findViewById(R.id.txvGenre)).setText(pelicula.getGenre());
        ((TextView) findViewById(R.id.txvPlot)).setText(pelicula.getPlot());
        ((TextView) findViewById(R.id.txvYear)).setText(pelicula.getYear());
        ((TextView) findViewById(R.id.txvRated)).setText(pelicula.getRated());
        ((TextView) findViewById(R.id.txvReleased)).setText(pelicula.getReleased());
        ((TextView) findViewById(R.id.txvRunTime)).setText(pelicula.getRuntime());
        ((TextView) findViewById(R.id.txvDirector)).setText(pelicula.getDirector());
        ((TextView) findViewById(R.id.txvWriter)).setText(pelicula.getWriter());
        ((TextView) findViewById(R.id.txvActors)).setText(pelicula.getActors());
        ((TextView) findViewById(R.id.txvAwards)).setText(pelicula.getAwards());
        ((TextView) findViewById(R.id.txvLanguage)).setText(pelicula.getLanguage());
        ((TextView) findViewById(R.id.txvCountry)).setText(pelicula.getCountry());
        ((TextView) findViewById(R.id.txvRating)).setText(pelicula.getImdbRating());
        ((TextView) findViewById(R.id.txvMetascore)).setText(pelicula.getMetascore());
        ((TextView) findViewById(R.id.txvVotes)).setText(pelicula.getImdbVotes());

        // User fields
        RatingBar voto = (RatingBar) findViewById(R.id.ratingBar);
        voto.setRating(pelicula.getNota() / 2f);
        voto.setOnRatingBarChangeListener(new myOnRatingBarChangeListener(pelicula));

        final EditText comentario = (EditText) findViewById(R.id.txtComment);
        comentario.setText(pelicula.getComment());
        comentario.addTextChangedListener(new myTextWatcher(pelicula));

    }

    private void alertInternetConnectionRequired() {
        new AlertDialog.Builder(this)
                .setTitle("Oh! No internet")
                .setMessage("Sorry, you need to be connected to internet.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .create()
                .show();
    }

    // Obtener información de la pelicula
    class getMovieInfo extends AsyncTask<String, Void, Pelicula> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MovieDetailActivity.this, "", "Loading movie info...", true);
        }

        @Override
        protected Pelicula doInBackground(String... params) {
            if (params.length == 2)
                return ApiRequests.getMovieByTitle(params[0], params[1]);
            
            return ApiRequests.getMovieByImdbId(params[0]);
        }

        @Override
        protected void onPostExecute(Pelicula result) {
            progressDialog.dismiss();
            if (result != null) {
                db.saveMovie(result);
                setPelicula(result);
            } else {
                alertInternetConnectionRequired();
            }
        }
    }

    // FAB & Add to list btn
    public void add_to_list_click(View view) {

        final CharSequence[] items = Lista.getNames();

        if (items.length == 0) {
            Snackbar.make(view, R.string.no_lists, Snackbar.LENGTH_LONG).setAction(R.string.new_list, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: mostrar dialogo nueva lista
                            Lista lista = new Lista("Que pasa wey", "Lista generada automaticamente");
                            db.guardarLista(lista);
                            lista.addPelicula(pelicula);
                            db.addPeliculaLista(pelicula, lista);
                        }
                    }
            ).show();
            return;
        }

        boolean[] checkedValues = Lista.getInList(pelicula);

        Pelicula p2 = db.getPelicula(pelicula.getImdbID());
        Log.e(TAG, "Comprovación " + p2.equals(pelicula));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_to_list)
                .setMultiChoiceItems(items, checkedValues,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                Lista lista = Lista.listas.get(which);
                                if (isChecked) {
                                    if (lista.addPelicula(pelicula))
                                        db.addPeliculaLista(pelicula, lista);
                                } else {
                                    if (lista.removePelicula(pelicula))
                                        db.removePeliculaLista(pelicula, lista);
                                }
                            }
                        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).create().show();
    }


    /* listener Marcar como visto */
    public void mark_as_seen_click(View v) {
        // TODO Implementar
    }

    /* listener Voto */
    private class myOnRatingBarChangeListener implements RatingBar.OnRatingBarChangeListener {

        private Pelicula pelicula;

        public myOnRatingBarChangeListener(Pelicula pelicula) {
            this.pelicula = pelicula;
        }

        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            pelicula.setNota((int) (rating * 2f));
            db.saveMovie(pelicula);
            Log.v(TAG, "RatingBar " + rating);
        }
    }

    /* listener Comentario */
    private class myTextWatcher implements TextWatcher {

        private Pelicula pelicula;

        public myTextWatcher(Pelicula pelicula) {
            this.pelicula = pelicula;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            pelicula.setComment(s.toString());
            db.saveMovie(pelicula);
        }
    }

    /* Ver trailer en Youtube */
    public void finhilo(String trailer) {
        String api_key = "";
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(),
                    PackageManager.GET_META_DATA);
            api_key = appInfo.metaData.getString("com.google.android.maps.v2.API_KEY");
        } catch (PackageManager.NameNotFoundException e) {
        }

        Log.e(TAG, "MovieDetailActivity : " + trailer);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailer)));
    }

    public void youtube_player(View v) {
        String api_key = "";
        //pelicula.
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(),
                    PackageManager.GET_META_DATA);
            api_key = appInfo.metaData.getString("com.google.android.maps.v2.API_KEY");
        } catch (PackageManager.NameNotFoundException e) {
        }
        YoutubeService youtubeService = new YoutubeService(pelicula, api_key, this);
        youtubeService.findTrailer();


    }
}
