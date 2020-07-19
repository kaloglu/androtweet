package com.zsk.androtweet.repositories

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.tweetui.TimelineCursor
import com.zsk.androtweet.models.SelectableTweet
import com.zsk.androtweet.utils.twitter.Resource

class TweetTimelineResultCallback(
        private val callback: ItemKeyedDataSource.LoadCallback<SelectableTweet>?,
        private val resultLiveData: MutableLiveData<Resource<List<SelectableTweet>>>
) : Callback<List<Tweet>>() {
    @Suppress("UNCHECKED_CAST")
    override fun success(result: Result<List<Tweet>>) {
        val items = result.data
        var minPosition: Long? = null
        var maxPosition: Long? = null

        if (items.isNotEmpty()) {
            minPosition = (items[items.size - 1]).id
            maxPosition = (items[0]).id
        }
        val timelineCursor = TimelineCursor(minPosition, maxPosition)

        val selectableTweets = items.map {
            SelectableTweet(it, timelineCursor)
        }
        callback?.onResult(selectableTweets)
                ?: resultLiveData.postValue(Resource.success(selectableTweets))
    }

    override fun failure(exception: TwitterException) {
        exception.printStackTrace()
        throw exception
    }
}