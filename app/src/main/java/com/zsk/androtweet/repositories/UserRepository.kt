package com.zsk.androtweet.repositories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import com.kaloglu.library.ui.interfaces.Repository
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.database.AndroTweetDatabase
import com.zsk.androtweet.database.dao.UserDao
import com.zsk.androtweet.models.User

class UserRepository private constructor(private val userDao: UserDao) : Repository<User> {
    fun clean() = userDao.deleteAll()

    override val data: MutableLiveData<List<User>> = MutableLiveData()

    override fun delete(entity: User) {
        userDao.delete(entity)
    }

    override fun insert(entity: User) {
        userDao.insert(entity)
    }

    override fun update(entity: User) {
        userDao.update(entity)
    }

    fun get() = userDao.get()
    fun getDUC() = get().distinctUntilChanged()
    fun getAll() = userDao.getAll()

    companion object {
        @Volatile
        private lateinit var INSTANCE: UserRepository

        fun getInstance(database: AndroTweetDatabase = AndroTweetApp.database): UserRepository {
            synchronized(this) {
                if (!::INSTANCE.isInitialized)
                    INSTANCE = UserRepository(database.userDao())
            }
            return INSTANCE
        }
    }
}