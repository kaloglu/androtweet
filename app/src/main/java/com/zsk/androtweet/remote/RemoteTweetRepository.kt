package com.zsk.androtweet.remote

import android.util.Log
import androidx.room.withTransaction
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.models.Tweet
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.database.AndroTweetDatabase
import com.zsk.androtweet.models.ListType
import com.zsk.androtweet.models.TweetCursor
import com.zsk.androtweet.models.TweetFromDao
import com.zsk.androtweet.utils.Constants
import com.zsk.androtweet.utils.extensions.RoomExtensions.asPersistList
import com.zsk.androtweet.utils.extensions.RoomExtensions.onIO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest

@ExperimentalCoroutinesApi
class RemoteTweetRepository(
        private val db: AndroTweetDatabase
) {
    private val currentUser: TwitterSession
        get() = AndroTweetApp.activeSession

    private val listType: ListType = ListType.TWEETS
    private val tag = this::class.java.simpleName
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

    internal suspend fun getTweets(maxItemsPerRequest: Int = Constants.pageSize, isNext: Boolean = true): Boolean {
        val response = when {
            isNext || cursor?.maxPosition == null -> {
                Log.d(tag, "CALL NEXT")
                getTweets(minPosition = cursor?.minPosition?.toLong(), maxItemsPerRequest = maxItemsPerRequest)
            }
            else -> {
                Log.d(tag, "CALL PREVIOUS")
                getTweets(maxPosition = cursor?.maxPosition?.toLong(), maxItemsPerRequest = maxItemsPerRequest)
            }
        }
        if (response.isSuccessful) {
            Result<List<Tweet>>(response.body(), response).data.run {
                if (isNotEmpty()) {
                    updateDB(
                            TweetCursor(
                                    type = listType,
                                    maxPosition = lastOrNull()?.idStr,
                                    minPosition = firstOrNull()?.idStr
                            ),
                            asPersistList()
                    )
                }
            }
        }

        return true
    }

    private suspend fun updateDB(tweetCursor: TweetCursor, tweetList: List<TweetFromDao>) {
        db.withTransaction {
            if (tweetCursor.maxPosition != null && tweetCursor.minPosition != null) {

                cursorDao.clearCursors(listType)
                cursorDao.insertAll(tweetCursor)

            }
            tweetListDao.insertAll(tweetList)
        }
    }

    private suspend fun getTweets(
            minPosition: Long? = null,
            maxPosition: Long? = null,
            maxItemsPerRequest: Int
    ) = AndroTweetApp.apiClient
            .getCustomStatusesService()
            .userTimeline(
                    currentUser.userId,
                    null,
                    maxItemsPerRequest,
                    minPosition,
                    maxPosition,
                    false,
                    listType != ListType.MENTIONS,
                    null,
                    listType == ListType.RETWEETS
            )

}
