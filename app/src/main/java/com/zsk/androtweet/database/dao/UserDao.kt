package com.zsk.androtweet.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.zsk.androtweet.database.dao.base.BaseDao
import com.zsk.androtweet.models.User

@Dao
interface UserDao : BaseDao<User> {

    @Query("SELECT * FROM user LIMIT 1")
    override fun get(): LiveData<User>

    @Query("DELETE FROM user")
    override fun deleteAll()

}