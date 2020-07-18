package com.zsk.androtweet.usecases
/*

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.paging.PagedList
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.tweetui.TimelineResult
import com.twitter.sdk.android.tweetui.UserTimeline
import com.zsk.androtweet.repositories.TweetListRepository
import com.zsk.androtweet.usecases.base.UseCase
import com.zsk.androtweet.utils.Constants
import com.zsk.androtweet.utils.extensions.RoomExtensions.asRoomModel
import com.zsk.androtweet.utils.twitter.Resource
import com.zsk.androtweet.viewmodels.ItemSourceFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlin.properties.Delegates
import com.twitter.sdk.android.core.models.Tweet

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class GetTweetList(
        private val repository: TweetListRepository = TweetListRepository.getInstance()
) : UseCase<List<Tweet>, TimelineResult<Sdk>, GetTweetList.Request>() {

    val initList = PagedList.Builder<Long, TweetWithUser>(ItemSourceFactory(mutableListOf()).create(), ItemSourceFactory.providePagingConfig())
            .setFetchExecutor(Dispatchers.Main.asExecutor())
            .setNotifyExecutor(Dispatchers.IO.asExecutor())
            .setBoundaryCallback(this)
            .build()

    override var request: Request = Request()

    override fun registerLifecycle(lifecycle: Lifecycle) {
        super.registerLifecycle(lifecycle)
        repository.registerLifecycle(lifecycle)
    }

    override suspend fun saveCallResult(result: TimelineResult<SdkTweet>) =
            repository.insert(result.items.asRoomModel(result.timelineCursor))

    override fun createCall() = fetchData<TimelineResult<SdkTweet>>(request)

    override fun shouldFetch(data: List<TweetWithUser>) = data.isEmpty() || data.first().tweet.cachedAt.isToday().not()

    override fun loadFromDb() = repository.get(request).cancellable()//.asLiveData()

    fun execute(userId: Long) {
        request.userId = userId
        return execute()
    }

    fun onEach(action: suspend (Resource<List<TweetWithUser>>) -> Unit) =
            this.flow.onEach(action).launchIn(coroutineScope)

    @Throws(TwitterException::class)
    fun <R : TimelineResult<SdkTweet>> fetchData(request: Request): Flow<R> {
        val userTimeline = UserTimeline.Builder()
                .userId(request.userId)
                .includeReplies(false)
                .includeRetweets(false)
                .maxItemsPerRequest(request.count)
                .build()
        return getTimeLineTweets(userTimeline, request.minPos, request.maxPos)
    }

    private fun <R : TimelineResult<SdkTweet>> getTimeLineTweets(userTimeline: UserTimeline, minPos: Long? = null, maxPos: Long? = null): Flow<R> {
        val fetch: MutableLiveData<R> = MutableLiveData()

        when {
            maxPos != null -> userTimeline.previous(maxPos, TimelineResultCallback(fetch))
            else -> userTimeline.next(minPos, TimelineResultCallback(fetch))
        }

        return fetch.asFlow()
    }

    data class Request(
            var count: Int = Constants.DEFAULT_TIMELINE_TWEET_COUNT,
            val minPos: Long? = null,
            val maxPos: Long? = null
    ) : UseCase.Request {
        var userId by Delegates.notNull<Long>()

    }

    inner class TimelineResultCallback<R : TimelineResult<SdkTweet>>(private val fetch: MutableLiveData<R>) : Callback<TimelineResult<SdkTweet>>() {
        @Suppress("UNCHECKED_CAST")
        override fun success(result: Result<TimelineResult<SdkTweet>>) = fetch.postValue(result.data as R)

        override fun failure(exception: TwitterException) {
            exception.printStackTrace()
            throw exception
        }

    }

}
*/
