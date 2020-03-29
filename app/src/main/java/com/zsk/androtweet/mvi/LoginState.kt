package com.zsk.androtweet.mvi

import com.kaloglu.library.ui.viewmodel.mvi.Event
import com.kaloglu.library.ui.viewmodel.mvi.State
import com.zsk.androtweet.models.User

sealed class LoginState : State {
    //    object Init : LoginState(), State.Init
    object UnAuthenticated : LoginState(), State.Done
    object /*data class*/ Authenticated/*(val data: User)*/ : LoginState(), State.Done
}

sealed class LoginEvent : Event {
    //    object Init : LoginEvent(), Event.Init
    data class LogIn(val data: User) : LoginEvent(), Event.Custom
    object LoggedIn : LoginEvent(), Event.Custom
    object LogOut : LoginEvent(), Event.Custom
    object LoggedOut : LoginEvent(), Event.Custom
}
