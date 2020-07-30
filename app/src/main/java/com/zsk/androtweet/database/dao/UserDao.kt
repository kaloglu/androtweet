package com.zsk.androtweet.database.dao

import androidx.room.*
import com.zsk.androtweet.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: User): Long

    @Delete
    suspend fun delete(vararg models: User): Int

    @Update
    suspend fun update(model: User): Int

    @Query("SELECT * FROM user LIMIT 1")
    fun getUser(): Flow<User?>

    fun getUserDistinctUntilChanged() = getUser().distinctUntilChanged()

    @Query("DELETE FROM user")
    suspend fun deleteAll()
}