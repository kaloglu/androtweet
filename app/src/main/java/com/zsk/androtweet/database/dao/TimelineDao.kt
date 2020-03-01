package com.zsk.androtweet.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zsk.androtweet.database.dao.base.BaseDao
import com.zsk.androtweet.models.Tweet

@Dao
interface TimelineDao : BaseDao<Tweet> {

    @Query("SELECT * FROM tweets ORDER BY time DESC LIMIT 1")
    override fun get(): LiveData<Tweet>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg tweet: Tweet)

    @Query("DELETE FROM tweets")
    override fun deleteAll()

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