package com.zsk.androtweet.models

import androidx.databinding.Bindable
import androidx.databinding.PropertyChangeRegistry
import com.kaloglu.library.databinding4vm.bindable
import com.kaloglu.library.databinding4vm.interfaces.BindableField
import com.kaloglu.library.ui.BaseModel
import com.kaloglu.library.ui.RecyclerItem
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.tweetui.TimelineCursor
import com.zsk.androtweet.R

@Suppress("CovariantEquals")
data class SelectableTweet(
        val tweet: Tweet,
        val timelineCursor: TimelineCursor?
) : RecyclerItem, BindableField {

    @Transient
    override var mCallbacks: PropertyChangeRegistry? = null

    @get:Bindable
    var isSelected by bindable(false)

    @get:Bindable
    var isDeleted by bindable(false)

    @get:Bindable
    var result by bindable("")

    override var layoutId: Int
        get() = LAYOUT_ID
        set(value) {
            LAYOUT_ID = value
        }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun <T : BaseModel> equals(tweet: T) = getID<Long>() == tweet.getID<Long>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getID() = tweet.id as T

    companion object {
        var LAYOUT_ID = R.layout.tweets_layout
    }
}
