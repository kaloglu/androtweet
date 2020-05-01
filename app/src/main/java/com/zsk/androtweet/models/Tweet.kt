package com.zsk.androtweet.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import com.kaloglu.library.ui.BaseModel
import com.kaloglu.library.ui.RecyclerItem
import com.twitter.sdk.android.tweetui.TimelineCursor
import com.twitter.sdk.android.core.models.Tweet as SdkTweet

@Suppress("CovariantEquals")
@Entity(
        tableName = "tweets",
        primaryKeys = ["tweet_id"],
        foreignKeys = [
            ForeignKey(
                    entity = User::class,
                    parentColumns = ["user_id"],
                    childColumns = ["tweet_user_id"],
                    onDelete = CASCADE
            )
        ]
)

class Tweet(
        var cachedAt: Long? = null,
        @ColumnInfo(name = "tweet_id")
        var id: Long = 0,
        @ColumnInfo(name = "tweet_id_str")
        var idStr: String? = null,
        @ColumnInfo(name = "tweet_user_id")
        var userId: Long? = null,
        var text: String? = null,
        var favoriteCount: Int? = null,
        var favorited: Boolean = false,
        var retweetCount: Int = 0,
        var retweeted: Boolean = false,
        var createdAt: String? = null,
        var maxPosition: Long = 0,
        var minPosition: Long = 0,
        var filterLevel: String? = null,
        var inReplyToScreenName: String? = null,
        var inReplyToStatusId: Long = 0,
        var inReplyToStatusIdStr: String? = null,
        var inReplyToUserId: Long = 0,
        var inReplyToUserIdStr: String? = null,
        var lang: String? = null,
        var possiblySensitive: Boolean = false,
        var quotedStatusIdStr: String? = null,
        var source: String? = null,
        var truncated: Boolean = false,
        var withheldCopyright: Boolean = false,
        var withheldScope: String? = null,
        var isSelected: Boolean = false
) : BaseModel, RecyclerItem {
    constructor(data: SdkTweet, timelineCursor: TimelineCursor?) : this(
            id = data.id,
            idStr = data.idStr,
            userId = data.user.id,
            text = data.text,
            favoriteCount = data.favoriteCount,
            favorited = data.favorited,
            retweetCount = data.retweetCount,
            retweeted = data.retweeted,
            createdAt = data.createdAt,
            maxPosition = timelineCursor?.maxPosition ?: 0,
            minPosition = timelineCursor?.minPosition ?: 0,
            filterLevel = data.filterLevel,
            inReplyToScreenName = data.inReplyToScreenName,
            inReplyToStatusId = data.inReplyToStatusId,
            inReplyToStatusIdStr = data.inReplyToStatusIdStr,
            inReplyToUserId = data.inReplyToUserId,
            inReplyToUserIdStr = data.inReplyToUserIdStr,
            lang = data.lang,
            possiblySensitive = data.possiblySensitive,
            quotedStatusIdStr = data.quotedStatusIdStr,
            source = data.source,
            truncated = data.truncated,
            withheldCopyright = data.withheldCopyright,
            withheldScope = data.withheldScope,
            cachedAt = System.currentTimeMillis()
    )

    //region BaseModel
    override fun <T : BaseModel> equals(obj2: T) = false

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getID() = id as T

    //endregion

    //region Recycler Item
    override var layoutId: Int = -1

    //endregion
}
