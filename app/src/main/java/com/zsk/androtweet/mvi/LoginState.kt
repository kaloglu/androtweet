package com.zsk.androtweet.mvi

import com.kaloglu.library.viewmodel.mvi.Event
import com.kaloglu.library.viewmodel.mvi.State
import com.zsk.androtweet.models.User

sealed class LoginState : State {
    object Init : LoginState(), State.Init
    object UnAuthenticated : LoginState(), State.Custom
    data class Authenticated(val user: User) : LoginState(), State.Custom
}

sealed class LoginEvent : Event {
    object Init : LoginEvent()
    data class LogIn(val user: User) : LoginEvent()
    data class LoggedIn(val user: User) : LoginEvent()
    object LogOut : LoginEvent()
    object LoggedOut : LoginEvent()
}
