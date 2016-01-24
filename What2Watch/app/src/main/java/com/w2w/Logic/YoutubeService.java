package com.w2w.Logic;


import android.os.AsyncTask;
import android.util.Log;

import com.w2w.Activities.MovieDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class YoutubeService {
    Pelicula peli;
    String urlBase = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&q=";
    String API_KEY;
    String json;
    String videoUrl;
    MovieDetailActivity mda;

    //ejemplo de URL a utilizar.
    //https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&q=euitio&key=AIzaSyCz7ZPjZzxZX_ZlI2u35euWtOqSOWfoRy0
    //https://www.googleapis.com/youtube/v3/search?part=trailer&key={YOUR_API_KEY}

    public YoutubeService(Pelicula peli, String apikey, MovieDetailActivity mda) {
        this.peli = peli;
        this.API_KEY = apikey;
        this.mda = mda;
    }


    private String makeUrl() {
        String titulo = peli.getTitle().replace(' ', '+');
        return urlBase + "+" + titulo + "+" + peli.getYear() + "&key=" + API_KEY;
    }


    public void findTrailer() {

        String urlString = makeUrl();
        String status;/*
        String result = "http://www.youtube.com/watch?v=";
        String vidCode="Hxy8BZGQ5Jo";*/

        //try {

        Log.v("YOUTUBE URL ", urlString);

        getJSON(urlString);


        //String json = http_get(urlString);
/*
            Log.v("YOUTUBE Json fn ", json);

            JSONObject object = new JSONObject(json);

            status =  object.getString("status");

            if(!status.equals("200")){
                return result+vidCode;
            }

            JSONArray array = object.getJSONArray("items");
            JSONObject item = (JSONObject) array.get(1);
            JSONObject id = (JSONObject) item.get("id");
            vidCode=id.getString("videoId");
            Log.v("YOUTUBE Services ", "codigo: "+vidCode+" URL completa: " + result+vidCode);


        } catch (JSONException ex) {
           Logger.getLogger("YOUTUBE Services ").log(Level.SEVERE, null, ex);

        }*/


        // return videoUrl; //result+vidCode;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    protected void setVideoUrl(String url) {
        this.videoUrl = url;
        this.mda.finhilo(videoUrl);
    }

    protected void getJSON(String url) {
        YoutubeResult youtube = new YoutubeResult(this, url);
        youtube.execute();
        //return getUrlContents(url);
    }

    private String getUrlContents(String theUrl) {
        Log.v("YOUTUBE getUrlcontents ", theUrl);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    /*
    private static String http_get(String strUrl) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Falla si se hace en el hilo principal, el fix son estas 2 lineas
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);

        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            return buffer.toString();

        } catch (IOException error) {
            Log.e("YoutubeService", error.getMessage());
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException error) {
                    Log.e("YoutubeService", error.getMessage());
                }
            }

    }
    */


    private class YoutubeResult extends AsyncTask<Void, Void, String> {
        private String url;
        private String jsonDevuelto;
        YoutubeService youtubeService;

        public YoutubeResult(YoutubeService youtubeService, String url) {
            this.url = url;
            this.youtubeService = youtubeService;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            jsonDevuelto = getUrlContents(url);
            json = jsonDevuelto;
            //Log.v("YOUTUBE jsonDevuelto ", json);
            //json = jsonDevuelto;
            return jsonDevuelto;
        }

        @Override
        protected void onPostExecute(String jsonresult) {
            //super.onPostExecute(jsonresult);

            String status;
            String result = "http://www.youtube.com/watch?v=";
            String vidCode = "Hxy8BZGQ5Jo";

            Log.v("YOUTUBE jsonPostE ", jsonresult);
            try {

                JSONObject object = new JSONObject(json);

                //status =  object.getString("status");

                //if(!status.equals("200")){
                //videoUrl =  result+vidCode;
                //}

                JSONArray array = object.getJSONArray("items");
                JSONObject item = (JSONObject) array.get(0);
                JSONObject id = (JSONObject) item.get("id");
                vidCode = id.getString("videoId");
                Log.v("YOUTUBE Services ", "codigo: " + vidCode + " URL completa: " + result + vidCode);
                //result+vidCode;

                youtubeService.setVideoUrl(result + vidCode);

            } catch (JSONException ex) {
                Logger.getLogger("YOUTUBE Services ").log(Level.SEVERE, null, ex);

            }
        }

        private String getUrlContents(String theUrl) {
            Log.v("YOUTUBE getUrlcontents ", theUrl);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            return content.toString();
        }

    }

}


