package com.zsk.androtweet.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.twitter.sdk.android.core.TwitterApiException
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import com.zsk.androtweet.AndroTweetApp.Companion.apiClient
import com.zsk.androtweet.database.AndroTweetDatabase
import com.zsk.androtweet.models.ListType
import com.zsk.androtweet.models.TweetCursor
import com.zsk.androtweet.models.TweetFromDao
import com.zsk.androtweet.twittercallback.TimelineResult
import com.zsk.androtweet.twittercallback.TweetsCallback
import com.zsk.androtweet.utils.extensions.RoomExtensions.onIO
import com.zsk.androtweet.utils.retrofitadapters.flowRetrofitAdapt
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Call
import retrofit2.HttpException
import java.io.IOException

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class TweetListRemoteMediator(
        private val db: AndroTweetDatabase,
        val userId: Long,
        private val listType: ListType
) : RemoteMediator<Int, TweetFromDao>() {
    private var maxItemsPerRequest: Int? = 25
    private val tag = "RemoteMedaitor"

    private var shouldCall: Boolean = false
    private val tweetListDao by lazy { db.tweetListDao() }
    private val cursorDao by lazy { db.cursorDao() }
    private var cursor: TweetCursor? = null

    init {
        GlobalScope.onIO {
            cursorDao.cursor(listType).collectLatest {
                cursor = it
            }
        }
    }

    override suspend fun initialize() = cursor?.let {
        InitializeAction.SKIP_INITIAL_REFRESH
    } ?: super.initialize()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, TweetFromDao>): MediatorResult {
        Log.i(tag, "Load Key=> $loadType")

        when (loadType) {
            LoadType.REFRESH -> {
                maxItemsPerRequest = state.config.initialLoadSize
                shouldCall = true
                GlobalScope.onIO {
                    tweetListDao.deletePersist()
                }
            }
            LoadType.PREPEND -> {
                shouldCall = state.isEmpty()
                maxItemsPerRequest = state.config.pageSize
            }
            LoadType.APPEND -> {
                shouldCall = state.isEmpty() // add Condition if need call
                maxItemsPerRequest = state.config.pageSize
            }
        }

        return try {
            if (shouldCall) callNetwork(loadType)
            MediatorResult.Success(endOfPaginationReached = state.isEmpty())
        } catch (exception: TwitterException) {
            MediatorResult.Error(exception)
        } catch (exception: TwitterApiException) {
            MediatorResult.Error(exception)
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun callNetwork(loadType: LoadType) {
        flowRetrofitAdapt<TimelineResult>(
                onCall = {
                    val call: Call<*>? = when {
                        loadType == LoadType.REFRESH || cursor?.maxPosition == null -> it.next()
                        else -> it.previous()
                    }
                    it.invokeOnCancellation {
                        call?.cancel()
                    }
                }
        ).collectLatest {
            updateDB(it)
        }
    }

    private fun get(
            minPosition: Long? = null,
            maxPosition: Long? = null,
            callback: TweetsCallback
    ): Call<MutableList<Tweet>>? {
        val call = apiClient.statusesService
                .userTimeline(
                        userId,
                        null,
                        maxItemsPerRequest,
                        minPosition,
                        maxPosition,
                        false,
                        listType != ListType.MENTIONS,
                        null,
                        listType == ListType.RETWEETS
                )
        call.enqueue(callback)
        return call
    }

    private suspend fun updateDB(timelineResult: TimelineResult) {
        db.withTransaction {
            timelineResult.updateCursors()

            tweetListDao.insertAll(timelineResult.items)
        }
    }

    private suspend fun TimelineResult.updateCursors() {
        cursorDao.clearCursors(listType)
        cursorDao.insertAll(cursor)
    }

    private fun CancellableContinuation<TimelineResult>.previous() =
            get(maxPosition = cursor?.maxPosition, callback = TweetsCallback(this, listType))

    private fun CancellableContinuation<TimelineResult>.next() =
            get(minPosition = cursor?.minPosition, callback = TweetsCallback(this, listType))
}
