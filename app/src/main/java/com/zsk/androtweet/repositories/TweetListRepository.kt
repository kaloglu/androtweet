package com.zsk.androtweet.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.database.AndroTweetDatabase
import com.zsk.androtweet.database.dao.TweetListDao
import com.zsk.androtweet.models.Tweet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged

@ExperimentalCoroutinesApi
class TweetListRepository private constructor(private val tweetListDao: TweetListDao) : Repository<List<Tweet>> {

    @WorkerThread
    override suspend fun delete(entity: List<Tweet>) = Unit

    @WorkerThread
    override suspend fun update(entity: List<Tweet>) = tweetListDao.update(entity)

    @WorkerThread
    override suspend fun insert(entity: List<Tweet>) = tweetListDao.insert(entity)

    fun get(userId: Long, count: Int, sinceId: Long? = null) = tweetListDao.get(userId, count, sinceId).distinctUntilChanged()

    companion object {

        @Volatile
        private lateinit var INSTANCE: TweetListRepository

        fun getInstance(database: AndroTweetDatabase = AndroTweetApp.database): TweetListRepository {
            synchronized(this) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = TweetListRepository(
                            database.tweetListDao()
                    )
                }
            }
            return INSTANCE
        }

    }

}

interface Repository<E> : LifecycleObserver {

    suspend fun insert(entity: E)

    suspend fun delete(entity: E)

    suspend fun update(entity: E)

    fun addLifecycle(lifecycle: Lifecycle) = lifecycle.addObserver(this)
    fun removeLifecycle(lifecycle: Lifecycle) = lifecycle.removeObserver(this)

    fun registerLifecycle(lifecycle: Lifecycle) {
        removeLifecycle(lifecycle)
        addLifecycle(lifecycle)
    }

}