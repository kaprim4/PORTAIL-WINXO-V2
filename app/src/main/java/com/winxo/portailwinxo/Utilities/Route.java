package com.winxo.portailwinxo.Utilities;

import android.annotation.SuppressLint;
import android.app.Activity;

public class Route {

    @SuppressLint("StaticFieldLeak")
    private static Route instance;
    private final Activity activity;

    private Route(Activity activity) {
        this.activity = activity;
    }

    public static synchronized Route getInstance(Activity activity) {
        if (instance == null) {
            instance = new Route(activity);
        }
        return instance;
    }

}
