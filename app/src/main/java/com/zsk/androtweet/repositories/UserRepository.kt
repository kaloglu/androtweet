package com.zsk.androtweet.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.kaloglu.library.ui.interfaces.Repository
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn

class UserRepository private constructor() : Repository<User> {
    private val userDao by lazy { AndroTweetApp.database.userDao() }
    fun clean() = userDao.deleteAll()

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

    override val data: LiveData<List<User>> = liveData { }
    fun get() = userDao.get()

    fun getAll() = userDao.getAll()

    override fun delete(entity: User) {
        userDao.delete(entity)
    }

    override fun insert(entity: User) {
        userDao.insert(entity)
    }

    override fun update(entity: User) {
        userDao.update(entity)
    }
}