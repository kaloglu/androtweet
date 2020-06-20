package com.zsk.androtweet.usecases

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.kaloglu.library.ktx.isToday
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.tweetui.TimelineResult
import com.twitter.sdk.android.tweetui.UserTimeline
import com.zsk.androtweet.models.TweetWithUser
import com.zsk.androtweet.repositories.TweetListRepository
import com.zsk.androtweet.usecases.base.UseCase
import com.zsk.androtweet.utils.RoomExtensions.asRoomModel
import com.zsk.androtweet.utils.twitter.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import kotlin.properties.Delegates
import com.twitter.sdk.android.core.models.Tweet as SdkTweet

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class GetTweetList(
        private val repository: TweetListRepository = TweetListRepository.getInstance()
)
    : UseCase<List<TweetWithUser>, TimelineResult<SdkTweet>, GetTweetList.Request>() {

    override var request: Request = Request()

    override fun registerLifecycle(lifecycle: Lifecycle) {
        super.registerLifecycle(lifecycle)
        repository.registerLifecycle(lifecycle)
    }

    override suspend fun saveCallResult(result: TimelineResult<SdkTweet>) =
            repository.insert(result.items.asRoomModel(result.timelineCursor))

    override fun createCall() = fetchData<TimelineResult<SdkTweet>>(request.userId, request.count)

    override fun shouldFetch(data: List<TweetWithUser>) = data.isEmpty() || data.first().tweet.cachedAt.isToday().not()

    override fun loadFromDb() = repository.get(request.userId, request.count).cancellable()//.asLiveData()

    fun execute(userId: Long) {
        request.userId = userId
        return execute()
    }

    fun onEach(action: suspend (Resource<List<TweetWithUser>>) -> Unit) =
            this.flow.onEach(action).launchIn(coroutineScope)

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
                Log.e("userTimeline services", exception.localizedMessage ?: "HATA")
                exception.printStackTrace()
                throw exception
            }

        })
        return fetch.asFlow()
    }

    data class Request(var count: Int = 10) : UseCase.Request {
        var userId by Delegates.notNull<Long>()
    }

}
