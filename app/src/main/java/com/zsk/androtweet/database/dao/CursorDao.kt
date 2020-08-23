package com.zsk.androtweet.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.zsk.androtweet.models.ListType
import com.zsk.androtweet.models.TweetCursor
import kotlinx.coroutines.flow.Flow

@Dao
interface CursorDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(cursors: List<TweetCursor>)

    @Query("SELECT * FROM tweet_cursor WHERE type = :listType")
    fun cursor(listType: ListType): Flow<TweetCursor?>

    @Query("DELETE FROM tweet_cursor WHERE type=:listType")
    suspend fun clearCursors(listType: ListType)

}
