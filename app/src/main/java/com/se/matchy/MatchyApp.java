package com.se.matchy;

import android.app.Application;

import timber.log.Timber;

public class MatchyApp extends Application {
    //region Application members

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
    }


    //endregion
}
