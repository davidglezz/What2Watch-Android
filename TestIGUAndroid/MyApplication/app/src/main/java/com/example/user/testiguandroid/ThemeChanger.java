package com.example.user.testiguandroid;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by USER on 29/12/2015.
 */
public class ThemeChanger {

    private static int sTheme;

    public final static int DAY = 0;
    public final static int CINEMA = 1;



    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
        activity.finish();

        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity)
    {
        switch (sTheme)
        {
            default:
            case DAY:
                break;
            case CINEMA:
                activity.setTheme(R.style.AppThemeCinemaMode);
                break;

        }
    }
}
