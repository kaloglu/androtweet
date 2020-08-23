package com.zsk.androtweet.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zsk.androtweet.utils.Converters

@Entity(tableName = "tweet_cursor")
data class TweetCursor(
        @PrimaryKey
        val type: ListType = Converters.stringToListType(),
        val maxPosition: Long?,
        val minPosition: Long?
)

enum class ListType {
    TWEETS,
    RETWEETS,
    FAVOURITES,
    MENTIONS,
    DIRECT_MESSAGES
}
