package com.zsk.androtweet.mvi

import com.kaloglu.library.ui.models.ErrorModel
import com.kaloglu.library.viewmodel.mvi.Event
import com.kaloglu.library.viewmodel.mvi.State
import com.zsk.androtweet.models.SelectableTweet

sealed class TweetListState : State {
    object Empty : TweetListState(), State.Empty
    object Success : TweetListState(), State.Success
    data class Error(val message: String) : TweetListState(), State.Error {
        override val error: ErrorModel
            get() = ErrorModel(message = message)
    }

    class SelectedItem(val isSelected: Boolean) : TweetListState(), State.Custom

    object Loading : TweetListState(), State.Loading

}

sealed class TweetListEvent : Event {
    object GetTweetList : TweetListEvent(), Event.Custom
    object ToggleSelectAllItem : TweetListEvent(), Event.Custom

    data class ShowError(val message: String) : TweetListEvent(), Event.Custom
    data class ShowLoading(val data: List<SelectableTweet>? = listOf()) : TweetListEvent(), Event.Custom
    data class ToggleSelectItem(val item: SelectableTweet) : TweetListEvent(), Event.Custom
}
