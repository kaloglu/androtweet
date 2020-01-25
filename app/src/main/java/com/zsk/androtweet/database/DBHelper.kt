package com.zsk.androtweet.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(paramContext: Context?) : SQLiteOpenHelper(paramContext, "AndroTweet", null, 7) {
    fun freshDB(paramSQLiteDatabase: SQLiteDatabase?) {
        paramSQLiteDatabase?.let {
            it.execSQL("DROP TABLE IF EXISTS TIMELINE")
            onCreate(it)
        }
    }

    override fun onCreate(paramSQLiteDatabase: SQLiteDatabase) {
        paramSQLiteDatabase.execSQL("CREATE TABLE TIMELINE (_id INTEGER PRIMARY KEY AUTOINCREMENT , tweet_id LONG, reply_id LONG, tweet TEXT, tweettime INTEGER, RT LONG, FAV LONG );")
    }

    override fun onUpgrade(paramSQLiteDatabase: SQLiteDatabase, paramInt1: Int, paramInt2: Int) {
        freshDB(paramSQLiteDatabase)
    }
}