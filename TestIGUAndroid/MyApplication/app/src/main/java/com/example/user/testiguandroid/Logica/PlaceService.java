package com.example.user.testiguandroid.Logica;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.Toast;

public class PlaceService {
    private final String TAG = "CINES";
    private String API_KEY;

    public PlaceService(String apikey) {
        this.API_KEY = apikey;
    }

    public void setApiKey(String apikey) {
        this.API_KEY = apikey;
    }

    public ArrayList<Place> findPlaces(double latitude, double longitude,
                                       String typeOfPlace) {

        String urlString = makeUrl(latitude, longitude, typeOfPlace);
        String status;

        try {
            String json = getJSON(urlString);

            System.out.println(json);
            JSONObject object = new JSONObject(json);

            status =  object.getString("status");

            if(!status.equals("OK")){
                return null;
            }

            JSONArray array = object.getJSONArray("results");

            ArrayList<Place> arrayList = new ArrayList<Place>();
            for (int i = 0; i < array.length(); i++) {
                try {
                    Place place = Place.jsonToPlace((JSONObject) array.get(i));
                    Log.v("Places Services ", "" + place);
                    /* Hay algunos sitios mal etiquetados en la API de Google. Por ejemplo, gente
                    etiqueta su videoclub como cine, probablemente con motivos publicitarios.
                    Al convertir Json a Place trataremos de filtrar estos sitios. Si un sitio mal
                    etiquetado es encontrado, el conversor de Json a Place devolverá null
                     */
                    if(place != null) {
                        arrayList.add(place);
                    }
                } catch (Exception e) {
                }
            }
            return arrayList;
        } catch (JSONException ex) {
            Logger.getLogger(PlaceService.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        return null;
    }

    // https://maps.googleapis.com/maps/api/place/search/json?location=28.632808,77.218276&radius=500&types=atm&sensor=false&key=apikey
    private String makeUrl(double latitude, double longitude, String place) {
        StringBuilder urlString = new StringBuilder(
                "https://maps.googleapis.com/maps/api/place/search/json?");

        if (place.equals("")) {
            urlString.append("&location=");
            urlString.append(Double.toString(latitude));
            urlString.append(",");
            urlString.append(Double.toString(longitude));
            urlString.append("&radius=50000"); //El radio esta en metros
            // urlString.append("&types="+place);
            urlString.append("&sensor=false&key=" + API_KEY);
        } else {
            urlString.append("&location=");
            urlString.append(Double.toString(latitude));
            urlString.append(",");
            urlString.append(Double.toString(longitude));
            urlString.append("&radius=50000"); //El radio esta en metros
            urlString.append("&types=" + place);
            urlString.append("&sensor=false&key=" + API_KEY);
        }
        Log.e(TAG, "PlaceService: " + urlString.toString());
        return urlString.toString();
    }

    protected String getJSON(String url) {
        return getUrlContents(url);
    }

    private String getUrlContents(String theUrl) {
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()), 8);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}