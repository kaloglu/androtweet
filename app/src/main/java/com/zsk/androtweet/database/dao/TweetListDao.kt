package com.zsk.androtweet.database.dao

import androidx.room.*
import com.zsk.androtweet.models.Tweet
import com.zsk.androtweet.models.TweetWithUser
import kotlinx.coroutines.flow.Flow

@Dao
interface TweetListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<Tweet>)

    @Delete
    suspend fun delete(list: List<Tweet>): Int

    @Transaction
    @Query("SELECT * FROM tweets WHERE tweet_user_id=:userId and Date(createdAt) >= Date(NULLIF(:sinceId, '')) IS NULL ORDER BY cachedAt desc LIMIT :count ")
    fun get(userId: Long, count: Int, sinceId: Long? = null): Flow<List<TweetWithUser>>
}