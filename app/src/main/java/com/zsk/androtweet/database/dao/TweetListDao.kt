package com.zsk.androtweet.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.zsk.androtweet.models.TweetFromDao

@Dao
interface TweetListDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(list: List<TweetFromDao>)

    @Delete
    suspend fun delete(list: List<TweetFromDao>): Int

    @Update(onConflict = REPLACE)
    suspend fun update(list: List<TweetFromDao>)

    @Query("SELECT * FROM tweets WHERE tweet_user_id=:userId ORDER BY createdAt desc")
    fun getTweets(userId: Long): PagingSource<Int, TweetFromDao>

    @Transaction
    fun get(userId: Long): PagingSource<Int, TweetFromDao> {
//        deletePersist()
        return getTweets(userId)
    }

    @Query("SELECT tweet_id FROM tweets WHERE tweet_user_id=:userId ORDER BY createdAt desc LIMIT 1")
    fun getNewestId(userId: Long): Long

//    @Query("DELETE FROM tweets WHERE isDeleted = :isDeleted")
//    fun deletePersist(@VisibleForTesting isDeleted: Boolean = true): PagingSource<Long, TweetFromDao>

}