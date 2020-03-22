package com.zsk.androtweet.interfaces.twittercallback

import com.twitter.sdk.android.core.TwitterException
import com.zsk.androtweet.models.User

interface LoginCallback {
    fun login(user: User)
    fun failure(exception: TwitterException?)
}
