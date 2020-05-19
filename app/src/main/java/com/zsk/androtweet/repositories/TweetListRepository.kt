package com.zsk.androtweet.repositories

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.kaloglu.library.ui.interfaces.Repository
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterApiClient
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.services.StatusesService
import com.twitter.sdk.android.tweetui.TimelineResult
import com.twitter.sdk.android.tweetui.UserTimeline
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.database.AndroTweetDatabase
import com.zsk.androtweet.database.dao.TweetListDao
import com.zsk.androtweet.models.Tweet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import com.twitter.sdk.android.core.models.Tweet as SdkTweet

@ExperimentalCoroutinesApi
class TweetListRepository private constructor(
        private val tweetListDao: TweetListDao,
        private val statusesService: StatusesService
) : Repository<Tweet> {

    override val data: MutableLiveData<List<Tweet>> = MutableLiveData()

    @Deprecated("You cannot use this method for the TweetListRepository", level = DeprecationLevel.HIDDEN)
    override fun delete(entity: Tweet) = Unit

    @Deprecated("You cannot use this method for the TweetListRepository", level = DeprecationLevel.HIDDEN)
    override fun insert(entity: Tweet) = Unit

    @Deprecated("You cannot use this method for the TweetListRepository", level = DeprecationLevel.HIDDEN)
    override fun update(entity: Tweet) = Unit

    fun getDUC(userId: Long, count: Int) = get(userId, count).distinctUntilChanged()

    @WorkerThread
    internal suspend fun insertAll(list: List<Tweet>) {
        tweetListDao.insert(list)
    }

    @WorkerThread
    fun delete(list: List<Tweet>) {
        GlobalScope.launch(Dispatchers.IO) {
            tweetListDao.delete(list)
        }
    }

    @Throws(TwitterException::class)
    fun <R : TimelineResult<SdkTweet>> fetchData(userId: Long?, count: Int = 2): Flow<R> {
        val fetch: MutableLiveData<R> = MutableLiveData()
        val userTimeline = UserTimeline.Builder()
                .userId(userId)
                .includeReplies(false)
                .includeRetweets(false)
                .maxItemsPerRequest(count)
                .build()

        userTimeline.next(null, object : Callback<TimelineResult<SdkTweet>>() {
            @Suppress("UNCHECKED_CAST")
            override fun success(result: Result<TimelineResult<SdkTweet>>) {
                fetch.postValue(result.data as R)
            }

            override fun failure(exception: TwitterException) {
//                fetch.postValue(TwitterResponse.create(exception))
                Log.e("statusesService", exception.localizedMessage ?: "HATA")
                exception.printStackTrace()
                throw exception
            }

        })
        return fetch.asFlow()
    }

    fun get(userId: Long, count: Int, sinceId: Long? = null) = tweetListDao.get(userId, count)

    companion object {
        @Volatile
        private lateinit var INSTANCE: TweetListRepository

        fun getInstance(
                database: AndroTweetDatabase = AndroTweetApp.database,
                apiClient: TwitterApiClient = AndroTweetApp.apiClient
        ): TweetListRepository {
            synchronized(this) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = TweetListRepository(
                            database.tweetListDao(),
                            apiClient.statusesService
                    )
                }
            }
            return INSTANCE
        }

    }
}
