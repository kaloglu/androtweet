package com.zsk.androtweet.twittercallback

import com.twitter.sdk.android.core.*
import com.zsk.androtweet.interfaces.twittercallback.LoginCallback
import com.zsk.androtweet.models.User
import com.zsk.androtweet.repositories.UserRepository
import com.zsk.androtweet.usecases.CleanUserUseCases
import com.zsk.androtweet.usecases.InsertUserUseCases

class TwitterSessionCallback(
        private val repository: UserRepository = UserRepository(),
        private val loginCallback: LoginCallback
) : Callback<TwitterSession>() {

    override fun failure(exception: TwitterException) {
        cleanUserInfo()
    }

    private fun cleanUserInfo() {
        CleanUserUseCases(repository)
    }

    override fun success(result: Result<TwitterSession>) {
        val authToken = result.data.authToken
        val twitterApiClient = TwitterCore.getInstance().apiClient
        twitterApiClient.accountService
                .verifyCredentials(false, true, true)
                .enqueue(object : Callback<com.twitter.sdk.android.core.models.User>() {
                    override fun success(result: Result<com.twitter.sdk.android.core.models.User>?) {
                        result?.data?.let {
                            val user = User(it)
                            user.token = authToken.token
                            user.secret = authToken.secret
                            InsertUserUseCases(repository)(user)
                        }
                    }

                    override fun failure(exception: TwitterException?) {
                        cleanUserInfo()
                    }

                })

        loginCallback.login()
    }
}

