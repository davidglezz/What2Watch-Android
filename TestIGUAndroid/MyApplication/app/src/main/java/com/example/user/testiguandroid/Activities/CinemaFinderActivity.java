package com.example.user.testiguandroid.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.example.user.testiguandroid.R;
import com.example.user.testiguandroid.Logica.Place;
import com.example.user.testiguandroid.Logica.PlaceService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class CinemaFinderActivity extends Activity {
    private final String TAG = "CINES";
    private String API_KEY;
    private GoogleMap mMap;
    private String typeOfPlace;
    private LocationManager locationManager;
    private Location loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ApplicationInfo appInfo;
        super.onCreate(savedInstanceState);
        /*Bundle extras = this.getIntent().getExtras();*/
        setContentView(R.layout.activity_cinema_finder);

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        //Obtenemos la etiqueta con el tipo de sitio cuyos datos queremos obtener. En nuestro caso "movie_theatre"
        typeOfPlace = getResources().getString(R.string.type_of_place).toLowerCase();

        //API_KEY = getIntent().getStringExtra("API_KEY");

        try {
            appInfo = getPackageManager().getApplicationInfo(getPackageName(),
                    PackageManager.GET_META_DATA);
            API_KEY = appInfo.metaData.getString("com.google.android.maps.v2.API_KEY");
        }catch(PackageManager.NameNotFoundException e){}

        getCurrentLocation();

        if (loc != null) {
            mMap.clear();
            new GetPlaces(CinemaFinderActivity.this, typeOfPlace).execute();
        }
    }

    private class GetPlaces extends AsyncTask<Void, Void, ArrayList<Place>> {

        private ProgressDialog dialog;
        private Context context;
        private String typeOfPlace;

        public GetPlaces(Context context, String typeOfPlace) {
            this.context = context;
            this.typeOfPlace = typeOfPlace;
        }

        @Override
        protected void onPostExecute(ArrayList<Place> result) {
            super.onPostExecute(result);

            //Cerrar el mensaje de "Loading"
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if(result == null){
                showToast("No Theatre/Cinema found in your area");
            }else {
                if (result != null) {
                    for (int i = 0; i < result.size(); i++) {
                        mMap.addMarker(new MarkerOptions()
                                .title(result.get(i).getName())
                                .position(
                                        new LatLng(result.get(i).getLatitude(), result
                                                .get(i).getLongitude()))
                                .snippet(result.get(i).getVicinity()));
                    }

                }
            }
            //Haya encontrado sitios o no, centramos el mapa en nuestra posicion
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(loc.getLatitude(),
                            loc.getLongitude())) // Centramos el mapa en la posicion actual
                            // Mountain View
                    .zoom(12) //A mayor valor, mas zoom
                    .tilt(30) // Tilt de la camara a 30 grados
                    .build(); // Crea la posicion de la camara a partir del "Builder"
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }

        @Override
        protected ArrayList<Place> doInBackground(Void... arg0) {
            Log.e(TAG, "Get PlacesdoInBackground");

            PlaceService service = new PlaceService(API_KEY);
                   /* "AIzaSyCz7ZPjZzxZX_ZlI2u35euWtOqSOWfoRy0");*/

            ArrayList<Place> findPlaces = service.findPlaces(loc.getLatitude(),
                    loc.getLongitude(), typeOfPlace);

            if(findPlaces != null){
                for (int i = 0; i < findPlaces.size(); i++) {
                    Place placeDetail = findPlaces.get(i);
                    Log.e(TAG, "Places Found : " + placeDetail.getName());
                }
            }
            return findPlaces;
        }

    }


    private void getCurrentLocation() {
        Location location;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), false);

        try {
            if (provider != null)
                location = locationManager.getLastKnownLocation(provider);
            else {
                location = null;
            }

            if (location == null) {
                locationManager.requestLocationUpdates(provider, 0, 0, listener);
                location = locationManager.getLastKnownLocation(provider);
                Log.e(TAG, "LocationUp : " + location);
            }
            //if don't have a location we try to get it using Network
            if(location == null) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Log.e(TAG, "locationNet : " + location);
            }
            //if don't have a location we try to get it using GPS
            if (location == null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Log.e(TAG, "locationGPS : " + location);
            }

            if (location != null) {
                loc = location;
                new GetPlaces(CinemaFinderActivity.this, typeOfPlace).execute();
            }
            Log.e(TAG, "location : " + location);
        }catch(SecurityException se){
            Log.e(TAG, "Exception: currentLocation");
        }
    }

    protected void showToast(String text) {
        Toast.makeText(this.getBaseContext(), text, Toast.LENGTH_SHORT).show();
    }

    private LocationListener listener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }


        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "Location update : " + location);
            loc = location;
            try {
                locationManager.removeUpdates(listener);
            }catch(SecurityException se){}
        }
    };
}