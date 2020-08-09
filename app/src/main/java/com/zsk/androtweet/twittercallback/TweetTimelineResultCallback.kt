package com.zsk.androtweet.twittercallback

import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterApiException
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.tweetui.TimelineResult
import com.zsk.androtweet.models.TweetFromDao
import com.zsk.androtweet.utils.extensions.RoomExtensions.asPersistList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.sendBlocking

@ExperimentalCoroutinesApi
class TweetTimelineResultCallback(
        private val callbackScope: ProducerScope<List<TweetFromDao>>
) : Callback<TimelineResult<Tweet>>() {

    override fun success(result: Result<TimelineResult<Tweet>>) =
            callbackScope.sendBlocking(result.data.items.asPersistList())

    override fun failure(exception: TwitterException) {
        exception as TwitterApiException
        exception.printStackTrace()
        callbackScope.cancel(exception.errorMessage, cause = exception)
    }


}