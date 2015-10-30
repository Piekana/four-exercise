package com.example.foursquare.application;

import android.app.Application;
import android.content.Context;

public class ApplicationContextHelper extends Application {

    // Reference to application context
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    /**
     * Returns the application context
     *
     * Note! This cannot be used for instantiating views!
     *
     * @return application context
     */
    public static Context getContext() {
        return sContext;
    }
}
