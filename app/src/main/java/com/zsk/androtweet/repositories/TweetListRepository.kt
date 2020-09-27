package com.zsk.androtweet.repositories

import androidx.annotation.WorkerThread
import com.kaloglu.library.ktx.isNotNullOrEmpty
import com.kaloglu.library.ui.interfaces.Repository
import com.twitter.sdk.android.core.TwitterSession
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.database.AndroTweetDatabase
import com.zsk.androtweet.models.TweetFromDao
import com.zsk.androtweet.remote.RemoteTweetRepository
import com.zsk.androtweet.utils.Constants
import com.zsk.androtweet.utils.extensions.RoomExtensions.asPersistModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class TweetListRepository private constructor(
        private val db: AndroTweetDatabase,
        private var remote: RemoteTweetRepository = RemoteTweetRepository(db)
) : Repository<List<TweetFromDao>> {

    private val deletedTweets: MutableList<TweetFromDao> = mutableListOf()
    private val currentUser: TwitterSession
        get() = AndroTweetApp.activeSession

    private val tweetListDao by lazy {
        db.tweetListDao()
    }

    @WorkerThread
    suspend fun deleteWithReturn(deleteList: List<TweetFromDao>): List<TweetFromDao> {
        delete(deleteList)
        return deletedTweets
    }

    @WorkerThread
    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override suspend fun delete(deleteList: List<TweetFromDao>) {
        deleteList
                .forEach { tweet ->
                    remote.destroy(tweet.id.toLong())
                            ?.asPersistModel()
                            ?.let {
                                deletedTweets.add(it)
                            }
                }.apply {
                    if (deletedTweets.isNotNullOrEmpty())
                        tweetListDao.delete(deletedTweets)
                }
    }

    @WorkerThread
    override suspend fun update(entity: List<TweetFromDao>) = tweetListDao.update(entity)

    @WorkerThread
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Do not use that!")
    override suspend fun insert(entity: List<TweetFromDao>) = tweetListDao.insertAll(entity)

    suspend fun getTweetsRemote(size: Int = Constants.pageSize) = remote.getTweets(size)

    fun getTweets() = tweetListDao.getTweets(currentUser.id)

    suspend fun clearDao() = tweetListDao.deletePersist()

    companion object {

        @Volatile
        private lateinit var INSTANCE: TweetListRepository

        fun getInstance(database: AndroTweetDatabase = AndroTweetApp.database): TweetListRepository {
            synchronized(this) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = TweetListRepository(
                            database
                    )
                }
            }
            return INSTANCE
        }

    }

}
