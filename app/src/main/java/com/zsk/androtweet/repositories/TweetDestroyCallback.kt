package com.zsk.androtweet.repositories

import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterApiException
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import com.zsk.androtweet.models.SelectableTweet

class TweetDestroyCallback(private val selectableTweet: SelectableTweet) : Callback<Tweet>() {
    override fun success(result: Result<Tweet>?) {
        selectableTweet.isDeleted = true
        selectableTweet.result = "Deletion is succeed!"
    }

    override fun failure(exception: TwitterException?) {
        selectableTweet.isDeleted = false
        selectableTweet.isSelected = false
        selectableTweet.result = (exception as TwitterApiException).errorMessage
    }

}