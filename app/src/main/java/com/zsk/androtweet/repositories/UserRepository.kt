package com.zsk.androtweet.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.kaloglu.library.ui.interfaces.Repository
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class UserRepository private constructor() : Repository<User> {

    private val userDao by lazy { AndroTweetApp.database.userDao() }

    suspend fun clean() = userDao.deleteAll()

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
            .flowOn(Dispatchers.Default)
            .conflate()

    fun getAll() = userDao.getAll()

    override suspend fun delete(entity: User) {
        userDao.delete(entity)
    }

    override suspend fun insert(entity: User) {
        userDao.insert(entity)
    }

    override suspend fun update(entity: User) {
        userDao.update(entity)
    }

}