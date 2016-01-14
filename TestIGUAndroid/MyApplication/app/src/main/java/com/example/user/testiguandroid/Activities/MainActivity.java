package com.example.user.testiguandroid.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
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

import com.example.user.testiguandroid.BaseDatos.MyDataSource;

import com.example.user.testiguandroid.Fragments.Configuration;
import com.example.user.testiguandroid.Fragments.MovieListResult;
import com.example.user.testiguandroid.Fragments.MyListsFragment;
import com.example.user.testiguandroid.Fragments.PopularsFragment;
import com.example.user.testiguandroid.Fragments.SearchMovies;
import com.example.user.testiguandroid.Logica.ApiRequests;
import com.example.user.testiguandroid.Logica.Lista;
import com.example.user.testiguandroid.Logica.Pelicula;
import com.example.user.testiguandroid.R;
import com.example.user.testiguandroid.ThemeChanger;

import java.net.MalformedURLException;
import java.util.List;

public class MainActivity extends Activity //AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        Configuration.OnFragmentInteractionListener,
        SearchMovies.OnFragmentInteractionListener,
        /*CinemaFinder.OnFragmentInteractionListener,*/
        PopularsFragment.OnPopularsFragmentInteractionListener,
        MyListsFragment.OnListFragmentInteractionListener,
        MovieListResult.OnFragmentInteractionListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private SharedPreferences datos;
    private Fragment currentFragment;

    public SharedPreferences getDatos() {
        return datos;
    }

    //Atributo para la base de datos
    private MyDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ThemeChanger.onActivityCreateSetTheme(this);
        datos = getSharedPreferences("What2WatchSecretData", Context.MODE_PRIVATE);
        boolean preferenciasModoCine = datos.getBoolean("CinemaMode", false);
        if(preferenciasModoCine){
            this.setTheme(R.style.AppThemeCinemaMode);
        }
        //

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //Crear nuevo objeto MyDataSource
        dataSource = MyDataSource.getInstance(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        /* Cargar la base de datos */
        dataSource.loadLists();

        if (currentFragment == null) {
            currentFragment = PopularsFragment.newInstance();
            changeFragment(currentFragment);
        }

        //Activar el sensor de luz
        SensorLuz();

    }

    public boolean hayInternet(){
        ConnectivityManager managerConectividad
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo redDeInternet= managerConectividad.getActiveNetworkInfo();
        if(redDeInternet==null || !redDeInternet.isAvailable()){
            return false;
        }
       if( redDeInternet.getState() == NetworkInfo.State.DISCONNECTED || redDeInternet.getState() == NetworkInfo.State.DISCONNECTED){
          return false;

        }
        if( redDeInternet.getState() == NetworkInfo.State.CONNECTED || redDeInternet.getState() == NetworkInfo.State.CONNECTING ){
            return true;
        }
        return redDeInternet.isConnected();
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

        if(!hayInternet()){
            Snackbar snackbar = Snackbar
                    .make(v, "Internet connection required to search movies", Snackbar.LENGTH_LONG);

            snackbar.show();

        }
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

    public void asyncResult(List<Pelicula> lista) {

        MovieListResult f = new MovieListResult(lista, this);

        changeFragment(f);

    }

    public void actualizarInterfaz(View v) {
        Switch interr = (Switch) findViewById(R.id.cinemaModeConfiguration);

        boolean preferencias = datos.getBoolean("CinemaMode", false);
       // System.out.println("Las preferencias antes eran eran: " + preferencias);
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

        int id = item.getItemId();
        if (id == R.id.nav_search_movie) {
            currentFragment = new SearchMovies();
            changeFragment(currentFragment);
        } else if (id == R.id.nav_popular) {
            currentFragment = PopularsFragment.newInstance();
            changeFragment(currentFragment);
        } else if (id == R.id.nav_my_lists) {
            currentFragment = new MyListsFragment();
            changeFragment(currentFragment);
        } else if (id == R.id.nav_conf) {
            currentFragment = new Configuration();
            changeFragment(currentFragment);

        } else if (id == R.id.CinemaFinder) {
            Intent intent = new Intent(this, CinemaFinderActivity.class);
            startActivity(intent);

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
        //best metodo ever chicos. Pero no lo borreis que sino no compila ;)
    }


    /***************
     * My Lists
     ***************/
    public void fab_new_list_click(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
        Context context = view.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setPadding(16, 16, 16, 16);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText nameBox = new EditText(context);
        nameBox.setHint(R.string.name);
        layout.addView(nameBox);

        final EditText descriptionBox = new EditText(context);
        descriptionBox.setHint(R.string.description);
        layout.addView(descriptionBox);

        dialog.setTitle(R.string.new_list).setView(layout)//.setIcon(R.drawable...)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (!nameBox.getText().toString().isEmpty()) {
                            Lista l = new Lista(nameBox.getText().toString(), descriptionBox.getText().toString());
                            MyDataSource.getInstance().guardarLista(l);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // No hacer nada
                    }
                });

        dialog.show();
    }

    /* Lista de películas Populares */
    @Override
    public void onPopularsFragmentInteraction(Pelicula p) {
        Log.v(TAG, "Click en " + p.toString());
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("imdbID", p.getImdbID());
        startActivity(intent);

    }

    @Override
    public void onListFragmentInteraction(Lista item) {
        Log.v(TAG, "Click en " + item.toString());
    }



    private void SensorLuz() {
        SensorManager mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        Sensor LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(LightSensor != null){
            mySensorManager.registerListener(
                    LightSensorListener,
                    LightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);

        }
    }

    float BackLightValue = 0.5f; //dummy default value

    private final SensorEventListener LightSensorListener = new SensorEventListener(){


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_LIGHT){

                //Formula a cambiar para regular como afecta la luz a la aplicacion
                BackLightValue = (float)1 - event.values[0];

                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.screenBrightness = BackLightValue;
                getWindow().setAttributes(layoutParams);

                int SysBackLightValue = (int)(BackLightValue * 255);

                android.provider.Settings.System.putInt(getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS,
                        SysBackLightValue);
            }
        }

    };
}
