package com.zsk.androtweet.database.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.room.*
import com.zsk.androtweet.models.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(model: User): Long

    @Delete
    fun delete(vararg models: User): Int

    @Update
    fun update(model: User): Int

    @Query("SELECT * FROM user LIMIT 1")
    fun get(): LiveData<User>

    fun getDistinctUntilChanged(name: String) =
            get().distinctUntilChanged()

    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("DELETE FROM user")
    fun deleteAll()
}