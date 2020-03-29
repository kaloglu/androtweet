package com.zsk.androtweet.viewmodels

import android.util.Log
import androidx.databinding.Bindable
import com.kaloglu.library.ui.viewmodel.databinding.BindableViewModel
import com.kaloglu.library.ui.viewmodel.databinding.bindable
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.models.User
import com.zsk.androtweet.mvi.LoginEvent
import com.zsk.androtweet.mvi.LoginState
import com.zsk.androtweet.usecases.AddUserUseCase
import com.zsk.androtweet.usecases.ClearUserUseCase
import com.zsk.androtweet.usecases.GetUserLiveDataUseCase


class LoginViewModel(
        private val getUser: GetUserLiveDataUseCase,
        private val addUser: AddUserUseCase,
        private val clearUser: ClearUserUseCase
) : BindableViewModel<LoginEvent, LoginState>(AndroTweetApp.getInstance()) {
    private val dummyUser = User()

    @get:Bindable
    var user by bindable(User())

    init {
        Log.i("LoginViewModel", "Init")
        onInit()
    }

    override fun onInit() {
        Log.i("LoginViewModel", "onInit")
        getUser().mapToEvent {
            user = it ?: dummyUser
            when (user) {
                dummyUser -> LoginEvent.LoggedOut
                else -> LoginEvent.LoggedIn
            }
        }
    }

    override fun onEvent(event: LoginEvent) {
        super.onEvent(event)
        when (event) {
            is LoginEvent.LogIn -> login(event.data)
            is LoginEvent.LogOut -> logout()
            is LoginEvent.LoggedIn -> postState(LoginState.Authenticated)
            is LoginEvent.LoggedOut -> postState(LoginState.UnAuthenticated)
        }
    }

    private fun login(user: User) = addUser(user)

    fun logout() = clearUser()

}

