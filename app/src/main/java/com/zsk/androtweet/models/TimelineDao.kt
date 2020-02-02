package com.zsk.androtweet.models

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TimelineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tweet: Tweet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg tweet: Tweet)

    @Delete
    fun delete(vararg tweet: Tweet)

    @Query("DELETE FROM tweets")
    fun deleteAll()

    @Update
    fun setRemoved(tweet: Tweet)

    @get:Query("SELECT * FROM tweets ORDER BY time DESC")
    val timeline: LiveData<List<Tweet>>

    @get:Query("SELECT * FROM tweets ORDER BY time DESC")
    val allTweets: List<Tweet>

    @get:Query("SELECT * FROM tweets WHERE text not like 'RT @%' and text not like '@%' ORDER BY time DESC")
    val justTweets: List<Tweet>

    @get:Query("SELECT * FROM tweets WHERE text like 'RT @%' ORDER BY time DESC")
    val retweeted: List<Tweet>

    @get:Query("SELECT * FROM tweets WHERE text like '@%' ORDER BY time DESC")
    val mentions: List<Tweet>
}