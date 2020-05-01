package com.zsk.androtweet.mvi

import com.kaloglu.library.ui.viewmodel.mvi.Event
import com.kaloglu.library.ui.viewmodel.mvi.State
import com.zsk.androtweet.models.User

sealed class LoginState : State {
    object UnAuthenticated : LoginState(), State.Success
    data class Authenticated(val user: User) : LoginState(), State.Success
}

sealed class LoginEvent : Event {
    data class LogIn(val user: User) : LoginEvent(), Event.Custom
    data class LoggedIn(val user: User) : LoginEvent(), Event.Custom
    object LogOut : LoginEvent(), Event.Custom
    object LoggedOut : LoginEvent(), Event.Custom
}
