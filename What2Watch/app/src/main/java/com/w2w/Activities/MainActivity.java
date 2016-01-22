package com.w2w.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.w2w.BaseDatos.MyDataSource;

import com.w2w.Fragments.AboutFragment;
import com.w2w.Fragments.SettingsFragment;
import com.w2w.Fragments.MovieListFragment;
import com.w2w.Fragments.SearchResultFragment;
import com.w2w.Fragments.MyListsFragment;
import com.w2w.Fragments.PopularsFragment;
import com.w2w.Fragments.SearchFragment;
import com.w2w.API.ApiRequests;
import com.w2w.Logica.Lista;
import com.w2w.Logica.Pelicula;
import com.w2w.Logica.Util;
import com.w2w.Other.NewListDialog;
import com.w2w.R;
import com.w2w.Logica.ThemeChanger;

import java.util.List;

public class MainActivity extends Activity //AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SettingsFragment.OnFragmentInteractionListener,
        SearchFragment.OnFragmentInteractionListener,
        /*CinemaFinder.OnFragmentInteractionListener,*/
        PopularsFragment.OnPopularsFragmentInteractionListener,
        MyListsFragment.OnListFragmentInteractionListener,
        SearchResultFragment.OnFragmentInteractionListener,
        MovieListFragment.OnMovieListFragmentInteractionListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private SharedPreferences datos;
    private Fragment currentFragment;
    private MyDataSource db;

    public SharedPreferences getDatos() {
        return datos;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        datos = getSharedPreferences("What2WatchSecretData", Context.MODE_PRIVATE);

        boolean preferenciasModoCine = datos.getBoolean("CinemaMode", false);

        ThemeChanger.onActivityCreateSetTheme(this, preferenciasModoCine ? 2 : 0);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //Crear nuevo objeto MyDataSource
        db = MyDataSource.getInstance(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer.openDrawer(GravityCompat.START);

        /* Cargar listas de la base de datos */
        db.loadLists();

        // Fragment por defecto
        if (currentFragment == null) {
            changeFragment(PopularsFragment.newInstance());
        }

        //Activar el sensor de luz
        SensorLuz();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (currentFragment instanceof MovieListFragment) {
                changeFragment(new MyListsFragment());
            } else if (currentFragment instanceof SearchResultFragment) {
                changeFragment(new SearchFragment());
            } else {
                // TODO abrir menu, si esta ya abierto cerrar app
                super.onBackPressed();
            }
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
        int id = item.getItemId();

        if (id == android.R.id.home) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                // Notice the Gravity.Right
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void searchSingleMovie(Pelicula p) {
        try {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            // FIX
            //intent.putExtra("imdbID", p.getImdbID());
            intent.putExtra("title", p.getTitle());
            intent.putExtra("year", p.getYear());
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void searchMovies(View v) {
        String title = ((EditText) findViewById(R.id.search_title_editext)).getText().toString().trim();
        String year = ((EditText) findViewById(R.id.search_year_editext)).getText().toString().trim();

        if (!Util.isNetworkAvailable(this)) {
            Snackbar.make(v, "Internet connection required to search movies", Snackbar.LENGTH_LONG).show();
        } else if (title == null || title.isEmpty()) {
            Snackbar.make(v, "A Movie Title is required to search any movie", Snackbar.LENGTH_LONG).show();
        } else if (title.length() == 1) {
            Snackbar.make(v, "Put at least 2 characters to search", Snackbar.LENGTH_LONG).show();
        } else {
            new TaskBusqueda().execute(title, year);
        }
    }

    class TaskBusqueda extends AsyncTask<String, Void, List<Pelicula>> {
        @Override
        protected List<Pelicula> doInBackground(String... params) {
            return ApiRequests.searchMovies(params[0], params[1], 1);
        }

        @Override
        protected void onPostExecute(List<Pelicula> result) {
            asyncResultBusqueda(result);
        }
    }

    public void asyncResultBusqueda(List<Pelicula> lista) {
        if (lista.size() > 0) {
            changeFragment(new SearchResultFragment(lista, this));
        } else {
            Snackbar.make(findViewById(android.R.id.content), "No movies found", Snackbar.LENGTH_LONG).show();
        }
    }

    public void actualizarInterfaz(View v) {
        Switch interr = (Switch) findViewById(R.id.cinemaModeConfiguration);

        //boolean preferencias = datos.getBoolean("CinemaMode", false);
        if (interr.isChecked()) {
            datos.edit().putBoolean("CinemaMode", true).commit();
            //ThemeChanger.changeToTheme(this, ThemeChanger.CINEMA);
        } else {
            datos.edit().putBoolean("CinemaMode", false).commit();
            //ThemeChanger.changeToTheme(this, ThemeChanger.DAY);
        }

        Switch interr2 = (Switch) findViewById(R.id.lightModeConfiguration);
        //preferencias = datos.getBoolean("LightMode", false);
        datos.edit().putBoolean("LightMode", interr2.isChecked()).commit();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_search_movie) {
            changeFragment(new SearchFragment());
        } else if (id == R.id.nav_popular) {
            changeFragment(PopularsFragment.newInstance());
        } else if (id == R.id.nav_my_lists) {
            changeFragment(new MyListsFragment());
        } else if (id == R.id.nav_conf) {
            changeFragment(new SettingsFragment());
        } else if (id == R.id.CinemaFinder) {
            Intent intent = new Intent(this, CinemaFinderActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            changeFragment(new AboutFragment());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeFragment(Fragment newFrg) {
        currentFragment = newFrg;
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentRemplazar, newFrg).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //best metodo ever chicos. Pero no lo borreis que sino no compila ;)
    }


    /***************
     * My Lists
     ***************/
    public void fab_new_list_click(View view) {
        new NewListDialog().showDialog(view);
    }

    /* Lista de películas Populares */
    @Override
    public void onPopularsFragmentInteraction(Pelicula p) {
        Log.v(TAG, "Click en " + p.toString());
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("imdbID", p.getImdbID());
        startActivity(intent);
    }

    /* Lista de películas */
    @Override
    public void onMovieListFragmentInteraction(Pelicula p) {
        Log.v(TAG, "Click en " + p.toString());
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("imdbID", p.getImdbID());
        startActivity(intent);
    }

    // Toque largo, menu eliminar: no funciona asi, funcion no usada de momento
    @Override
    public void onMovieListFragmentLongInteraction(Pelicula p) {
        Log.v(TAG, "onLongClick: " + p.getTitle());

        final Pelicula pelicula = p;
        final CharSequence[] list = {"Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove from list?");
        builder.setItems(list, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Lista lista = Lista.current;
                lista.removePelicula(pelicula);
                db.removeMovieInList(pelicula.getID(), lista.getId());
                Log.v(TAG, "Eliminada pelicula \"" + pelicula.getTitle() + "\" de la lista " + lista.getNombre());
                MovieListFragment f = (MovieListFragment) currentFragment;
                f.notifyDataSetChanged();
                Snackbar.make(findViewById(android.R.id.content), "Movie removed from list", Snackbar.LENGTH_LONG).show();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // Lista de listas click
    @Override
    public void onListFragmentInteraction(Lista lista) {
        Log.v(TAG, "Click en " + lista.toString());
        lista.setCurrent(); // Guardamos la lista que estamos viendo
        changeFragment(MovieListFragment.newInstance(lista.getId()));
    }


    /*
    * Cosas sensor de luz
    * */
    private void SensorLuz() {
        SensorManager mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Sensor LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (LightSensor != null) {
            mySensorManager.registerListener(
                    LightSensorListener,
                    LightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);

        }
    }

    float BackLightValue = 0.5f; //Valor por defecto


    private final SensorEventListener LightSensorListener = new SensorEventListener() {


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSensorChanged(SensorEvent event) {

            boolean preferenciasLuz = datos.getBoolean("LightMode", false);

            if (preferenciasLuz) {
                if (event.sensor.getType() == Sensor.TYPE_LIGHT) {

                    //Formula a cambiar para regular como afecta la luz a la aplicacion
                    BackLightValue = event.values[0];

                    WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                    layoutParams.screenBrightness = BackLightValue;
                    getWindow().setAttributes(layoutParams);

                    int SysBackLightValue = (int) (BackLightValue);

                    android.provider.Settings.System.putInt(getContentResolver(),
                            android.provider.Settings.System.SCREEN_BRIGHTNESS,
                            SysBackLightValue);
                }
            }
        }


    };


}
