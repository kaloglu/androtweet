package com.zsk.androtweet.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.twitter.sdk.android.tweetui.UserTimeline
import com.zsk.androtweet.database.AndroTweetDatabase
import com.zsk.androtweet.models.TweetFromDao
import com.zsk.androtweet.twittercallback.TweetTimelineResultCallback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import retrofit2.HttpException
import java.io.IOException

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class TweetListRemoteMediator(
        private val db: AndroTweetDatabase,
        val userId: Long
) : RemoteMediator<Int, TweetFromDao>() {
    private var shouldCall: Boolean = false
    private val tweetListDao by lazy { db.tweetListDao() }
    private val tag = "RemoteMedaitor"
    private val userTimelineBuilder = UserTimeline.Builder().userId(userId)
            .includeReplies(false)
            .includeRetweets(false)

    override suspend fun initialize() = InitializeAction.SKIP_INITIAL_REFRESH

    override suspend fun load(
            loadType: LoadType,
            state: PagingState<Int, TweetFromDao>
    ): MediatorResult {
        Log.i(tag, "Load Key=> $loadType")
        userTimelineBuilder.maxItemsPerRequest(state.config.pageSize)
        return try {
            shouldCall = state.isEmpty()

            if (loadType == LoadType.REFRESH) {
                userTimelineBuilder.maxItemsPerRequest(state.config.initialLoadSize)
                shouldCall = true
            }

            if (shouldCall) callNetwork()

            return MediatorResult.Success(endOfPaginationReached = state.isEmpty())

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun callNetwork() {
        callbackFlow<List<TweetFromDao>> {
            userTimelineBuilder.build().next(null, TweetTimelineResultCallback(this))
            awaitClose {
                channel.close()
            }
        }.collectLatest {
            db.withTransaction {
                tweetListDao.insert(it)
            }
        }
    }

}
