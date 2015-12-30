package com.example.user.testiguandroid.BaseDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;



 public class ListaDataSource {

    private SQLiteDatabase baseDatos;

    private MyDBHelper dbHelper;

     //TODO
     //Columnas de la tabla Lista
    private final String[] allColumns = { MyDBHelper.COLUMN_ID, MyDBHelper.COLUMN_USER,
            MyDBHelper.COLUMN_COMMENT, MyDBHelper.COLUMN_RATING };


     //TODO

    public ListaDataSource(Context context) {
        dbHelper = new MyDBHelper(context, null, null, 0);
    }


    public void open() throws SQLException {
        baseDatos = dbHelper.getWritableDatabase();
    }


    public void close() {
        dbHelper.close();
    }


     //TODO
     //logica de las listas de peliculas
    /*
    public long createLista(final Valoration valorationToInsert) {
        // Establecemos los valores que se insertaran
        final ContentValues values = new ContentValues();
        values.put(MyDBHelper.COLUMN_COMMENT, valorationToInsert.getComment());
        values.put(MyDBHelper.COLUMN_USER, valorationToInsert.getCourse());
        values.put(MyDBHelper.COLUMN_RATING, valorationToInsert.getRating());

        // Insertamos la valoracion
        final long insertId = database.insert(MyDBHelper.TABLE_VALORATIONS, null, values);

        return insertId;
    }

    /**
     * Obtiene todas las valoraciones andadidas por los usuarios.
     *
     * @return Lista de objetos de tipo Valoration
     */
     /*
    public List<Valoration> getAllValorations() {
        // Lista que almacenara el resultado
        List<Valoration> valorationList = new ArrayList<Valoration>();
        Cursor cursor = database.query(MyDBHelper.TABLE_VALORATIONS, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            final Valoration valoration = new Valoration();
            valoration.setCourse(cursor.getString(1));
            valoration.setComment(cursor.getString(2));
            valoration.setRating(cursor.getInt(3));

            valorationList.add(valoration);
            cursor.moveToNext();
        }

        cursor.close();
        // Una vez obtenidos todos los datos y cerrado el cursor, devolvemos la
        // lista.
        return valorationList;
    }
     */

}
