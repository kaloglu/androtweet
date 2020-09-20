package com.zsk.androtweet.repositories

import androidx.lifecycle.LifecycleObserver
import com.kaloglu.library.ui.interfaces.Repository
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.database.AndroTweetDatabase
import com.zsk.androtweet.database.dao.UserDao
import com.zsk.androtweet.models.UserFromDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

@ExperimentalCoroutinesApi
class UserRepository private constructor(private val userDao: UserDao) : Repository<UserFromDao>, LifecycleObserver {

    suspend fun clean() = userDao.deleteAll()

    override suspend fun delete(entity: UserFromDao) {
        userDao.delete(entity)
    }

    override suspend fun insert(entity: UserFromDao) {
        userDao.insert(entity)
    }

    override suspend fun update(entity: UserFromDao) {
        userDao.update(entity)
    }

    fun getUser() = userDao.getUserDistinctUntilChanged()

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