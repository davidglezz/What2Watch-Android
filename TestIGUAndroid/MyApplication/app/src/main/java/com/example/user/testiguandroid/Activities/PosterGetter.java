package com.example.user.testiguandroid.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.user.testiguandroid.Logica.Pelicula;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

class PosterGetter extends AsyncTask<String,Void,Bitmap>{


    private MainActivity main;
    private Pelicula p;

    public PosterGetter(MainActivity mainActivity, Pelicula p) {
        this.main=mainActivity;
        this.p=p;
    }

    @Override
    protected Bitmap  doInBackground(String... params) {
        String urldisplay = params[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
           e.printStackTrace();
        }
        return mIcon11;
    }




    @Override
    protected void onPostExecute(Bitmap  result){


        main.asyncResult(p,result);


    }
}

