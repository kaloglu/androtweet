package com.zsk.androtweet.states

import com.kaloglu.library.ui.viewmodel.states.State
import com.zsk.androtweet.models.User

sealed class LoginState : State {
    data class Init(override val value: Int = -1) : LoginState()
    data class Authenticated(val data: User, override val value: Int = 1) : LoginState()
    data class UnAuthenticated(override val value: Int = 0) : LoginState()
}
