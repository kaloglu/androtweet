package com.zsk.androtweet.utils.extensions

import com.zsk.androtweet.models.User
import com.zsk.androtweet.utils.ContextProviders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import com.twitter.sdk.android.core.models.User as SdkUser

@ExperimentalCoroutinesApi
object RoomExtensions {
    fun SdkUser.asRoomModel(token: String? = "", secret: String? = "") = User(this, token, secret)

    fun CoroutineScope.onUndispatchedIO(contextProviders: ContextProviders, function: suspend () -> Unit) {
        launch(contextProviders.IO, CoroutineStart.UNDISPATCHED) {
            function()
        }
    }

}