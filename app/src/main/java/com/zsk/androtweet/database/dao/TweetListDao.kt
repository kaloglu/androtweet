package com.zsk.androtweet.database.dao

import androidx.annotation.VisibleForTesting
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.zsk.androtweet.models.TweetFromDao
import kotlinx.coroutines.flow.Flow

@Dao
interface TweetListDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(list: List<TweetFromDao>)

    @Delete
    suspend fun delete(list: List<TweetFromDao>): Int

    @Update(onConflict = REPLACE)
    suspend fun update(list: List<TweetFromDao>)

    @Query("SELECT * FROM tweets WHERE tweet_user_id=:userId ORDER BY createdAt desc")
    fun getTweets(userId: Long): Flow<List<TweetFromDao>?>

    @Query("DELETE FROM tweets WHERE isDeleted = :isDeleted")
    suspend fun deletePersist(@VisibleForTesting isDeleted: Boolean = true): Int

}
