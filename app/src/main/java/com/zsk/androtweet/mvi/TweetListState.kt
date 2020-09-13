package com.zsk.androtweet.mvi

import com.kaloglu.library.ui.models.ErrorModel
import com.kaloglu.library.viewmodel.mvi.Event
import com.kaloglu.library.viewmodel.mvi.State
import com.zsk.androtweet.models.TweetFromDao
import com.zsk.androtweet.utils.Constants

sealed class TweetListState : State {
    object Init : TweetListState(), State.Init
    object NeedLogin : TweetListState(), State.Failure {
        override val error: ErrorModel by lazy { Constants.LoginError }
    }

    object UpdateUI : TweetListState(), State.Done

    class SelectedItem(val isSelected: Boolean) : TweetListState(), State.Custom
    class Failure(override val error: ErrorModel) : TweetListState(), State.Failure {
        constructor(errorMessage: String) : this(ErrorModel(errorMessage))
    }
}

sealed class TweetListEvent : Event {
    object Init : TweetListEvent()
    object ToggleSelectAllItem : TweetListEvent()
    object RefreshTweetList : TweetListEvent()
    object GetTweetList : TweetListEvent()

    data class ShowError(val message: String) : TweetListEvent()
    data class ShowLoading(val data: List<TweetFromDao>? = listOf()) : TweetListEvent()
    data class ToggleSelectItem(val item: TweetFromDao) : TweetListEvent()

}
