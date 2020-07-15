package com.zsk.androtweet

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.kaloglu.library.ui.BaseApplication
import com.twitter.sdk.android.core.*
import com.zsk.androtweet.database.AndroTweetDatabase
import com.zsk.androtweet.models.User


/**
 * Created by kaloglu on 24/04/16.
 */
@Suppress("MemberVisibilityCanBePrivate")
class AndroTweetApp : BaseApplication() {

    override fun onCreate() {
        INSTANCE = this
        super.onCreate()

        Twitter.initialize(twitterConfig)

    }

    companion object {
        @Volatile
        private lateinit var INSTANCE: AndroTweetApp

        @Volatile
        private lateinit var TWITTER_CONFIG: TwitterConfig

        @Volatile
        private lateinit var DATABASE: AndroTweetDatabase

        @Volatile
        private lateinit var TWITTER_CORE: TwitterCore

        @Volatile
        private lateinit var API_CLIENT: TwitterApiClient

        private val context: Context
            get() = INSTANCE.applicationContext

        val instance: AndroTweetApp
            get() {
                synchronized(this) {
                    if (!::INSTANCE.isInitialized)
                        INSTANCE = AndroTweetApp()
                }
                return INSTANCE
            }

        val twitterConfig: TwitterConfig
            get() {
                synchronized(TwitterConfig::class.java) {
                    if (!::TWITTER_CONFIG.isInitialized)
                        TWITTER_CONFIG = TwitterConfig.Builder(context)
                                .logger(DefaultLogger(Log.DEBUG))
                                .debug(true)
                                .build()
                }
                return TWITTER_CONFIG
            }

        val database: AndroTweetDatabase
            get() {
                synchronized(AndroTweetDatabase::class.java) {
                    if (!::DATABASE.isInitialized) {
                        DATABASE = Room
                                .databaseBuilder(context, AndroTweetDatabase::class.java, "AndroTweet")
                                .fallbackToDestructiveMigration()
                                .build()
                    }
                }
                return DATABASE
            }

        /*val twitterCore: TwitterCore
            get() {
                synchronized(TwitterCore::class.java) {
                    if (!::TWITTER_CORE.isInitialized) {
                        TWITTER_CORE = TwitterCore.getInstance()
                    }
                }
                return TWITTER_CORE
            }*/

        /*val apiClient: TwitterApiClient
            get() {
                synchronized(TwitterApiClient::class.java) {
                    val activeSession = twitterCore.sessionManager.activeSession

                    val customClient = OkHttpClient.Builder()
                            .addInterceptor(
                                    HttpLoggingInterceptor().apply {
                                        setLevel(HttpLoggingInterceptor.Level.BODY)
                                    }
                            ).build()

                    if (activeSession != null) {
                        twitterCore.addApiClient(activeSession, TwitterApiClient(activeSession, customClient))
                    } else {
                        twitterCore.addGuestApiClient(TwitterApiClient(customClient))
                    }

                    API_CLIENT = twitterCore.apiClient
                }
                return API_CLIENT
            }*/
    }
}
