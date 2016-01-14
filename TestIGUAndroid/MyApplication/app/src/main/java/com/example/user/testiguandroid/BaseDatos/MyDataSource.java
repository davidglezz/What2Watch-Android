package com.example.user.testiguandroid.BaseDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.user.testiguandroid.Logica.Lista;
import com.example.user.testiguandroid.Logica.Pelicula;


public class MyDataSource {

    // Singleton
    private static MyDataSource instance;
    //Variables para manipulación de datos
    private SQLiteDatabase database;
    private MyDBHelper dbHelper;
    public final static String TAG = MyDataSource.class.getSimpleName();

    private MyDataSource(Context context) {
        //Creando una instancia hacia la base de datos
        dbHelper = new MyDBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public static MyDataSource getInstance(Context context) {
        if (instance == null)
            instance = new MyDataSource(context);
        return instance;
    }

    public static MyDataSource getInstance() {
        return instance;
    }


    /**
     * Metodo genérico para filtrar de cualquier tabla
     * Este método te ayuda a añadir todas las partes posibles de las cuales se podría componer una
     * consulta, además que te protege de inyecciones SQL, separando las clausulas de los argumentos.
     *
     * @param table         Nombre de la tabla a consultar
     * @param columns       Lista de nombres de las columnas que se van a consultar. Si deseas
     *                      obtener todas las columnas usas null.
     * @param selection     Es el cuerpo de la sentencia WHERE con las columnas a condicionar. Es
     *                      posible usar el placeholder ‘?’ para generalizar la condición.
     * @param selectionArgs Es una lista de los valores que se usaran para reemplazar las incógnitas
     *                      de selection en el WHERE.
     * @param groupBy       Aquí puedes establecer como se vería la clausula GROUP BY, si es que la
     *                      necesitas.
     * @param having        Establece la sentencia HAVING para condicionar a groupBy.
     * @param orderBy       Reordena las filas de la consulta a través de ORDER BY.
     * @return Devuelve un cursor con la consulta
     */
    public Cursor getAnyRow(String table, String[] columns, String selection,
                            String[] selectionArgs, String groupBy, String having, String orderBy) {
        return database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    /**
     * Metodo para borrar filas de cualquier tabla
     *
     * @param table         Nombre de la tabla a consultar
     * @param selection     Es el cuerpo de la sentencia WHERE con las columnas a condicionar. Es
     *                      posible usar el placeholder ‘?’ para generalizar la condición.
     * @param selectionArgs Es una lista de los valores que se usaran para reemplazar las incógnitas
     *                      de selection en el WHERE.
     */
    public void deleteAnyRow(String table, String selection, String[] selectionArgs) {
        database.delete(table, selection, selectionArgs);
    }


    // Carga las listas de peliculas
    public void loadLists() {
        Cursor cListas = getAllLists(); // Cursor en tabla listas
        Cursor cMovieList; // Cursor en tabla lista-pelicula
        Cursor cPeli; // Cursor en tabla peliculas a una película
        while (cListas.moveToNext()) {
            int id_list = cListas.getInt(cListas.getColumnIndex(ColumnList.ID_LIST));
            if (!Lista.isLista(id_list)) {
                Lista lista = new Lista(id_list,
                        cListas.getString(cListas.getColumnIndex(ColumnList.NAME_LIST)),
                        cListas.getString(cListas.getColumnIndex(ColumnList.DESCRIPTION_LIST)));
                cMovieList = getMoviesInList(cListas.getInt(cListas.getColumnIndex(ColumnList.ID_LIST)));
                while (cMovieList.moveToNext()) {
                    cPeli = getMovie(cMovieList.getInt(cMovieList.getColumnIndex(ColumnMoviesList.ID_MOVIE)));
                    if(cPeli.moveToNext())
                        lista.addPelicula(getPelicula(cPeli.getString(cPeli.getColumnIndex(ColumnMovie.IMDBID))));
                    cPeli.close();
                }
                cMovieList.close();
            }
        }
        cListas.close();
    }

    //******************************* MOVIE ********************************************

    //Metainformación de la base de datos
    public static final String MOVIE_TABLE_NAME = "Movie";
    public static final String STRING_TYPE = "text";
    public static final String INT_TYPE = "integer";
    public static final String BOOL_TYPE = "boolean";

    //Campos de la tabla Movie
    public static class ColumnMovie {
        public static final String ID = BaseColumns._ID;
        public static final String TITLE = "title";
        public static final String YEAR = "year";
        public static final String RATED = "rated";
        public static final String RELEASED = "released";
        public static final String DURATION = "runtime";
        public static final String GENRE = "genre";
        public static final String DIRECTOR = "director";
        public static final String WRITER = "writer";
        public static final String ACTORS = "actors";
        public static final String PLOT = "plot";
        public static final String LANGUAGE = "language";
        public static final String COUNTRY = "country";
        public static final String AWARDS = "awards";
        public static final String POSTER = "poster";
        public static final String METASCORE = "metascore";
        public static final String IMDB_RATING = "imdbRating";
        public static final String IMDB_VOTES = "imdbVotes";
        public static final String IMDBID = "imdbID";
        public static final String TYPE = "type";

        // USER
        // public static final String USER = "user";
        public static final String USER_RATING = "rating";
        public static final String COMMENT = "comment";
        public static final String VIEWED = "viewed";

    }

    //Script de Creación de la tabla Movie
    public static final String CREATE_MOVIE_SCRIPT =
            "create table " + MOVIE_TABLE_NAME + "(" +
                    ColumnMovie.ID + " " + INT_TYPE + " primary key autoincrement not null," +
                    ColumnMovie.TITLE + " " + STRING_TYPE + " not null," +
                    ColumnMovie.YEAR + " " + STRING_TYPE + "," +
                    ColumnMovie.RATED + " " + STRING_TYPE + "," +
                    ColumnMovie.RELEASED + " " + STRING_TYPE + "," +
                    ColumnMovie.DURATION + " " + STRING_TYPE + "," +
                    ColumnMovie.GENRE + " " + STRING_TYPE + "," +
                    ColumnMovie.DIRECTOR + " " + STRING_TYPE + "," +
                    ColumnMovie.WRITER + " " + STRING_TYPE + "," +
                    ColumnMovie.ACTORS + " " + STRING_TYPE + "," +
                    ColumnMovie.PLOT + " " + STRING_TYPE + "," +
                    ColumnMovie.LANGUAGE + " " + STRING_TYPE + "," +
                    ColumnMovie.COUNTRY + " " + STRING_TYPE + "," +
                    ColumnMovie.AWARDS + " " + STRING_TYPE + "," +
                    ColumnMovie.POSTER + " " + STRING_TYPE + "," +
                    ColumnMovie.METASCORE + " " + STRING_TYPE + "," +
                    ColumnMovie.IMDB_RATING + " " + STRING_TYPE + "," +
                    ColumnMovie.IMDB_VOTES + " " + STRING_TYPE + "," +
                    ColumnMovie.IMDBID + " " + STRING_TYPE + "," +
                    ColumnMovie.TYPE + " " + STRING_TYPE + "," +
                    ColumnMovie.VIEWED + " " + BOOL_TYPE + " not null ," +
                    ColumnMovie.USER_RATING + " " + STRING_TYPE + "," +
                    ColumnMovie.COMMENT + " " + STRING_TYPE + ")";

    //Script para borrar la base de datos
    public static final String DROP_MOVIE_SCRIPT = "DROP TABLE IF EXISTS " + MOVIE_TABLE_NAME;

    //Metodo para añadir una pelicula
    public void saveMovie(Pelicula p) {
        Log.e(TAG, "saveMovie: " + p.toString());
        if (p.getID() == 0) {
            //Nuestro contenedor de valores
            ContentValues values = new ContentValues();

            //Seteando columnas
            values.put(ColumnMovie.TITLE, p.getTitle());
            values.put(ColumnMovie.YEAR, p.getYear());
            values.put(ColumnMovie.RATED, p.getRated());
            values.put(ColumnMovie.RELEASED, p.getReleased());
            values.put(ColumnMovie.DURATION, p.getRuntime());
            values.put(ColumnMovie.GENRE, p.getGenre());
            values.put(ColumnMovie.DIRECTOR, p.getDirector());
            values.put(ColumnMovie.WRITER, p.getWriter());
            values.put(ColumnMovie.ACTORS, p.getActors());
            values.put(ColumnMovie.PLOT, p.getPlot());
            values.put(ColumnMovie.LANGUAGE, p.getLanguage());
            values.put(ColumnMovie.COUNTRY, p.getCountry());
            values.put(ColumnMovie.AWARDS, p.getAwards());
            values.put(ColumnMovie.POSTER, p.getPoster());
            values.put(ColumnMovie.METASCORE, p.getMetascore());
            values.put(ColumnMovie.IMDB_RATING, p.getImdbRating());
            values.put(ColumnMovie.IMDB_VOTES, p.getImdbVotes());
            values.put(ColumnMovie.IMDBID, p.getImdbID());
            values.put(ColumnMovie.TYPE, p.getType());
            values.put(ColumnMovie.VIEWED, p.getVista());
            values.put(ColumnMovie.USER_RATING, p.getNota());
            values.put(ColumnMovie.COMMENT, p.getComment());

            //Insertando en la base de datos
            long id = database.insert(MOVIE_TABLE_NAME, null, values);
            if (id != -1) {
                p.setID(id);
                Log.e(TAG, "Pelicula guardada en db: " + p.toString());
            } else {
                // ERROR
                Log.e(TAG, "No se pudo insertar la pelicula: " + p.toString());
            }
        } else {
            actualizarMovie(p.getID(), p.getNota(), p.getComment(), p.getVista());
        }
    }

    public Pelicula getPelicula(String imdbID) {
        String selection = ColumnMovie.IMDBID + " = ?";
        String[] selectionArgs = {imdbID};
        Cursor cursor = getAnyRow(MOVIE_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        cursor.moveToNext();

        return new Pelicula(
                cursor.getString(cursor.getColumnIndex(ColumnMovie.TITLE)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.YEAR)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.RATED)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.RELEASED)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.DURATION)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.GENRE)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.DIRECTOR)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.WRITER)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.ACTORS)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.PLOT)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.LANGUAGE)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.COUNTRY)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.AWARDS)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.POSTER)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.METASCORE)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.IMDB_RATING)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.IMDB_VOTES)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.IMDBID)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.TYPE)),
                cursor.getInt(cursor.getColumnIndex(ColumnMovie.ID)),
                cursor.getInt(cursor.getColumnIndex(ColumnMovie.VIEWED)) > 0,
                cursor.getInt(cursor.getColumnIndex(ColumnMovie.USER_RATING)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.COMMENT)));
    }

    public boolean existPelicula(String imdbID) {
        String selection = ColumnMovie.IMDBID + " = ?";
        String[] selectionArgs = {imdbID};
        Cursor cursor = getAnyRow(MOVIE_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        return cursor.moveToNext();
    }

    //borrar una peli por pelicula
    public void eliminarPelicula(String imdbID) {
        String selection = ColumnMovie.IMDBID + " = ?";
        String[] selectionArgs = {imdbID};

        deleteAnyRow(MOVIE_TABLE_NAME, selection, selectionArgs);
    }


    //Devuelve todas las peliculas
    public Cursor getAllMovies() {
        //Seleccionamos todas las filas de la tabla Movie
        return database.rawQuery(
                "select * from " + MOVIE_TABLE_NAME, null);
    }

    public void actualizarMovie(long id, int rating, String comment, boolean view) {
        //Nuestro contenedor de valores
        ContentValues values = new ContentValues();

        //Seteando body y author
        values.put(ColumnMovie.USER_RATING, rating);
        values.put(ColumnMovie.COMMENT, comment);
        values.put(ColumnMovie.VIEWED, view);

        //Clausula WHERE
        String selection = ColumnMovie.ID + " = ?";
        String[] selectionArgs = {Long.toString(id)};

        //Actualizando
        database.update(MOVIE_TABLE_NAME, values, selection, selectionArgs);
    }

    //obtener una peli por su id
    public Cursor getMovie(int id) {
        String selection = ColumnMovie.ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};
        return getAnyRow(MOVIE_TABLE_NAME, null, selection, selectionArgs, null, null, null);
    }

    //borrar una peli por su id
    public void removeMovie(int id) {
        String selection = ColumnMovie.ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};

        deleteAnyRow(MOVIE_TABLE_NAME, selection, selectionArgs);
    }


    //************************************ LIST *****************************************

    //Metainformación de la base de datos
    public static final String LIST_TABLE_NAME = "List";

    //Campos de la tabla List
    public static class ColumnList {
        public static final String ID_LIST = BaseColumns._ID;
        public static final String NAME_LIST = "name";
        public static final String DESCRIPTION_LIST = "description";


    }

    //Script de Creación de la tabla List
    public static final String CREATE_LIST_SCRIPT =
            "create table " + LIST_TABLE_NAME + "(" +
                    ColumnList.ID_LIST + " " + INT_TYPE + " primary key autoincrement not null," +
                    ColumnList.NAME_LIST + " " + STRING_TYPE + " not null," +
                    ColumnList.DESCRIPTION_LIST + " " + STRING_TYPE + ")";


    //Scripts de inserción por de listas
    public static final String INSERT_LIST_SCRIPT =
            "insert into " + LIST_TABLE_NAME + " values(" +
                    "null," +
                    "\"Favorite\"," +
                    "\"Favorite movies\")," +
                    "(null," +
                    "\"To watch\"," +
                    "\"Movies to see in the future\")," +
                    "(null," +
                    "\"Already views\"," +
                    "\"Movies that have already been seen\")";

    //Script para borrar la base de datos
    public static final String DROP_LIST_SCRIPT = "DROP TABLE IF EXISTS " + LIST_TABLE_NAME;

    //Metodo para añadir una lista
    public void saveListRow(String name, String descripcion) {
        //Nuestro contenedor de valores
        ContentValues values = new ContentValues();

        //Seteando columnas
        values.put(ColumnList.NAME_LIST, name);
        values.put(ColumnList.DESCRIPTION_LIST, descripcion);

        //Insertando en la base de datos
        database.insert(LIST_TABLE_NAME, null, values);
    }


    //Devuelve todas las listas
    public Cursor getAllLists() {
        //Seleccionamos todas las filas de la tabla List
        return database.rawQuery(
                "select * from " + LIST_TABLE_NAME, null);
    }

    public void actualizarList(int id, String name, String descripcion) {
        //Nuestro contenedor de valores
        ContentValues values = new ContentValues();

        //Seteando body y author
        values.put(ColumnList.NAME_LIST, name);
        values.put(ColumnList.DESCRIPTION_LIST, descripcion);

        //Clausula WHERE
        String selection = ColumnList.ID_LIST + " = ?";
        String[] selectionArgs = {Integer.toString(id)};

        //Actualizando
        database.update(LIST_TABLE_NAME, values, selection, selectionArgs);
    }

    //obtener una lista por su id
    public Cursor getList(int id) {
        String selection = ColumnList.ID_LIST + " = ?";
        String[] selectionArgs = {Integer.toString(id)};
        return getAnyRow(LIST_TABLE_NAME, null, selection, selectionArgs, null, null, null);
    }

    //borrar una lista por su id
    public void removeList(int id) {
        String selection = ColumnList.ID_LIST + " = ?";
        String[] selectionArgs = {Integer.toString(id)};

        deleteAnyRow(LIST_TABLE_NAME, selection, selectionArgs);
    }

    //Metodo para añadir una lista
    public void guardarLista(Lista lista) {
        if (lista.getId() == 0) {
            //Nuestro contenedor de valores
            ContentValues values = new ContentValues();

            //Seteando columnas
            values.put(ColumnList.NAME_LIST, lista.getNombre());
            values.put(ColumnList.DESCRIPTION_LIST, lista.getDescripcion());

            //Insertando en la base de datos
            database.insert(LIST_TABLE_NAME, null, values);
        } else {
            actualizarList(lista.getId(), lista.getNombre(), lista.getDescripcion());
        }
    }

    //borrar una lista por su id
    public void eliminarLista(Lista lista) {
        String selection = ColumnList.ID_LIST + " = ?";
        String[] selectionArgs = {Integer.toString(lista.getId())};

        deleteAnyRow(LIST_TABLE_NAME, selection, selectionArgs);
    }


    //************************************** MoviesList ***********************************

    //Metainformación de la base de datos
    public static final String MOVIESLIST_TABLE_NAME = "MoviesList";

    //Campos de la tabla MoviesList
    public static class ColumnMoviesList {
        public static final String ID_MOVIE = "idMovie";
        public static final String ID_LIST = "idList";
    }

    //Script de Creación de la tabla MoviesList
    public static final String CREATE_MOVIESLIST_SCRIPT =
            "create table " + MOVIESLIST_TABLE_NAME + "(" +
                    ColumnMoviesList.ID_MOVIE + " " + INT_TYPE + "," +
                    ColumnMoviesList.ID_LIST + " " + INT_TYPE + "," +
                    "PRIMARY KEY(" + ColumnMoviesList.ID_MOVIE + "," + ColumnMoviesList.ID_LIST + ")," +
                    "FOREIGN KEY(" + ColumnMoviesList.ID_MOVIE + ") REFERENCES " +
                    MOVIE_TABLE_NAME + "(" + ColumnMovie.ID + ") ON DELETE CASCADE," +
                    "FOREIGN KEY(" + ColumnMoviesList.ID_LIST + ") REFERENCES " +
                    LIST_TABLE_NAME + "(" + ColumnList.ID_LIST + ") ON DELETE CASCADE" + ")";


    //Script para borrar la base de datos
    public static final String DROP_MOVIESLIST_SCRIPT = "DROP TABLE IF EXISTS " + MOVIESLIST_TABLE_NAME;

    //Metodo para añadir una pelicula a una lista
    public void saveMoviesListRow(long idMovie, long idList) {
        //Nuestro contenedor de valores
        ContentValues values = new ContentValues();

        //Seteando columnas
        values.put(ColumnMoviesList.ID_MOVIE, idMovie);
        values.put(ColumnMoviesList.ID_LIST, idList);

        //Insertando en la base de datos
        database.insert(MOVIESLIST_TABLE_NAME, null, values);
    }

    public void addPeliculaLista(Pelicula pelicula, Lista lista) {
        saveMoviesListRow(pelicula.getID(), lista.getId());
    }

    public void removePeliculaLista(Pelicula pelicula, Lista lista) {
        removeMovieInList(pelicula.getID(), lista.getId());
    }

    //obtener las peliculas de una lista
    public Cursor getMoviesInList(int id) {
        String selection = ColumnMoviesList.ID_LIST + " = ?";
        String[] selectionArgs = {Integer.toString(id)};
        String columns[] = new String[]{ColumnMoviesList.ID_MOVIE};
        return getAnyRow(MOVIESLIST_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
    }

    //borrar todas las peliculas de una lista
    public void removeAllMoviesInList(int id) {
        String selection = ColumnMoviesList.ID_LIST + " = ?";
        String[] selectionArgs = {Integer.toString(id)};

        deleteAnyRow(MOVIESLIST_TABLE_NAME, selection, selectionArgs);
    }

    //borrar una movie de una lista
    public void removeMovieInList(long idMovie, long idList) {
        String selection = ColumnMoviesList.ID_LIST + " = ?   AND " +
                ColumnMoviesList.ID_MOVIE + " = ?";
        String[] selectionArgs = {Long.toString(idList), Long.toString(idMovie)};

        deleteAnyRow(MOVIESLIST_TABLE_NAME, selection, selectionArgs);
    }

}
