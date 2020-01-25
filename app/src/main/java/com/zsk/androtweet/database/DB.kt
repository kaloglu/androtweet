package com.zsk.androtweet.database

import android.content.ContentValues
import android.provider.BaseColumns
import com.zsk.androtweet.models.Tweet

object DB {
    internal const val DBNAME = "AndroTweet"
    internal const val DBVERSION = 7
    internal const val DROP_QUERY = "DROP TABLE IF EXISTS "
    internal const val TIMELINE_CREATE = "CREATE TABLE TIMELINE (_id INTEGER PRIMARY KEY AUTOINCREMENT , tweet_id LONG, reply_id LONG, tweet TEXT, tweettime INTEGER, RT LONG, FAV LONG );"
    internal const val TIMELINE_DROP = "DROP TABLE IF EXISTS TIMELINE"
    const val TIMELINE_TABLE = "TIMELINE"
    @JvmStatic
    fun CreateTimeLineData(paramTweet: Tweet): ContentValues {
        val localContentValues = ContentValues()
        localContentValues.put("tweet_id", java.lang.Long.valueOf(paramTweet.id))
        localContentValues.put("reply_id", java.lang.Long.valueOf(paramTweet.replyId))
        localContentValues.put("tweet", paramTweet.tweetText)
        localContentValues.put("tweettime", java.lang.Long.valueOf(paramTweet.time))
        localContentValues.put("RT", Integer.valueOf(paramTweet.rtCount))
        localContentValues.put("FAV", Integer.valueOf(paramTweet.favcount))
        return localContentValues
    }

    object Timeline : BaseColumns {
        const val FAV = "FAV"
        const val REPLY_ID = "reply_id"
        const val RT = "RT"
        const val TIME = "tweettime"
        const val TWEET = "tweet"
        const val TWEET_ID = "tweet_id"
    }
}