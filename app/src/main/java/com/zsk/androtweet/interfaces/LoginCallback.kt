package com.zsk.androtweet.interfaces

import com.twitter.sdk.android.core.TwitterException
import com.zsk.androtweet.models.UserFromDao

interface LoginCallback {
    fun login(user: UserFromDao)
    fun failure(exception: TwitterException?)
}
