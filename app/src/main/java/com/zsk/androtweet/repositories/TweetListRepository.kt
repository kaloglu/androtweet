package com.zsk.androtweet.repositories

import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.kaloglu.library.ui.interfaces.Repository
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterApiException
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.database.AndroTweetDatabase
import com.zsk.androtweet.models.ListType
import com.zsk.androtweet.models.TweetFromDao
import com.zsk.androtweet.remote.TweetListRemoteMediator
import com.zsk.androtweet.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.coroutines.suspendCoroutine

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class TweetListRepository private constructor(private val db: AndroTweetDatabase) : Repository<List<TweetFromDao>> {

    private val tweetListDao by lazy {
        db.tweetListDao()
    }

    @WorkerThread
    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override suspend fun delete(deleteList: List<TweetFromDao>) {
        tweetListDao.delete(
                deleteList
                        .map { tweet ->

                            suspendCoroutine<TweetFromDao> { continuation ->

                                AndroTweetApp.apiClient.statusesService
                                        .destroy(tweet.id, true)
                                        .enqueue(object : Callback<Tweet>() {
                                            override fun success(result: Result<Tweet>?) {
//                                        tweet.isDeleted = true
                                                continuation.resumeWith(kotlin.Result.success(tweet))

                                            }

                                            override fun failure(exception: TwitterException?) {
                                                continuation.resumeWith(kotlin.Result.failure((exception as TwitterApiException)))
                                            }

                                        })
                            }
                        }
        )
    }

    @WorkerThread
    override suspend fun update(entity: List<TweetFromDao>) = tweetListDao.update(entity)

    @WorkerThread
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Do not use that!")
    override suspend fun insert(entity: List<TweetFromDao>) = tweetListDao.insertAll(entity)

    fun getTweets(userId: Long, listType: ListType = ListType.TWEETS) = Pager(
            config = PagingConfig(
                    Constants.pageSize,
                    prefetchDistance = Constants.prefetchDistance,
                    initialLoadSize = Constants.pageSize
            ),
            remoteMediator = TweetListRemoteMediator(db, userId, listType),
            pagingSourceFactory = { tweetListDao.getTweets(userId = userId) }
    ).flow.distinctUntilChanged()

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
