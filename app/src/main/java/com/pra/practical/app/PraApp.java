package com.pra.practical.app;

import android.app.Application;


public class PraApp extends Application {

    private static PraApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized PraApp getInstance() {
        return mInstance;
    }

}
