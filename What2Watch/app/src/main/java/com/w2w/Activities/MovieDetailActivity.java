package com.w2w.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import com.w2w.DataBase.MyDataSource;
import com.w2w.Logic.Lista;
import com.w2w.Logic.Pelicula;
import com.w2w.Logic.ThemeChanger;
import com.w2w.Logic.Util;
import com.w2w.Logic.YoutubeService;
import com.w2w.Other.TaskDownloadMovieInfo;
import com.w2w.Other.NewListDialog;
import com.w2w.R;

public class MovieDetailActivity extends AppCompatActivity {
    public final static String TAG = MovieDetailActivity.class.getSimpleName();
    private Pelicula pelicula;
    private MyDataSource db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Inicializar
        super.onCreate(savedInstanceState);
        //ThemeChanger.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                    setPelicula(db.getPelicula(imdbID), true);
                } else if (Util.isNetworkAvailable(this)) {
                    new TaskDownloadMovieInfo(new DownloadMovieInfoListener()).execute(imdbID);
                } else {
                    alertInternetConnectionRequired();
                }
            } else {
                // Obtener de la base de datos o de de internet...
                String title = intent.getStringExtra("title");
                String year = intent.getStringExtra("year");
                Pelicula movie = db.getPelicula(title, year);
                if (movie != null) {
                    setPelicula(movie, true);
                } else if (Util.isNetworkAvailable(this)) {
                    new TaskDownloadMovieInfo(new DownloadMovieInfoListener()).execute(title, year != null ? year : "");
                } else {
                    alertInternetConnectionRequired();
                }
            }
        }
    }

    // Establecer valores
    private void setPelicula(Pelicula movie, boolean setPoster) {

        this.pelicula = movie;

        // Movie Info
        ((CollapsingToolbarLayout) findViewById(R.id.toolbar_layout)).setTitle(movie.getTitle());
        ((TextView) findViewById(R.id.txvGenre)).setText(movie.getGenre());
        ((TextView) findViewById(R.id.txvPlot)).setText(movie.getPlot());
        ((TextView) findViewById(R.id.txvYear)).setText(movie.getYear());
        ((TextView) findViewById(R.id.txvRated)).setText(movie.getRated());
        ((TextView) findViewById(R.id.txvReleased)).setText(movie.getReleased());
        ((TextView) findViewById(R.id.txvRunTime)).setText(movie.getRuntime());
        ((TextView) findViewById(R.id.txvDirector)).setText(movie.getDirector());
        ((TextView) findViewById(R.id.txvWriter)).setText(movie.getWriter());
        ((TextView) findViewById(R.id.txvActors)).setText(movie.getActors());
        ((TextView) findViewById(R.id.txvAwards)).setText(movie.getAwards());
        ((TextView) findViewById(R.id.txvLanguage)).setText(movie.getLanguage());
        ((TextView) findViewById(R.id.txvCountry)).setText(movie.getCountry());
        ((TextView) findViewById(R.id.txvRating)).setText(movie.getImdbRating());
        ((TextView) findViewById(R.id.txvMetascore)).setText(movie.getMetascore());
        ((TextView) findViewById(R.id.txvVotes)).setText(movie.getImdbVotes());

        if (setPoster) {
            setPoster(movie.getBigPoster());
        }
        //if (movie.getPoster().startsWith("http"));

        // User fields
        RatingBar voto = (RatingBar) findViewById(R.id.ratingBar);
        voto.setRating(movie.getNota() / 2f);
        voto.setOnRatingBarChangeListener(new myOnRatingBarChangeListener(movie));

        final EditText comentario = (EditText) findViewById(R.id.txtComment);
        comentario.setText(movie.getComment());
        comentario.addTextChangedListener(new myTextWatcher(movie));

    }

    private void setPoster(String url) {
        Glide.with(this).load(url).into((ImageView) findViewById(R.id.imgPoster));
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
    class DownloadMovieInfoListener implements TaskDownloadMovieInfo.TaskDownloadMovieInfoListener {
        private ProgressDialog progressDialog;

        @Override
        public void onPreDownload() {
            progressDialog = ProgressDialog.show(MovieDetailActivity.this, "", "Loading movie info...", true);
        }

        @Override
        public void onDownloadedMovieInfo(Pelicula result) {
            progressDialog.dismiss();
            if (result != null)  // TODO && this.pelicula == null
                setPelicula(result, true);
        }

        @Override
        public void onDownloadedMoviePoster(Pelicula result) {
            if (result != null) {
                setPoster(result.getBigPoster());
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
                            new NewListDialog(pelicula).showDialog(v);
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
