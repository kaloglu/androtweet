package com.zsk.androtweet.repositories

import androidx.annotation.WorkerThread
import androidx.paging.PagedList
import com.kaloglu.library.ui.interfaces.Repository
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterApiException
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.tweetui.TimelineCursor
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.models.SelectableTweet
import com.zsk.androtweet.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

typealias resultType = Pair<List<SelectableTweet>?, String?>

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class TweetListRepository : PagedList.BoundaryCallback<SelectableTweet>(), Repository<List<SelectableTweet>> {

    val tweetFlow = MutableStateFlow(resultType(emptyList(), null))
    private var userId: Long? = null

    @WorkerThread
    override suspend fun delete(entity: List<SelectableTweet>) = Unit

    @WorkerThread
    override suspend fun update(entity: List<SelectableTweet>) = Unit

    @WorkerThread
    override suspend fun insert(entity: List<SelectableTweet>) = Unit

    override fun onItemAtEndLoaded(itemAtEnd: SelectableTweet) =
            getUserTimeline(minPosition = itemAtEnd.timelineCursor.minPosition)

    override fun onItemAtFrontLoaded(itemAtFront: SelectableTweet) =
            getUserTimeline(maxPosition = itemAtFront.timelineCursor.maxPosition)

    fun destroyTweets(selectedTweetList: List<SelectableTweet>) {
        selectedTweetList
//                .filter { it.result.isEmpty() }
                .forEach {
                    AndroTweetApp.apiClient.statusesService
                            .destroy(it.tweet.id, true)
                            .enqueue(TweetDestroyCallback(it))
                }
    }

    fun getUserTimeline(minPosition: Long? = null,
                        maxPosition: Long? = null,
                        pageSize: Int = Constants.initialLoadSizeHint) {
        if (userId == null) return

        AndroTweetApp.apiClient.statusesService
                .userTimeline(
                        userId,
                        null,
                        pageSize,
                        minPosition,
                        maxPosition,
                        true,
                        false,
                        false,
                        false
                ).enqueue(object : Callback<List<Tweet>>() {

                    override fun success(result: Result<List<Tweet>>) {
                        val items = result.data
                        var minPos: Long? = null
                        var maxPos: Long? = null

                        if (items.isNotEmpty()) {
                            minPos = (items[items.size - 1]).id
                            maxPos = (items[0]).id
                        }
                        val timelineCursor = TimelineCursor(minPos, maxPos)

                        val selectableTweets = items.map {
                            SelectableTweet(it, timelineCursor)
                        }
                        tweetFlow.value = resultType(selectableTweets, null)
                    }

                    override fun failure(exception: TwitterException) {
                        exception.printStackTrace()
                        tweetFlow.value = resultType(null, (exception as TwitterApiException).errorMessage)
                    }
                })
    }

    fun initialList(userId: Long?) {
        userId?.let {
            this.userId = it
        }
        getUserTimeline()
    }


    companion object {

        @Volatile
        private lateinit var INSTANCE: TweetListRepository

        fun getInstance(): TweetListRepository {
            synchronized(this) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = TweetListRepository()
                }
            }
            return INSTANCE
        }

    }

}
