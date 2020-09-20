package com.zsk.androtweet

import android.content.Context
import androidx.room.Room
import com.kaloglu.library.ui.BaseApplication
import com.twitter.sdk.android.core.*
import com.zsk.androtweet.database.AndroTweetDatabase
import com.zsk.androtweet.remote.CustomApiClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

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
        private lateinit var ACTIVE_SESSION: TwitterSession

        @Volatile
        private lateinit var API_CLIENT: CustomApiClient

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
                                .logger(DefaultLogger(1))
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

        val twitterCore: TwitterCore
            get() {
                synchronized(TwitterCore::class.java) {
                    if (!::TWITTER_CORE.isInitialized) {
                        TWITTER_CORE = TwitterCore.getInstance()
                    }
                }
                return TWITTER_CORE
            }

        val activeSession: TwitterSession
            get() {
                synchronized(TwitterSession::class.java) {
                    if (!::ACTIVE_SESSION.isInitialized) {
                        ACTIVE_SESSION = twitterCore.sessionManager.activeSession
                    }
                }
                return ACTIVE_SESSION
            }

        val apiClient: CustomApiClient
            get() {
                synchronized(CustomApiClient::class.java) {
                    if (!::API_CLIENT.isInitialized || activeSession != twitterCore.sessionManager.activeSession) {
                        val loggingInterceptor = HttpLoggingInterceptor()
                        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                        val customClient = OkHttpClient.Builder()
                                .addInterceptor(loggingInterceptor).build()

                        twitterCore.addApiClient(activeSession, CustomApiClient(activeSession, customClient))
                        API_CLIENT = twitterCore.apiClient as CustomApiClient
                    }
                }
                return API_CLIENT
            }
    }
}
