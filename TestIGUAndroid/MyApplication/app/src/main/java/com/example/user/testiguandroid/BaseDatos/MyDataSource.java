package com.example.user.testiguandroid.BaseDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.user.testiguandroid.Logica.Lista;
import com.example.user.testiguandroid.Logica.Pelicula;


public class MyDataSource {

    // Singleton
    private static MyDataSource instance;
    //Variables para manipulación de datos
    private SQLiteDatabase database;
    private MyDBHelper dbHelper;

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
        Cursor cursor = getAllLists();
        Cursor cursor2;
        Cursor cursor3;
        Lista lista;
        while (cursor.moveToNext()) {
            if (!Lista.isLista(cursor.getInt(cursor.getColumnIndex(ColumnList.ID_LIST)))) {
                lista = new Lista(cursor.getInt(cursor.getColumnIndex(ColumnList.ID_LIST)),
                        cursor.getString(cursor.getColumnIndex(ColumnList.NAME_LIST)),
                        cursor.getString(cursor.getColumnIndex(ColumnList.DESCRIPTION_LIST)));
                cursor2 = getMoviesInList(cursor.getInt(cursor.getColumnIndex(ColumnList.ID_LIST)));
                while (cursor2.moveToNext()) {
                    cursor3 = getMovie(cursor2.getInt(cursor.getColumnIndex(ColumnMoviesList.ID_MOVIE_MOVIESLIST)));
                    cursor3.moveToNext();
                    lista.addPelicula(getPelicula(cursor3.getString(cursor.getColumnIndex(ColumnMovie.IMDBID_MOVIE))));
                    cursor3.close();
                }
                cursor2.close();

            }

        }
        cursor.close();
    }

    //******************************* MOVIE ********************************************

    //Metainformación de la base de datos
    public static final String MOVIE_TABLE_NAME = "Movie";
    public static final String STRING_TYPE = "text";
    public static final String INT_TYPE = "integer";
    public static final String BOOL_TYPE = "boolean";

    //Campos de la tabla Movie
    public static class ColumnMovie {
        public static final String ID_MOVIE = BaseColumns._ID;
        public static final String USER_MOVIE = "user";
        public static final String RATING_MOVIE = "rating";
        public static final String TYPE_MOVIE = "type";
        public static final String COMMENT_MOVIE = "comment";
        public static final String TITLE_MOVIE = "title";
        public static final String GENRE_MOVIE = "genre";
        public static final String PLOT_MOVIE = "plot";
        public static final String YEAR_MOVIE = "year";
        public static final String RATED_MOVIE = "rated";
        public static final String RELEASED_MOVIE = "released";
        public static final String DURATION_MOVIE = "duration";
        public static final String DIRECTOR_MOVIE = "director";
        public static final String WRITER_MOVIE = "writer";
        public static final String ACTORS_MOVIE = "actor";
        public static final String AWARDS_MOVIE = "awards";
        public static final String COUNTRY_MOVIE = "country";
        public static final String IMDBID_MOVIE = "imdbID";
        public static final String IMDB_RATING_MOVIE = "imdbRating";
        public static final String POSTER_MOVIE = "poster";
        public static final String VIEW_MOVIE = "view";
    }

    //Script de Creación de la tabla Movie
    public static final String CREATE_MOVIE_SCRIPT =
            "create table " + MOVIE_TABLE_NAME + "(" +
                    ColumnMovie.ID_MOVIE + " " + INT_TYPE + " primary key autoincrement not null," +
                    ColumnMovie.USER_MOVIE + " " + STRING_TYPE + " not null," +
                    ColumnMovie.RATING_MOVIE + " " + STRING_TYPE + "," +
                    ColumnMovie.COMMENT_MOVIE + " " + STRING_TYPE + "," +
                    ColumnMovie.TITLE_MOVIE + " " + STRING_TYPE + " not null," +
                    ColumnMovie.GENRE_MOVIE + " " + STRING_TYPE + "," +
                    ColumnMovie.TYPE_MOVIE + " " + STRING_TYPE + "," +
                    ColumnMovie.PLOT_MOVIE + " " + STRING_TYPE + "," +
                    ColumnMovie.YEAR_MOVIE + " " + INT_TYPE + "," +
                    ColumnMovie.RATED_MOVIE + " " + STRING_TYPE + "," +
                    ColumnMovie.RELEASED_MOVIE + " " + STRING_TYPE + "," +
                    ColumnMovie.DURATION_MOVIE + " " + STRING_TYPE + "," +
                    ColumnMovie.DIRECTOR_MOVIE + " " + STRING_TYPE + "," +
                    ColumnMovie.WRITER_MOVIE + " " + STRING_TYPE + "," +
                    ColumnMovie.ACTORS_MOVIE + " " + STRING_TYPE + "," +
                    ColumnMovie.AWARDS_MOVIE + " " + STRING_TYPE + "," +
                    ColumnMovie.COUNTRY_MOVIE + " " + STRING_TYPE + "," +
                    ColumnMovie.IMDBID_MOVIE + " " + STRING_TYPE + "," +
                    ColumnMovie.IMDB_RATING_MOVIE + " " + STRING_TYPE + "," +
                    ColumnMovie.POSTER_MOVIE + " " + STRING_TYPE + "," +
                    ColumnMovie.VIEW_MOVIE + " " + BOOL_TYPE + " not null )";


    //Script para borrar la base de datos
    public static final String DROP_MOVIE_SCRIPT = "DROP TABLE IF EXISTS " + MOVIE_TABLE_NAME;

    //Metodo para añadir una pelicula
    public void saveMovie(Pelicula p) {

        if(p.getID() == 0) {
            //Nuestro contenedor de valores
            ContentValues values = new ContentValues();

            //Seteando columnas
            //values.put(ColumnMovie.USER_MOVIE, user);
            values.put(ColumnMovie.RATING_MOVIE, p.getNota());
            //values.put(ColumnMovie.COMMENT_MOVIE, comment); // No implementado
            values.put(ColumnMovie.TITLE_MOVIE, p.getTitle());
            values.put(ColumnMovie.GENRE_MOVIE, p.getGenre());
            values.put(ColumnMovie.PLOT_MOVIE, p.getPlot());
            values.put(ColumnMovie.YEAR_MOVIE, p.getYear());
            values.put(ColumnMovie.RATED_MOVIE, p.getRated());
            values.put(ColumnMovie.RELEASED_MOVIE, p.getReleased());
            values.put(ColumnMovie.DURATION_MOVIE, p.getRuntime());
            values.put(ColumnMovie.DIRECTOR_MOVIE, p.getDirector());
            values.put(ColumnMovie.WRITER_MOVIE, p.getWriter());
            values.put(ColumnMovie.ACTORS_MOVIE, p.getActors());
            values.put(ColumnMovie.AWARDS_MOVIE, p.getAwards());
            values.put(ColumnMovie.COUNTRY_MOVIE, p.getCountry());
            values.put(ColumnMovie.IMDBID_MOVIE, p.getImdbID());
            values.put(ColumnMovie.IMDB_RATING_MOVIE, p.getImdbRating());
            values.put(ColumnMovie.POSTER_MOVIE, p.getPoster());
            values.put(ColumnMovie.VIEW_MOVIE, p.getVista());
            values.put(ColumnMovie.TYPE_MOVIE, p.getType());

            //Insertando en la base de datos
            database.insert(MOVIE_TABLE_NAME, null, values);
        }
        else{
            actualizarMovie(p.getID(),p.getNota(),p.getComment(),p.getVista());
        }
    }

    public Pelicula getPelicula(String imdbID){
        String selection = ColumnMovie.IMDBID_MOVIE + " = ?";
        String[] selectionArgs = {imdbID};
        Cursor cursor = getAnyRow(MOVIE_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        cursor.moveToNext();
        return new Pelicula(cursor.getString(cursor.getColumnIndex(ColumnMovie.TITLE_MOVIE)),
                cursor.getInt(cursor.getColumnIndex(ColumnMovie.YEAR_MOVIE)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.IMDBID_MOVIE)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.TYPE_MOVIE)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.POSTER_MOVIE)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.RATED_MOVIE)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.RELEASED_MOVIE)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.DURATION_MOVIE)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.GENRE_MOVIE)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.DIRECTOR_MOVIE)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.WRITER_MOVIE)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.ACTORS_MOVIE)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.PLOT_MOVIE)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.COUNTRY_MOVIE)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.AWARDS_MOVIE)),
                cursor.getString(cursor.getColumnIndex(ColumnMovie.IMDB_RATING_MOVIE)));
    }

    public boolean existPelicula(String imdbID) {
        String selection = ColumnMovie.IMDBID_MOVIE + " = ?";
        String[] selectionArgs = {imdbID};
        Cursor cursor = getAnyRow(MOVIE_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        return cursor.moveToNext();
    }

    //borrar una peli por pelicula
    public void eliminarPelicula(String imdbID) {
        String selection = ColumnMovie.IMDBID_MOVIE + " = ?";
        String[] selectionArgs = {imdbID};

        deleteAnyRow(MOVIE_TABLE_NAME, selection, selectionArgs);
    }


    //Devuelve todas las peliculas
    public Cursor getAllMovies() {
        //Seleccionamos todas las filas de la tabla Movie
        return database.rawQuery(
                "select * from " + MOVIE_TABLE_NAME, null);
    }

    public void actualizarMovie(int id, int rating, String comment, boolean view) {
        //Nuestro contenedor de valores
        ContentValues values = new ContentValues();

        //Seteando body y author
        values.put(ColumnMovie.RATING_MOVIE, rating);
        values.put(ColumnMovie.COMMENT_MOVIE, comment);
        values.put(ColumnMovie.VIEW_MOVIE, view);

        //Clausula WHERE
        String selection = ColumnMovie.ID_MOVIE + " = ?";
        String[] selectionArgs = {Integer.toString(id)};

        //Actualizando
        database.update(MOVIE_TABLE_NAME, values, selection, selectionArgs);
    }

    //obtener una peli por su id
    public Cursor getMovie(int id) {
        String selection = ColumnMovie.ID_MOVIE + " = ?";
        String[] selectionArgs = {Integer.toString(id)};
        return getAnyRow(MOVIE_TABLE_NAME, null, selection, selectionArgs, null, null, null);
    }

    //borrar una peli por su id
    public void removeMovie(int id) {
        String selection = ColumnMovie.ID_MOVIE + " = ?";
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
        if(lista.getId() == 0) {
            //Nuestro contenedor de valores
            ContentValues values = new ContentValues();

            //Seteando columnas
            values.put(ColumnList.NAME_LIST, lista.getNombre());
            values.put(ColumnList.DESCRIPTION_LIST, lista.getDescripcion());

            //Insertando en la base de datos
            database.insert(LIST_TABLE_NAME, null, values);
        }
        else{
            actualizarList(lista.getId(), lista.getNombre(),lista.getDescripcion());
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
        public static final String ID_MOVIE_MOVIESLIST = "idMovie";
        public static final String ID_LIST_MOVIESLIST = "idList";
    }

    //Script de Creación de la tabla MoviesList
    public static final String CREATE_MOVIESLIST_SCRIPT =
            "create table " + MOVIESLIST_TABLE_NAME + "(" +
                    ColumnMoviesList.ID_MOVIE_MOVIESLIST + " " + INT_TYPE + "," +
                    ColumnMoviesList.ID_LIST_MOVIESLIST + " " + INT_TYPE + "," +
                    "PRIMARY KEY(" + ColumnMoviesList.ID_MOVIE_MOVIESLIST + "," +
                    ColumnMoviesList.ID_LIST_MOVIESLIST + ")" + "," +
                    "FOREIGN KEY(" + ColumnMoviesList.ID_MOVIE_MOVIESLIST + ") REFERENCES " +
                    MOVIE_TABLE_NAME + "(" + ColumnMovie.ID_MOVIE + ") ON DELETE CASCADE," +
                    "FOREIGN KEY(" + ColumnMoviesList.ID_LIST_MOVIESLIST + ") REFERENCES " +
                    LIST_TABLE_NAME + "(" + ColumnList.ID_LIST + ") ON DELETE CASCADE" + ")";


    //Script para borrar la base de datos
    public static final String DROP_MOVIESLIST_SCRIPT = "DROP TABLE IF EXISTS " + MOVIESLIST_TABLE_NAME;

    //Metodo para añadir una pelicula a una lista
    public void saveMoviesListRow(int idMovie, int idList) {
        //Nuestro contenedor de valores
        ContentValues values = new ContentValues();

        //Seteando columnas
        values.put(ColumnMoviesList.ID_MOVIE_MOVIESLIST, idMovie);
        values.put(ColumnMoviesList.ID_LIST_MOVIESLIST, idList);

        //Insertando en la base de datos
        database.insert(MOVIESLIST_TABLE_NAME, null, values);
    }

    public void addPeliculaLista(Pelicula pelicula, Lista lista){
        saveMoviesListRow(pelicula.getID(), lista.getId());
    }

    public void removePeliculaLista(Pelicula pelicula, Lista lista){
        removeMovieInList(pelicula.getID(),lista.getId());
    }

    //obtener las peliculas de una lista
    public Cursor getMoviesInList(int id) {
        String selection = ColumnMoviesList.ID_LIST_MOVIESLIST + " = ?";
        String[] selectionArgs = {Integer.toString(id)};
        String columns[] = new String[]{ColumnMoviesList.ID_MOVIE_MOVIESLIST};
        return getAnyRow(MOVIESLIST_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
    }

    //borrar todas las peliculas de una lista
    public void removeAllMoviesInList(int id) {
        String selection = ColumnMoviesList.ID_LIST_MOVIESLIST + " = ?";
        String[] selectionArgs = {Integer.toString(id)};

        deleteAnyRow(MOVIESLIST_TABLE_NAME, selection, selectionArgs);
    }

    //borrar una movie de una lista
    public void removeMovieInList(int idMovie, int idList) {
        String selection = ColumnMoviesList.ID_LIST_MOVIESLIST + " = ?   AND " +
                ColumnMoviesList.ID_MOVIE_MOVIESLIST + " = ?";
        String[] selectionArgs = {Integer.toString(idList), Integer.toString(idMovie)};

        deleteAnyRow(MOVIESLIST_TABLE_NAME, selection, selectionArgs);
    }

}
