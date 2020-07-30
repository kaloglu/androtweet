package com.zsk.androtweet.mvi

import com.kaloglu.library.viewmodel.mvi.Event
import com.kaloglu.library.viewmodel.mvi.State
import com.zsk.androtweet.models.SelectableTweet

sealed class TweetListState : State {
    object Init : TweetListState(), State.Init

    class SelectedItem(val isSelected: Boolean) : TweetListState(), State.Custom
}

sealed class TweetListEvent : Event {
    object Init : TweetListEvent()
    object ToggleSelectAllItem : TweetListEvent()

    data class GetTweetList(val userId: Long? = null) : TweetListEvent()
    data class ShowError(val message: String) : TweetListEvent()
    data class ShowLoading(val data: List<SelectableTweet>? = listOf()) : TweetListEvent()
    data class ToggleSelectItem(val item: SelectableTweet) : TweetListEvent()
    data class UpdateList(val tweetList: List<SelectableTweet> = emptyList()) : TweetListEvent()

}
