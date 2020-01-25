package com.zsk.androtweet

import android.app.Application

/**
 * Created by kaloglu on 24/04/16.
 */
class AndroTweetApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        var instance: Application? = null
            private set
        @JvmStatic
        var daysAgo = 0

        @JvmStatic
        var tweetId: String? = null

    }
}