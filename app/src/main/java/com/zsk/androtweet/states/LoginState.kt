package com.zsk.androtweet.states

import com.kaloglu.library.ui.viewmodel.states.State
import com.zsk.androtweet.models.User

sealed class LoginState : State {
    object Init : LoginState(), State.UiState.Init
    object UnAuthenticated : LoginState(), State.UiState.Done
    data class Authenticated(val data: User) : LoginState(), State.UiState.Done
}
