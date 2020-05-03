package com.zsk.androtweet.utils

import com.twitter.sdk.android.tweetui.TimelineCursor
import com.zsk.androtweet.models.Tweet
import com.zsk.androtweet.models.User
import com.twitter.sdk.android.core.models.Tweet as SdkTweet
import com.twitter.sdk.android.core.models.User as SdkUser

fun SdkTweet.asRoomModel(timelineCursor: TimelineCursor? = null) = Tweet(this, timelineCursor)

fun List<SdkTweet>?.asRoomModel(timelineCursor: TimelineCursor?) = this?.map {
    it.asRoomModel(timelineCursor)
} ?: listOf()

fun SdkUser.asRoomModel(token: String? = "", secret: String? = "") = User(this, token, secret)