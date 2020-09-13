package com.zsk.androtweet.utils.extensions

import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.User
import com.zsk.androtweet.models.TweetFromDao
import com.zsk.androtweet.models.UserFromDao
import com.zsk.androtweet.utils.ContextProviders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

object RoomExtensions {

    @ExperimentalCoroutinesApi
    fun CoroutineScope.onUndispatchedIO(contextProviders: ContextProviders = ContextProviders.instance, function: suspend CoroutineScope.() -> Unit) {
        launch(contextProviders.IO, CoroutineStart.UNDISPATCHED) {
            function()
        }
    }

    @ExperimentalCoroutinesApi
    fun CoroutineScope.onIO(contextProviders: ContextProviders = ContextProviders.instance, function: suspend CoroutineScope.() -> Unit) {
        launch(contextProviders.IO) {
            function()
        }
    }

    fun User.asPersistModel(token: String? = "", secret: String? = "") = UserFromDao(
            token = token,
            secret = secret,
            id = id,
            idStr = idStr,
            screenName = screenName
    )

    @JvmStatic
    fun List<Tweet>.asPersistList() = mapTo(mutableListOf()) {
        it.asPersistModel()
    }.toList()

    @JvmStatic
    fun Tweet.asPersistModel() = TweetFromDao(
            id = idStr,
            userId = user.id,
            text = text,
            favoriteCount = favoriteCount,
            favorited = favorited,
            retweetCount = retweetCount,
            retweeted = retweeted,
            inReplyToScreenName = inReplyToScreenName,
            inReplyToStatusId = inReplyToStatusId,
            inReplyToStatusIdStr = inReplyToStatusIdStr,
            inReplyToUserId = inReplyToUserId,
            inReplyToUserIdStr = inReplyToUserIdStr,
            lang = lang,
            quotedStatusIdStr = quotedStatusIdStr,
            source = source,
            createdAt = createdAt.toTimeStamp()
    )
}