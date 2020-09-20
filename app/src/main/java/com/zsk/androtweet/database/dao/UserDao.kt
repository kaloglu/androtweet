package com.zsk.androtweet.database.dao

import androidx.room.*
import com.zsk.androtweet.models.UserFromDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: UserFromDao): Long

    @Delete
    suspend fun delete(vararg models: UserFromDao): Int

    @Update
    suspend fun update(model: UserFromDao): Int

    @Query("SELECT * FROM user LIMIT 1")
    fun getUser(): Flow<UserFromDao?>

    fun getUserDistinctUntilChanged() = getUser().distinctUntilChanged()

    @Query("DELETE FROM user")
    suspend fun deleteAll()
}