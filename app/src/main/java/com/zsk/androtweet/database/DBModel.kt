package com.zsk.androtweet.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.zsk.androtweet.database.DB.createTimeLineData
import com.zsk.androtweet.database.DB.TIMELINE_TABLE
import com.zsk.androtweet.database.DB.Timeline
import com.zsk.androtweet.models.Search.Companion.instance
import com.zsk.androtweet.models.Tweet
import twitter4j.Status
import java.util.*

class DBModel(context: Context?) {
    private val lastTweetid = 0L
    fun close() {
        db?.close()
    }

    fun drop() {
        dbhelper!!.freshDB(db)
    }

    fun insertTweetList(statuses: List<Status?>) {
        for (status in statuses) {
            insertTweet(Tweet(status!!))
        }
    }

    private fun insertTweet(tweet: Tweet) {
        if (checkTweet(tweet.id)) {
            return
        }
        instance.lastTweetId = tweet.id
        db?.insert(TIMELINE_TABLE, null, createTimeLineData(tweet))
    }

    /* (non-Javadoc)
     * @see
     *
     * (e.g. HOLDED_REC_COUNT = 100;)
     */
    val tweetList: List<Tweet>
        get() = getTweetList()

    private fun getTweetList(count: Int = HOLDED_REC_COUNT): List<Tweet> {
        val tweets: MutableList<Tweet> = ArrayList()
        val search = instance
        var query = ""
        if (search.isViewRTs) query += "SELECT * FROM " + TIMELINE_TABLE + " WHERE " + Timeline.TWEET + " like 'RT @%'"
        if (search.isViewRTs && search.isViewMentions) query += " UNION ALL "
        if (search.isViewMentions) query += "SELECT * FROM " + TIMELINE_TABLE + " WHERE " + Timeline.TWEET + " like '@%'"
        if (search.isViewMyTweets && (search.isViewMentions || search.isViewRTs)) query += " UNION ALL "
        if (search.isViewMyTweets) query += "SELECT * FROM " + TIMELINE_TABLE + " WHERE " + Timeline.TWEET + " not like '@%' and " + Timeline.TWEET + " not like 'RT @%'"
        if (query != "") {
            val selectQuery = "select * FROM (" + query + ") as foo" +
                    " ORDER BY " + Timeline.TIME + " DESC" +
                    " LIMIT " + count
            //            log("QUERY: " + selectQuery);
            val cursor = db?.rawQuery(selectQuery, null)
            // looping through all rows and adding to list
            if (cursor?.moveToFirst() == true) {
                do {
                    val tweet = Tweet(cursor)
                    // adding to todo list
                    tweets.add(tweet)
                } while (cursor.moveToNext())
            }
        }
        return tweets
    }

    fun deleteTweet(tweet: Tweet): Boolean {
        val deleteQuery = "delete from " + TIMELINE_TABLE + " WHERE " + Timeline.TWEET_ID + " = " + tweet.id
        db?.execSQL(deleteQuery)
        return true
    }

    fun deleteOldTweets() {
        db?.execSQL("delete from $TIMELINE_TABLE")
    }

    companion object {
        const val HOLDED_REC_COUNT = 150
        private var dbhelper: DBHelper? = null
        private var db: SQLiteDatabase? = null
        fun checkTweet(tweet_id: Long): Boolean {
            var checkTweet = false
            val selectQuery = "SELECT  " + Timeline.TWEET_ID +
                    " FROM " + TIMELINE_TABLE + " as TWEET  " +
                    "WHERE TWEET." + Timeline.TWEET_ID + " = " + tweet_id
            //		log("QUERY: " + selectQuery);
            val cursor = db?.rawQuery(selectQuery, null)
            if (cursor != null) checkTweet = cursor.count > 0
            return checkTweet
        }
    }

    init {
        if (dbhelper == null) dbhelper = DBHelper(context)
        db = dbhelper?.writableDatabase
    }
}