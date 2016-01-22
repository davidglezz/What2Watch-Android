package com.w2w.Logica;

import android.app.Activity;
import android.content.Intent;

import com.w2w.R;

/**
 * Created by USER on 29/12/2015.
 */
public class ThemeChanger {
    // Themes
    public final static int THEME_LIGHT = 0;
    public final static int THEME_DARK = 1;
    public final static int THEME_CINEMA = 2;

    private static int currentTheme = 0;

    public static void changeToTheme(Activity activity, int theme) {
        currentTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void onActivityCreateSetTheme(Activity activity, int theme) {
        currentTheme = theme;
        onActivityCreateSetTheme(activity);
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        switch (currentTheme)
        {
            default:
            case THEME_LIGHT:
                //activity.setTheme(R.style.Theme_AppCompat_Light);
                activity.setTheme(R.style.theme_light);
                break;
            case THEME_DARK:
                activity.setTheme(R.style.theme_dark);
                break;
            case THEME_CINEMA:
                activity.setTheme(R.style.theme_cinema);
                break;
        }
    }
}
