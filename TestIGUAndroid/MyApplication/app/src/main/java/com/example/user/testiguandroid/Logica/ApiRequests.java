package com.example.user.testiguandroid.Logica;

import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Exception;
import java.lang.String;
import java.lang.StringBuffer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * Esta clase realiza todas las tareas relacionadas con la api, descargar informacion, busqueda, posters ...
 */
public class ApiRequests {

    private static final String BASE_URL = "http://www.omdbapi.com/?";
    private static final String POSTER_FOLDER = "w2w_data";

    // Parámetros API OMDB
    private static class PARAM {
        public static final String id = "i";
        public static final String title = "t";
        public static final String type = "type";  // movie, series, episode
        public static final String plot = "plot";  // short, full
        public static final String tomatoes = "tomatoes"; //true, false
        public static final String search = "s";
        public static final String year = "y";
        public static final String return_format = "r"; // json, xml
        public static final String page = "page";
        public static final String callback = "callback";
        public static final String version = "v";
    }

    // Para el log: es buena practica o costumbre incluir esto en todas las clases
    public final static String TAG = ApiRequests.class.getSimpleName();

    /*
    * Busca por un titulo de pelicula, retorna una lista de películas con informacion reducida
    */
    public List<Pelicula> searchMovies(String title, int page) {

        // Genera la url de la API
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM.search, title)
                .appendQueryParameter(PARAM.type, "movie")
                .appendQueryParameter(PARAM.page, Integer.toString(page))
                .appendQueryParameter(PARAM.return_format, "xml")
                .build();

        Document doc = getXMLDocument(uri.toString());
        if (doc == null) return null;

        List<Pelicula> peliculas = new ArrayList<Pelicula>();

        NodeList nList = doc.getElementsByTagName("result");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                peliculas.add(new Pelicula(
                        eElement.getAttribute("Title"),
                        eElement.getAttribute("Year"),
                        eElement.getAttribute("imdbID"),
                        eElement.getAttribute("Type"),
                        eElement.getAttribute("Poster")));
            }
        }

        return peliculas;
    }

    /*
    * Obtiene todos los datos de una pelicula
    */
    public static Pelicula getMovie(String imdbID) {

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM.id, imdbID)
                .appendQueryParameter(PARAM.type, "movie")
                .appendQueryParameter(PARAM.plot, "full")
                .appendQueryParameter(PARAM.return_format, "xml")
                .build();

        Document doc = getXMLDocument(uri.toString());
        if (doc == null) return null;

        NodeList nList = doc.getElementsByTagName("movie");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                return new Pelicula(
                        eElement.getAttribute("title"),
                        eElement.getAttribute("year"),
                        eElement.getAttribute("rated"),
                        eElement.getAttribute("released"),
                        eElement.getAttribute("runtime"),
                        eElement.getAttribute("genre"),
                        eElement.getAttribute("director"),
                        eElement.getAttribute("writer"),
                        eElement.getAttribute("actors"),
                        eElement.getAttribute("plot"),
                        eElement.getAttribute("language"),
                        eElement.getAttribute("country"),
                        eElement.getAttribute("awards"),
                        eElement.getAttribute("poster"),
                        eElement.getAttribute("metascore"),
                        eElement.getAttribute("imdbRating"),
                        eElement.getAttribute("imdbVotes"),
                        eElement.getAttribute("imdbID"),
                        eElement.getAttribute("type"));
            }
        }
        return null;
    }

    /*
    * Obtiene las 100 peliculas mas populares del momento
    * Retorna una lista de ids de IMDB
    */
    public static List<String> getPopularMovies() {
        final String URL = "http://www.imdb.com/chart/moviemeter";

        String web = http_get(URL);

        List<String> ids = new ArrayList<String>();

        if (web == null || web.isEmpty())
            return ids;

        Pattern p = Pattern.compile("data-tconst=\"([[a-z][0-9]]{9})\"");
        Matcher m = p.matcher(web);
        while (m.find()) { // Find each match in turn; String can't do this.
            ids.add(m.group(1)); // Access a submatch group; String can't do this.
            Log.v(TAG, m.group(1));
        }

        return ids;
    }

    /**
     * Descarga un xml y retorna su Document
    * */
    private static Document getXMLDocument(String url) {
        Document doc = null;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        if (doc != null)
            doc.getDocumentElement().normalize();

        return doc;
    }

    /* Petición HTTP GET a una url, retorna los datos como string */
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
            Log.e(TAG, error.getMessage());
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException error) {
                    Log.e(TAG, error.getMessage());
                }
            }
        }
    }

    /**
     * Descarga la imagen de la película y la guarda en la memoria.
     */
    public static String downloadAndSavePoster(String posterUrl) {

        if (posterUrl == null || posterUrl.isEmpty()) {
            return "";
        }

        String[] urlParts = posterUrl.split("/");
        String fileName = POSTER_FOLDER + "/" + urlParts[urlParts.length - 1];

        FileOutputStream fileOutput = null;
        InputStream inputStream = null;

        try {

            URL url = new URL(posterUrl);
            Log.v(TAG, "Poster URL: " + posterUrl);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            File sdCardFolder = Environment.getExternalStorageDirectory().getAbsoluteFile();

            File posterFolder = new File(sdCardFolder, POSTER_FOLDER);
            if (!posterFolder.isDirectory()) {
                if (!posterFolder.mkdir())
                    Log.e(TAG, "No se pude crear la carpeta " + posterFolder);
            }

            File file = new File(sdCardFolder, fileName);
            if (!file.exists()) {
                if (!file.createNewFile())
                    Log.e(TAG, "No se pude crear el archivo " + file.getPath());
            }

            fileOutput = new FileOutputStream(file);
            inputStream = urlConnection.getInputStream();

            int totalSize = urlConnection.getContentLength();
            int downloadedSize = 0;
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                Log.v(TAG, "Progress - downloaded Size:" + downloadedSize + "totalSize:" + totalSize);
            }

            if (downloadedSize == totalSize)
                return file.getPath();

        } catch (Exception error) {
            Log.e(TAG, error.getMessage());
        } finally {
            if (fileOutput != null) {
                try {
                    fileOutput.close();
                } catch (IOException error) {
                    Log.e(TAG, error.getMessage());
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException error) {
                    Log.e(TAG, error.getMessage());
                }
            }
        }

        return ""; // no se si es mejor retornar null o string vacio ""
    }
}
