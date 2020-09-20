package com.zsk.androtweet.twittercallback

import com.twitter.sdk.android.core.*
import com.zsk.androtweet.interfaces.LoginCallback
import com.zsk.androtweet.utils.extensions.RoomExtensions.asPersistModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class TwitterSessionCallback(private val loginCallback: LoginCallback) : Callback<TwitterSession>() {
    override fun failure(exception: TwitterException) {
        loginCallback.failure(exception)
    }

    override fun success(result: Result<TwitterSession>) {
        val authToken = result.data.authToken
        val twitterApiClient = TwitterCore.getInstance().apiClient
        twitterApiClient.accountService
                .verifyCredentials(false, true, true)
                .enqueue(object : Callback<com.twitter.sdk.android.core.models.User>() {
                    override fun success(result: Result<com.twitter.sdk.android.core.models.User>?) {
                        result?.data?.let {
                            loginCallback.login(it.asPersistModel(authToken.token, authToken.secret))
                        }
                    }

                    override fun failure(exception: TwitterException?) {
                        loginCallback.failure(exception)
                    }
                })
    }
}
