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
import com.twitter.sdk.android.tweetui.TimelineResult
import com.twitter.sdk.android.tweetui.UserTimeline
import com.zsk.androtweet.database.AndroTweetDatabase
import com.zsk.androtweet.models.ListType
import com.zsk.androtweet.models.TweetCursor
import com.zsk.androtweet.models.TweetFromDao
import com.zsk.androtweet.twittercallback.TweetTimelineResultCallback
import com.zsk.androtweet.utils.extensions.RoomExtensions.asPersistList
import com.zsk.androtweet.utils.extensions.RoomExtensions.onIO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import retrofit2.HttpException
import java.io.IOException

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class TweetListRemoteMediator(
        private val db: AndroTweetDatabase,
        userId: Long,
        private val listType: ListType
) : RemoteMediator<Int, TweetFromDao>() {
    private val tag = "RemoteMedaitor"

    private var shouldCall: Boolean = false
    private val tweetListDao by lazy { db.tweetListDao() }
    private val cursorDao by lazy { db.cursorDao() }
    private var cursor: TweetCursor? = null
    private val userTimelineBuilder = UserTimeline.Builder().userId(userId)
            .includeReplies(listType == ListType.MENTIONS)
            .includeRetweets(listType == ListType.RETWEETS)

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

        shouldCall = state.isEmpty()

        when (loadType) {
            LoadType.REFRESH -> {
                userTimelineBuilder.maxItemsPerRequest(state.config.initialLoadSize)
                shouldCall = true
                GlobalScope.onIO {
                    tweetListDao.deletePersist()
                }
            }
            LoadType.PREPEND -> {
                userTimelineBuilder.maxItemsPerRequest(state.config.pageSize)
            }
            LoadType.APPEND -> return MediatorResult.Success(endOfPaginationReached = state.isEmpty())
        }

        return try {
            when {
                shouldCall -> callNetwork(loadType)
            }
            MediatorResult.Success(endOfPaginationReached = false)
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
        val userTimeline = userTimelineBuilder.build()

        if (loadType == LoadType.REFRESH || cursor?.maxPosition == null) {
            callbackFlow<TimelineResult<Tweet>> {
                userTimeline.next(cursor?.minPosition, TweetTimelineResultCallback(this))
                awaitClose {
                    channel.close()
                }
            }.collectLatest {
                updateDB(it)
            }
        } else {
            callbackFlow<TimelineResult<Tweet>> {
                userTimeline.previous(cursor?.maxPosition, TweetTimelineResultCallback(this))
                awaitClose {
                    channel.close()
                }
            }.collectLatest {
                updateDB(it)
            }
        }
    }

    private suspend fun TimelineResult<Tweet>.updateCursors() {
        items.map {
            TweetCursor(
                    type = listType,
                    maxPosition = timelineCursor.maxPosition,
                    minPosition = timelineCursor.minPosition
            )
        }.let { cursors ->
            cursorDao.clearCursors(listType)
            cursorDao.insertAll(cursors)
        }
    }

    private suspend fun updateDB(timelineResult: TimelineResult<Tweet>) {
        db.withTransaction {
            timelineResult.updateCursors()

            tweetListDao.insertAll(timelineResult.items.asPersistList())
        }
    }
}
