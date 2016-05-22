package com.zsk.androtweet;

import android.app.Application;

/**
 * Created by kaloglu on 24/04/16.
 */
public class AndroTweetApp extends Application {
    private static Application instance;
    private static int daysAgo;
    private static String userName;
    private static String tweetId;
    private static Object tweetID;

    public static Application getInstance() {
        return instance;
    }

    public static void setDaysAgo(int daysAgo) {
        AndroTweetApp.daysAgo = daysAgo;
    }

    public static void setUserName(String userName) {
        AndroTweetApp.userName = userName;
    }

    public static void setTweetId(String tweetId) {
        AndroTweetApp.tweetId = tweetId;
    }

    public static String getTweetId() {
        return tweetId;
    }

    public static int getDaysAgo() {
        return daysAgo;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }
}
