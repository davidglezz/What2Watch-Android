package com.w2w.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.w2w.Logica.YoutubeService;
import com.w2w.R;

public class MovieDetailActivity extends AppCompatActivity {
    public final static String TAG = MovieDetailActivity.class.getSimpleName();
    private MovieDetailActivity This;
    private ProgressDialog progressDialog;
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

        This = this;
        db = MyDataSource.getInstance();

        // Obtener ID la película que se quiere mostrar
        // TODO: obtener objeto pelicula
        String imdbID = getIntent().getStringExtra("imdbID");

        if (db.existPelicula(imdbID)) {// Obtener de la base de datos
            setPelicula(db.getPelicula(imdbID));
        } else { // Si no esta en la base de datos: de internet...
            if (!hayInternet())
            {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Sorry");
                alertDialog.setMessage("You need to be connected to internet");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                alertDialog.show();
            } else {
                new getMovieInfo().execute(imdbID);
            }
        }

    }

    // Establecer valores
    private void setPelicula(Pelicula pelicula) {

        this.pelicula = pelicula;

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

        //pelicula.setBigPosterToView(poster);
        Glide.with(this).load(pelicula.getBigPoster()).into(poster);

        collapsing_container.setTitle(pelicula.getTitle());
        genre.setText(pelicula.getGenre());
        plot.setText(pelicula.getPlot());
        year.setText(pelicula.getYear());
        rated.setText(pelicula.getRated());
        relased.setText(pelicula.getReleased());
        duration.setText(pelicula.getRuntime());
        director.setText(pelicula.getDirector());
        writer.setText(pelicula.getWriter());
        actors.setText(pelicula.getActors());
        awards.setText(pelicula.getAwards());
        language.setText(pelicula.getLanguage());
        country.setText(pelicula.getCountry());
        rating.setText(pelicula.getImdbRating());
        metascore.setText(pelicula.getMetascore());
        votes.setText(pelicula.getImdbVotes());

        // Usuario
        RatingBar voto = (RatingBar) findViewById(R.id.ratingBar);
        voto.setRating(pelicula.getNota() / 2f);
        voto.setOnRatingBarChangeListener(new myOnRatingBarChangeListener(pelicula));

        final EditText comentario = (EditText) findViewById(R.id.txtComment);
        comentario.setText(pelicula.getComment());
        comentario.addTextChangedListener(new myTextWatcher(pelicula));

    }

    // Obtener información de la pelicula
    class getMovieInfo extends AsyncTask<String, Void, Pelicula> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MovieDetailActivity.this, "", "Loading movie info...", true);
        }

        @Override
        protected Pelicula doInBackground(String... params) {
            return ApiRequests.getMovie(params[0]);
        }

        @Override
        protected void onPostExecute(Pelicula result) {
            db.saveMovie(result);
            setPelicula(result);
            progressDialog.dismiss();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(This);
        builder.setTitle(R.string.add_to_list);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Lista lista = Lista.listas.get(item);
                Log.v(TAG, "Añadir pelicula(" + pelicula.getID() + ") a lista " + lista.getNombre());
                lista.addPelicula(pelicula);
                db.addPeliculaLista(pelicula, lista);
                // TODO: avisar de que todo correcto
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void finhilo (String trailer)
    {
        String api_key="";
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(),
                    PackageManager.GET_META_DATA);
            api_key = appInfo.metaData.getString("com.google.android.maps.v2.API_KEY");
        }catch(PackageManager.NameNotFoundException e){}

        Log.e(TAG, "MovieDetailActivity : " + trailer);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailer)));
    }

    public void youtube_player(View v) {
        String api_key="";
        //pelicula.
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(),
                    PackageManager.GET_META_DATA);
            api_key = appInfo.metaData.getString("com.google.android.maps.v2.API_KEY");
        }catch(PackageManager.NameNotFoundException e){}
        YoutubeService youtubeService = new YoutubeService(pelicula,api_key, this);
        youtubeService.findTrailer();



    }


    public void mark_as_seen_click(View v) {
        // TODO
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

    public boolean hayInternet() {
        ConnectivityManager managerConectividad
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo redDeInternet = managerConectividad.getActiveNetworkInfo();
        if (redDeInternet == null || !redDeInternet.isAvailable()) {
            return false;
        }
        if (redDeInternet.getState() == NetworkInfo.State.DISCONNECTED || redDeInternet.getState() == NetworkInfo.State.DISCONNECTED) {
            return false;

        }
        if (redDeInternet.getState() == NetworkInfo.State.CONNECTED || redDeInternet.getState() == NetworkInfo.State.CONNECTING) {
            return true;
        }
        return redDeInternet.isConnected();
    }
}
