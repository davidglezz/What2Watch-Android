package com.example.user.w2w;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by USER on 29/12/2015.
 */
public class ThemeChanger {

    private static int temaActual;

    public final static int DAY = 0;
    public final static int CINEMA = 1;



    public static void changeToTheme(Activity activity, int temaID)
    {
        temaActual = temaID;
        activity.finish();

        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity)
    {
       if(temaActual==CINEMA) {
           activity.setTheme(R.style.AppThemeCinemaMode);
       }


    }
}
