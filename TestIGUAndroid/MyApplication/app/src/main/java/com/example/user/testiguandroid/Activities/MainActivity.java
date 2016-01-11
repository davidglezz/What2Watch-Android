package com.example.user.testiguandroid.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;

import com.example.user.testiguandroid.BaseDatos.MyDataSource;
import com.example.user.testiguandroid.Fragments.Configuration;
import com.example.user.testiguandroid.Fragments.MovieListResult;
import com.example.user.testiguandroid.Fragments.MyListsFragment;
import com.example.user.testiguandroid.Fragments.SearchMovies;
import com.example.user.testiguandroid.Fragments.SingleMovieData;
import com.example.user.testiguandroid.Logica.Lista;
import com.example.user.testiguandroid.Logica.Pelicula;
import com.example.user.testiguandroid.R;
import com.example.user.testiguandroid.ThemeChanger;

import java.net.MalformedURLException;
import java.util.List;

public class MainActivity extends Activity //AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Configuration.OnFragmentInteractionListener, SearchMovies.OnFragmentInteractionListener, MovieListResult.OnFragmentInteractionListener,
        SingleMovieData.OnFragmentInteractionListener {

    SharedPreferences datos;

    public SharedPreferences getDatos() {
        return datos;
    }

    //Atributo para la base de datos
    private MyDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChanger.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //Crear nuevo objeto MyDataSource
        dataSource = new MyDataSource(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        datos = getSharedPreferences("What2WatchSecretData", Context.MODE_PRIVATE);


        /* Prueba listas */
        dataSource.loadDB();
        /*
            new Lista("Mi lista", "Prueba");
            new Lista("Mi lista 2", "Prueba");
            new Lista("Mi lista 3", "Prueba");
            */
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    public void searchSingleMovie(Pelicula p) {
        try {
            //String codigo=p.getImdbID();
            //APICalls api= new APICalls();
            //String url=api.obtenerURLSoloUnaPeli(codigo);
            //new XMLDecoderUnaPelicula(this).execute(url);

            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra("imdbID", p.getImdbID());
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void searchMovies(View v) {
        //TODO
        android.support.design.widget.TextInputLayout title =
                (TextInputLayout) findViewById(R.id.movieTitleToSearch);
        android.support.design.widget.TextInputLayout year = (TextInputLayout) findViewById(R.id.movieYearToSearch);
        String titleString = title.getEditText().getText().toString().trim();
        String yearString = year.getEditText().getText().toString().trim();

        if (titleString == "" || titleString.isEmpty() || titleString == null) {
            Snackbar snackbar = Snackbar
                    .make(v, "A Movie Title is required to search any movie", Snackbar.LENGTH_LONG);

            snackbar.show();

        } else if (titleString.length() == 1) {
            Snackbar snackbar = Snackbar
                    .make(v, "Put at least 2 characters to search", Snackbar.LENGTH_LONG);

            snackbar.show();
        } else {
            try {
                APICalls api = new APICalls();
                api.buscarTitulos(titleString);
                api.buscarPorAnyo(yearString);
                String url = api.obtenerURLListaPeliculas();
                new XMLDecoderVariasPeliculas(this).execute(url);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

    }

    public void asyncResult(Pelicula p) {
        new PosterGetter(this, p).execute(p.getPoster());
    }

    public void asyncResult(Pelicula p, Bitmap caratula) {
        SingleMovieData s = new SingleMovieData(p, caratula);
        changeFragment(s);
    }


    public void asyncResult(List<Pelicula> lista) {

        MovieListResult f = new MovieListResult(lista, this);

        changeFragment(f);

    }


    public void actualizarInterfaz(View v) {
        Switch interr = (Switch) findViewById(R.id.cinemaModeConfiguration);

        boolean preferencias = datos.getBoolean("CinemaMode", false);
        System.out.println("Las preferencias antes eran eran: " + preferencias);
        if (interr.isChecked()) {

            datos.edit().putBoolean("CinemaMode", true).commit();
            ThemeChanger.changeToTheme(this, ThemeChanger.CINEMA);
        } else {
            datos.edit().putBoolean("CinemaMode", false).commit();
            ThemeChanger.changeToTheme(this, ThemeChanger.DAY);

        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment;
        if (id == R.id.nav_search_movie) {
            fragment = new SearchMovies();
            changeFragment(fragment);
        } else if (id == R.id.nav_my_lists) {
            fragment = new MyListsFragment();
            changeFragment(fragment);
        } else if (id == R.id.nav_conf) {
            fragment = new Configuration();
            changeFragment(fragment);

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra("imdbID", "tt2488496");
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeFragment(Fragment newFrg) {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentRemplazar, newFrg).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
