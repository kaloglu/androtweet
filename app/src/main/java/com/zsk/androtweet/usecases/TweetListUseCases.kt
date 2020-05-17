package com.zsk.androtweet.usecases

import androidx.lifecycle.Lifecycle
import com.kaloglu.library.ktx.isToday
import com.twitter.sdk.android.tweetui.TimelineResult
import com.zsk.androtweet.models.TweetWithUser
import com.zsk.androtweet.repositories.TweetListRepository
import com.zsk.androtweet.usecases.base.UseCase
import com.zsk.androtweet.utils.asRoomModel
import com.zsk.androtweet.utils.twitter.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import kotlin.properties.Delegates
import com.twitter.sdk.android.core.models.Tweet as SdkTweet

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class GetTweetList(private val repository: TweetListRepository = TweetListRepository.getInstance())
    : UseCase<List<TweetWithUser>, TimelineResult<SdkTweet>, GetTweetList.Request>() {
    override var request: Request = Request()

    fun execute(userId: Long) {
        request.userId = userId
        return execute()
    }

    override fun registerLifecycle(lifecycle: Lifecycle) {
        super.registerLifecycle(lifecycle)
        repository.registerLifecycle(lifecycle)
    }

    override suspend fun saveCallResult(result: TimelineResult<SdkTweet>) =
            repository.insertAll(result.items.asRoomModel(result.timelineCursor))

    override fun createCall() =
            repository.fetchData<TimelineResult<SdkTweet>>(request.userId, request.count)

    override fun shouldFetch(data: List<TweetWithUser>) =
            data.isEmpty() || Date(data.first().tweet.cachedAt ?: -1).isToday().not()

    override fun loadFromDb() = repository.getDUC(request.userId, request.count)//.asLiveData()

    fun onEach(action: suspend (Resource<List<TweetWithUser>>) -> Unit) =
            this.flow.onEach(action).launchIn(coroutineScope)

    data class Request(var count: Int = 10) : UseCase.Request {
        var userId by Delegates.notNull<Long>()
    }

}
