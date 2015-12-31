package com.example.user.testiguandroid.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;

import com.example.user.testiguandroid.Fragments.Configuration;
import com.example.user.testiguandroid.Fragments.SearchMovies;
import com.example.user.testiguandroid.R;
import com.example.user.testiguandroid.ThemeChanger;

public class MainActivity extends Activity //AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Configuration.OnFragmentInteractionListener, SearchMovies.OnFragmentInteractionListener {

    SharedPreferences datos;

    public SharedPreferences getDatos(){
        return datos;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChanger.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        datos=getSharedPreferences("What2WatchSecretData", Context.MODE_PRIVATE);
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

    public void actualizarInterfaz(View v){
        Switch interr=(Switch)findViewById(R.id.cinemaModeConfiguration);

        boolean preferencias=datos.getBoolean("CinemaMode",false);
        System.out.println("Las preferencias antes eran eran: " + preferencias);
        if(interr.isChecked()){

            datos.edit().putBoolean("CinemaMode",true).commit();
            ThemeChanger.changeToTheme(this, ThemeChanger.CINEMA);
        }
        else{
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
            FragmentManager fm= getFragmentManager();
            fm.beginTransaction().replace(R.id.fragmentRemplazar,fragment).commit();
        } else if (id == R.id.nav_my_lists) {

        }  else if (id == R.id.nav_conf) {
            fragment = new Configuration();
            FragmentManager fm= getFragmentManager();
            fm.beginTransaction().replace(R.id.fragmentRemplazar,fragment).commit();

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
