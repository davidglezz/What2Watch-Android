package com.w2w.Logica;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Util {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = con.getActiveNetworkInfo();
        return net != null && net.isConnectedOrConnecting();
    }

}
