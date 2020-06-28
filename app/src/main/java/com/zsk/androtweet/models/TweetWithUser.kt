package com.zsk.androtweet.models

import androidx.databinding.Bindable
import androidx.databinding.PropertyChangeRegistry
import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.kaloglu.library.databinding4vm.bindable
import com.kaloglu.library.databinding4vm.interfaces.BindableField
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
) : RecyclerItem, BindableField {

    @Transient
    override var mCallbacks: PropertyChangeRegistry? = null

    @get:Bindable
    @delegate:Ignore
    var isSelected by bindable(false)

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