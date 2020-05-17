package com.zsk.androtweet.models

import androidx.room.Embedded
import androidx.room.Relation

data class TweetWithUser(
        @Embedded
        val tweet: Tweet,
        @Relation(
                parentColumn = "tweet_user_id",
                entityColumn = "user_id"
        )
        val user: User
)