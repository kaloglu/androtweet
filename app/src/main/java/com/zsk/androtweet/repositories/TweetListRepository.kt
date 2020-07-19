package com.zsk.androtweet.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.kaloglu.library.ktx.isNotNullOrEmpty
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
import com.zsk.androtweet.utils.twitter.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

typealias resultType = Pair<List<SelectableTweet>?, String?>

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class TweetListRepository : PagedList.BoundaryCallback<SelectableTweet>(), Repository<List<SelectableTweet>> {
    private val _resultLiveData: MutableLiveData<Resource<List<SelectableTweet>>> = MutableLiveData()
    val resultLiveData: LiveData<Resource<List<SelectableTweet>>>
        get() = _resultLiveData

    private var userId: Long? = null
    /* val userList = LivePagedListBuilder(DataSource, Constants.pageSize)
             .setBoundaryCallback(this).build()*/

    @WorkerThread
    override suspend fun delete(entity: List<SelectableTweet>) = Unit

    @WorkerThread
    override suspend fun update(entity: List<SelectableTweet>) = Unit

    @WorkerThread
    override suspend fun insert(entity: List<SelectableTweet>) = Unit

    override fun onItemAtEndLoaded(itemAtEnd: SelectableTweet) =
            getUserTimeline(maxPosition = itemAtEnd.timelineCursor.minPosition)

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

    fun getUserTimeline(
            minPosition: Long? = null,
            maxPosition: Long? = null,
            pageSize: Int = Constants.initialLoadSizeHint
    ) {

        if (userId == null) return

        object : BoundaryCallback<SelectableTweet, resultType>() {

            override fun createCall(itemAtEnd: SelectableTweet?): LiveData<resultType> {
                val resultLiveData: MutableLiveData<resultType> = MutableLiveData()
                getUserTimeline(itemAtEnd).enqueue(TweetListCallback(resultLiveData))
                return resultLiveData
            }

            inner class TweetListCallback(private val resultLiveData: MutableLiveData<resultType>) : Callback<List<Tweet>>() {
                @Suppress("UNCHECKED_CAST")
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
                    resultLiveData.postValue(resultType(selectableTweets, null))
                }

                override fun failure(exception: TwitterException) {
                    exception.printStackTrace()
                    resultLiveData.postValue(resultType(null, (exception as TwitterApiException).errorMessage))
                }
            }

            private fun getUserTimeline(itemAtEnd: SelectableTweet?) =
                    AndroTweetApp.apiClient.statusesService
                            .userTimeline(userId, null, pageSize, itemAtEnd?.timelineCursor?.minPosition, maxPosition, true, false, false, false)

            override fun isSuccessful(response: resultType) = response.first.isNotNullOrEmpty()


        }

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
