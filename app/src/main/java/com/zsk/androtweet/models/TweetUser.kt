package com.zsk.androtweet.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity
data class TweetWithUser(
        @Embedded
        val tweet: Tweet,
        @Relation(
                parentColumn = "userId",
                entityColumn = "id"
        )
        val user: User
)