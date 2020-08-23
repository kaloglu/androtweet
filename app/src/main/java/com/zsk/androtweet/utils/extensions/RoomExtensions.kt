package com.zsk.androtweet.utils.extensions

import com.kaloglu.library.ktx.toDate
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.User
import com.zsk.androtweet.models.TweetFromDao
import com.zsk.androtweet.models.UserFromDao
import com.zsk.androtweet.utils.Constants
import com.zsk.androtweet.utils.ContextProviders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
object RoomExtensions {
    fun CoroutineScope.onUndispatchedIO(contextProviders: ContextProviders = ContextProviders.instance, function: suspend CoroutineScope.() -> Unit) {
        launch(contextProviders.IO, CoroutineStart.UNDISPATCHED) {
            function()
        }
    }

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
            id = id,
            idStr = idStr,
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
            createdAt = createdAt.toDate(Constants.TWEET_DATE_PATTERN)
    )
}