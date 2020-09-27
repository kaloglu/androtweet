package com.zsk.androtweet.mvi

import com.kaloglu.library.ui.models.ErrorModel
import com.kaloglu.library.viewmodel.mvi.Event
import com.kaloglu.library.viewmodel.mvi.State
import com.zsk.androtweet.utils.Constants

sealed class TweetListState : State {
    object Idle : TweetListState(), State.Idle
    object Init : TweetListState(), State.Init
    object NeedLogin : TweetListState(), State.Failure {
        override val error: ErrorModel by lazy { Constants.LoginError }
    }

    data class Loading(override val loading: Boolean = true) : TweetListState(), State.Loading

    object UpdateUI : TweetListState(), State.Done

    class SelectedItem(val isSelected: Boolean) : TweetListState(), State.Custom
    class Failure(override val error: ErrorModel) : TweetListState(), State.Failure {
        constructor(errorMessage: String) : this(ErrorModel(errorMessage))
    }
}

sealed class TweetListEvent : Event {
    object Idle : TweetListEvent(), Event.Idle
    object Init : TweetListEvent()
//    object RefreshTweetList : TweetListEvent()
//    object GetTweetList : TweetListEvent()

//    data class ShowError(val message: String) : TweetListEvent()
//    data class ToggleSelectItem(val item: TweetFromDao) : TweetListEvent()
//    data class ToggleSelectAllItem(val selectAll: Boolean = true) : TweetListEvent()

}
