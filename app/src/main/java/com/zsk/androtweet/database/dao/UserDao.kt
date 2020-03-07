package com.zsk.androtweet.database.dao

import androidx.room.*
import com.zsk.androtweet.models.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@ExperimentalCoroutinesApi
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: User): Long

    @Delete
    suspend fun delete(vararg models: User): Int

    @Update
    suspend fun update(model: User): Int

    @Query("SELECT * FROM user LIMIT 1")
    fun get(): Flow<User>

    fun getDistinctUntilChanged(name: String) =
            get().distinctUntilChanged()

    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("DELETE FROM user")
    suspend fun deleteAll()

}