package com.zsk.androtweet.states

import com.kaloglu.library.ui.models.ErrorModel
import com.kaloglu.library.ui.viewmodel.states.State
import com.zsk.androtweet.models.User

sealed class LoginState : State {
    object Init : LoginState(), State.UiState.Init
    data class UnAuthenticated(override val error: ErrorModel) : LoginState(), State.UiState.Error
    data class Authenticated(val data: User) : LoginState(), State.UiState.Done
}
