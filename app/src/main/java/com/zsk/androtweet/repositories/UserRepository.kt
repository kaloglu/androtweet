package com.zsk.androtweet.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.kaloglu.library.ui.interfaces.Repository
import com.kaloglu.library.ui.utils.Resource
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.models.User
import com.zsk.androtweet.simpletasks.SimpleDelete
import com.zsk.androtweet.simpletasks.SimpleDeleteAll
import com.zsk.androtweet.simpletasks.SimpleInsert
import com.zsk.androtweet.simpletasks.SimpleUpdate

class UserRepository private constructor() : Repository<User> {

    private val userDao by lazy { AndroTweetApp.database.userDao() }

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

    override fun get(): LiveData<Resource<User>> = Transformations.map(userDao.get()) {
        Resource.Success(it)
    }

    fun get2(): LiveData<User> = userDao.get()
//        SimpleGet(this, userDao).execute()
//        return result

    fun clean() {
        SimpleDeleteAll(this, userDao).execute()
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(): UserRepository {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = UserRepository()
                }
            }
            return INSTANCE!!
        }
    }

}