package com.zsk.androtweet.mvi

import com.kaloglu.library.ui.models.ErrorModel
import com.kaloglu.library.ui.viewmodel.mvi.Event
import com.kaloglu.library.ui.viewmodel.mvi.State
import com.zsk.androtweet.models.Tweet

sealed class TweetListState : State {
    object Empty : TweetListState(), State.Empty
    object Success : TweetListState(), State.Success
    data class Error(val message: String) : TweetListState(), State.Error {
        override val error: ErrorModel
            get() = ErrorModel(message = message)
    }

    object Loading : TweetListState(), State.Loading

}

sealed class TweetListEvent : Event {
    data class ShowTweetList(val data: List<Tweet>) : TweetListEvent(), Event.Done
    object EmptyList : TweetListEvent(), Event.Done
    object GetTweetList : TweetListEvent(), Event.Custom
    data class FetchRemoteData(val userId: Long) : TweetListEvent(), Event.Custom
    data class ShowError(val message: String) : TweetListEvent(), Event.Custom
    data class ShowLoading(val data: List<Tweet>?) : TweetListEvent(), Event.Custom
}
