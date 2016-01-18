package com.example.user.w2w.BaseDatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDBHelper extends SQLiteOpenHelper {
    /**
     * Nombre y version de la base de datos
     */
    private static final String DATABASE_NAME = "W2W.db";
    private static final int DATABASE_VERSION = 1;



    public MyDBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Crea la tabla movie
        db.execSQL(MyDataSource.CREATE_MOVIE_SCRIPT);

        //Crea la tabla list
        db.execSQL(MyDataSource.CREATE_LIST_SCRIPT);

        //Crea la tabla MoviesList
        db.execSQL(MyDataSource.CREATE_MOVIESLIST_SCRIPT);

        //Insertar listas por defecto
        db.execSQL(MyDataSource.INSERT_LIST_SCRIPT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MyDataSource.DROP_MOVIESLIST_SCRIPT);
        db.execSQL(MyDataSource.DROP_MOVIE_SCRIPT);
        db.execSQL(MyDataSource.DROP_LIST_SCRIPT);
        this.onCreate(db);

    }


    //activa las foreignKey
    @Override
    public void onConfigure(SQLiteDatabase db){
        db.setForeignKeyConstraintsEnabled(true);
    }
}
