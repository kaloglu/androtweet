package com.zsk.androtweet.models

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.kaloglu.library.ui.BaseModel
import com.kaloglu.library.ui.RecyclerItem
import com.zsk.androtweet.R

@Suppress("CovariantEquals")
data class TweetWithUser constructor(
        @Embedded
        val tweet: Tweet,
        @Relation(
                parentColumn = "tweet_user_id",
                entityColumn = "user_id"
        )
        val user: User?
) : RecyclerItem {

    @set:Ignore
    override var layoutId: Int
        get() = LAYOUT_ID
        set(value) {
            LAYOUT_ID = value
        }


    //region BaseModel
    override fun <T : BaseModel> equals(obj2: T) = getID<Long>() == obj2.getID<Long>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getID() = tweet.id as T
    //endregion

    companion object {
        var LAYOUT_ID: Int = R.layout.tweets_layout
    }
}