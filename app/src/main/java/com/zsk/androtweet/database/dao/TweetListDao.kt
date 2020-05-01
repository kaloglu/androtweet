package com.zsk.androtweet.database.dao

import androidx.room.*
import com.zsk.androtweet.models.Tweet
import kotlinx.coroutines.flow.Flow

@Dao
interface TweetListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<Tweet>)

    @Delete
    suspend fun delete(list: List<Tweet>): Int

    @Query("SELECT * FROM tweets WHERE tweet_user_id=:userId LIMIT :count")
    fun get(userId: Long, count: Int): Flow<List<Tweet>>

}