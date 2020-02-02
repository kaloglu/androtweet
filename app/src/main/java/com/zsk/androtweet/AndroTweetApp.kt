package com.zsk.androtweet

import android.app.Application
import androidx.room.Room
import com.zsk.androtweet.database.TweetDatabase

/**
 * Created by kaloglu on 24/04/16.
 */
class AndroTweetApp : Application() {
    override fun onCreate() {
        instance = this
        super.onCreate()

        database = Room
                .databaseBuilder(this, TweetDatabase::class.java, "AndroTweet")
                .fallbackToDestructiveMigration()
                .build()
    }

    companion object {
        var instance: Application? = null
            private set
        @JvmStatic
        var daysAgo = 0

        @JvmStatic
        var tweetId: String? = null

        lateinit var database: TweetDatabase

    }
}