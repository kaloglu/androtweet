package com.zsk.androtweet.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.kaloglu.library.ui.interfaces.Repository
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.database.AndroTweetDatabase
import com.zsk.androtweet.database.dao.TweetListDao
import com.zsk.androtweet.models.Tweet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged

@ExperimentalCoroutinesApi
class TweetListRepository private constructor(private val tweetListDao: TweetListDao) : Repository<Tweet> {

    override val data: MutableLiveData<List<Tweet>> = MutableLiveData()

    @WorkerThread
    internal suspend fun insertAll(list: List<Tweet>) {
        tweetListDao.insert(list)
    }

    @WorkerThread
    suspend fun deleteAll(list: List<Tweet>) {
        tweetListDao.delete(list)
    }

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

    @Deprecated("You cannot use this method for the TweetListRepository", level = DeprecationLevel.HIDDEN)
    override fun delete(entity: Tweet) = Unit

    @Deprecated("You cannot use this method for the TweetListRepository", level = DeprecationLevel.HIDDEN)
    override fun insert(entity: Tweet) = Unit

    @Deprecated("You cannot use this method for the TweetListRepository", level = DeprecationLevel.HIDDEN)
    override fun update(entity: Tweet) = Unit

}
