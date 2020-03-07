package com.zsk.androtweet

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.kaloglu.library.ui.BaseApplication
import com.kaloglu.library.ui.interfaces.Repository
import com.kaloglu.library.ui.viewmodel.BaseViewModel
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterConfig
import com.zsk.androtweet.database.AndroTweetDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi

/**
 * Created by kaloglu on 24/04/16.
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@Suppress("MemberVisibilityCanBePrivate")
class AndroTweetApp : BaseApplication() {
    override fun onCreate() {
        INSTANCE = this
        super.onCreate()

        val config = TwitterConfig.Builder(this)
                .logger(DefaultLogger(Log.DEBUG))
                .debug(true)
                .build()
        Twitter.initialize(config)

        database = Room
                .databaseBuilder(this, AndroTweetDatabase::class.java, "AndroTweet")
                .fallbackToDestructiveMigration()
                .build()

    }

    companion object {
        @Volatile
        private var INSTANCE: AndroTweetApp? = null
        fun getInstance() = INSTANCE!!

        @JvmStatic
        var daysAgo = 0

        @JvmStatic
        var tweetId: String? = null

        lateinit var database: AndroTweetDatabase

    }
}