package com.zsk.androtweet.database.dao.base

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.kaloglu.library.ui.BaseModel

interface BaseDao<T : BaseModel> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(model: T)

    @Delete
    fun delete(vararg models: T)

    @Update
    fun update(model: T)

    fun get(): LiveData<T>

    fun deleteAll()
}
