package com.zsk.androtweet.database

import android.database.Cursor
import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteQuery
import com.zsk.androtweet.database.dao.TweetListDao
import com.zsk.androtweet.database.dao.UserDao
import com.zsk.androtweet.models.TweetFromDao
import com.zsk.androtweet.models.UserFromDao
import com.zsk.androtweet.utils.Converters

@TypeConverters(Converters::class)
@Database(entities = [UserFromDao::class, TweetFromDao::class], version = 26)
abstract class AndroTweetDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun tweetListDao(): TweetListDao

    /*override fun query(query: SupportSQLiteQuery): Cursor {
        //This will give you the SQL String
        //You can log it in a way you like, I am using Timber
        Log.d("ROOM_LOGGING", query.sql + "[${query.argCount}]")
        return super.query(query)
    }*/

    override fun query(query: String, args: Array<out Any>?): Cursor {
        //This will give you the SQL String
        //You can log it in a way you like, I am using Timber
        Log.d("ROOM_LOGGING", "$query [$args]")
        return super.query(query, args)
    }
}