package com.zsk.androtweet.twittercallback

import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterApiException
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import com.zsk.androtweet.models.ListType
import com.zsk.androtweet.models.TweetCursor
import com.zsk.androtweet.models.TweetFromDao
import com.zsk.androtweet.utils.extensions.RoomExtensions.asPersistList
import retrofit2.Response
import kotlin.coroutines.Continuation
import kotlin.coroutines.resumeWithException

data class TimelineResult(
        val cursor: TweetCursor,
        val items: List<TweetFromDao>,
        val response: Response<*>
)

internal class TweetsCallback(private val continuation: Continuation<TimelineResult>, private val listType: ListType) : Callback<List<Tweet>>() {
    override fun success(result: Result<List<Tweet>>) {
        continuation.resumeWith(
                kotlin.Result.success(result.mapTo(listType))
        )
    }

    override fun failure(exception: TwitterException) {
        exception as TwitterApiException // why cancelled???
        exception.printStackTrace()
        continuation.resumeWithException(exception)
    }

}

private fun Result<List<Tweet>>.mapTo(listType: ListType) = TimelineResult(
        TweetCursor(
                type = listType,
                maxPosition = data.lastOrNull()?.idStr,
                minPosition = data.firstOrNull()?.idStr
        ),
        data.asPersistList(),
        response
)
