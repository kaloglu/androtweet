package com.zsk.androtweet.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.kaloglu.library.ktx.currentTimestamp
import com.kaloglu.library.ktx.toDate
import com.kaloglu.library.ui.BaseModel
import com.twitter.sdk.android.tweetui.TimelineCursor
import com.zsk.androtweet.utils.Constants
import java.util.*
import com.twitter.sdk.android.core.models.Tweet as SdkTweet

@Suppress("CovariantEquals")
@Entity(
        tableName = "tweets",
        primaryKeys = ["tweet_id"]
)
data class Tweet(
        @Ignore
        var isSelected: Boolean = false,
        @ColumnInfo(name = "tweet_id")
        var id: Long = 0,
        @ColumnInfo(name = "tweet_id_str")
        var idStr: String? = null,
        @ColumnInfo(name = "tweet_user_id")
        var userId: Long? = null,
        var text: String? = "test",
        var favoriteCount: Int? = null,
        var favorited: Boolean = false,
        var retweetCount: Int = 0,
        var retweeted: Boolean = false,
        var inReplyToScreenName: String? = null,
        var inReplyToStatusId: Long = 0,
        var inReplyToStatusIdStr: String? = null,
        var inReplyToUserId: Long = 0,
        var inReplyToUserIdStr: String? = null,
        var quotedStatusIdStr: String? = null,
        var lang: String? = null,
        var maxPosition: Long? = -1,
        var minPosition: Long? = -1,
        var source: String? = null,
        var createdAt: Date = currentTimestamp().toDate(),
        var cachedAt: Date = currentTimestamp().toDate()
) : BaseModel {

    constructor(data: SdkTweet, timelineCursor: TimelineCursor?) : this(
            id = data.id,
            idStr = data.idStr,
            userId = data.user.id,
            text = data.text,
            favoriteCount = data.favoriteCount,
            favorited = data.favorited,
            retweetCount = data.retweetCount,
            retweeted = data.retweeted,
            maxPosition = timelineCursor?.maxPosition,
            minPosition = timelineCursor?.minPosition,
            inReplyToScreenName = data.inReplyToScreenName,
            inReplyToStatusId = data.inReplyToStatusId,
            inReplyToStatusIdStr = data.inReplyToStatusIdStr,
            inReplyToUserId = data.inReplyToUserId,
            inReplyToUserIdStr = data.inReplyToUserIdStr,
            lang = data.lang,
            quotedStatusIdStr = data.quotedStatusIdStr,
            source = data.source,
            createdAt = data.createdAt.toDate(Constants.TWEET_DATE_PATTERN)
    )

    //region BaseModel
    override fun <T : BaseModel> equals(obj2: T) = false

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getID() = id as T

    //endregion

}
