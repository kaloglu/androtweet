package com.zsk.androtweet.repositories

import androidx.lifecycle.MutableLiveData
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.database.dao.UserDao
import com.zsk.androtweet.interfaces.Repository
import com.zsk.androtweet.models.Resource
import com.zsk.androtweet.models.User
import com.zsk.androtweet.simpletasks.SimpleDelete
import com.zsk.androtweet.simpletasks.SimpleDeleteAll
import com.zsk.androtweet.simpletasks.SimpleInsert
import com.zsk.androtweet.simpletasks.SimpleUpdate

class UserRepository : Repository<User> {
    private val userDao: UserDao = AndroTweetApp.database.userDao()

    override val result: MutableLiveData<Resource<User>> = MutableLiveData()

    override fun insert(entity: User) {
        SimpleInsert(this, userDao).execute(entity)
    }

    override fun delete(entity: User) {
        SimpleDelete(this, userDao).execute(entity)
    }

    override fun update(entity: User) {
        SimpleUpdate(this, userDao).execute(entity)
    }

    override fun get() = userDao.get()
//        SimpleGet(this, userDao).execute()
//        return result

    fun clean() {
        SimpleDeleteAll(this, userDao).execute()
    }

}