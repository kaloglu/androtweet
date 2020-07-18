package com.zsk.androtweet.utils.extensions

import com.twitter.sdk.android.tweetui.TimelineCursor
import com.zsk.androtweet.models.SelectableTweet
import com.zsk.androtweet.models.User
import com.twitter.sdk.android.core.models.Tweet as SdkTweet
import com.twitter.sdk.android.core.models.User as SdkUser

object RoomExtensions {
    fun SdkTweet.asRoomModel(timelineCursor: TimelineCursor? = null) = SelectableTweet(this, timelineCursor)

    fun List<SdkTweet>?.asRoomModel(timelineCursor: TimelineCursor?) = this?.map {
        it.asRoomModel(timelineCursor)
    } ?: listOf()

    fun SdkUser.asRoomModel(token: String? = "", secret: String? = "") = User(this, token, secret)
}