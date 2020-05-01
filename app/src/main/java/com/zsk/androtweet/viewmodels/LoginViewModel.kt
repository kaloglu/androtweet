package com.zsk.androtweet.viewmodels

import android.util.Log
import androidx.databinding.Bindable
import com.kaloglu.library.ui.viewmodel.databinding.BindableViewModel
import com.kaloglu.library.ui.viewmodel.databinding.bindable
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.models.User
import com.zsk.androtweet.mvi.LoginEvent
import com.zsk.androtweet.mvi.LoginState
import com.zsk.androtweet.usecases.AddUserUseCaseLegacy
import com.zsk.androtweet.usecases.ClearUserUseCaseLegacy
import com.zsk.androtweet.usecases.GetUserUseCaseAsLiveData


class LoginViewModel(
        private val getUser: GetUserUseCaseAsLiveData,
        private val addUser: AddUserUseCaseLegacy,
        private val clearUser: ClearUserUseCaseLegacy
) : BindableViewModel<LoginEvent, LoginState>(AndroTweetApp.instance) {
    private val dummyUser = User()

    @get:Bindable
    var user by bindable(User())

    @get:Bindable
    var title by bindable("")

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
                else -> LoginEvent.LoggedIn(user)
            }
        }
    }

    override fun onEvent(event: LoginEvent) {
        super.onEvent(event)
        when (event) {
            is LoginEvent.LogIn -> login(event.user)
            is LoginEvent.LogOut -> logout()
            is LoginEvent.LoggedIn -> postState(LoginState.Authenticated(event.user))
            is LoginEvent.LoggedOut -> postState(LoginState.UnAuthenticated)
        }
    }

    private fun login(user: User) = addUser(user)

    private fun logout() = clearUser()

}

