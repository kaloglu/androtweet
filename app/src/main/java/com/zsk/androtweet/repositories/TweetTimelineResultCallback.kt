package com.zsk.androtweet.repositories

import androidx.paging.ItemKeyedDataSource
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.tweetui.TimelineResult
import com.zsk.androtweet.models.SelectableTweet

class TweetTimelineResultCallback(private val callback: ItemKeyedDataSource.LoadCallback<SelectableTweet>?) : Callback<TimelineResult<Tweet>>() {
    @Suppress("UNCHECKED_CAST")
    override fun success(result: Result<TimelineResult<Tweet>>) {
        callback?.onResult(
                result.data.items.map {
                    SelectableTweet(it, result.data.timelineCursor)
                }
        )
    }

    override fun failure(exception: TwitterException) {
        exception.printStackTrace()
        throw exception
    }
}