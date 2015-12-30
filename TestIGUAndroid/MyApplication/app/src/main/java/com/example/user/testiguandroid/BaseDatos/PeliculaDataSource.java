package com.example.user.testiguandroid.BaseDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.user.testiguandroid.Logica.Pelicula;


public class PeliculaDataSource {

    private SQLiteDatabase database;

    private MyDBHelper dbHelper;


    //TODO columnas de la tabla Pelicula

    private final String[] allColumns = { MyDBHelper.COLUMN_ID, MyDBHelper.COLUMN_USER,
            MyDBHelper.COLUMN_COMMENT, MyDBHelper.COLUMN_RATING };
    //
    //TODO columnas de la tabla Pelicula


    public PeliculaDataSource(Context context) {
        dbHelper = new MyDBHelper(context, null, null, 0);
    }


    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }


    public void close() {

        dbHelper.close();
    }


    public long createPelicula(final Pelicula peliculaToInsert) {

        final ContentValues values = new ContentValues();
       // values.put(MyDBHelper.COLUMN_COMMENT, valorationToInsert.getComment());
        //values.put(MyDBHelper.COLUMN_USER, valorationToInsert.getCourse());
        //values.put(MyDBHelper.COLUMN_RATING, valorationToInsert.getRating());
    //TODO meter una pelicula
        // Insertamos la valoracion
        final long insertId;//= database.insert(MyDBHelper.TABLE_VALORATIONS, null, values);

        return 0L;//insertId;
    }


}
