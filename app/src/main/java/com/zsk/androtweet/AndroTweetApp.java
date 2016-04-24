package com.zsk.androtweet;

import android.app.Application;

/**
 * Created by kaloglu on 24/04/16.
 */
public class AndroTweetApp extends Application {
    private static Application instance;

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }
}
