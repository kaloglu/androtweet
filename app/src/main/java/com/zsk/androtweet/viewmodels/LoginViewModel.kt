package com.zsk.androtweet.viewmodels

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.kaloglu.library.ui.models.ErrorModel
import com.kaloglu.library.ui.viewmodel.databinding.BindableViewModel
import com.kaloglu.library.ui.viewmodel.databinding.bindable
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.models.User
import com.zsk.androtweet.states.LoginState
import com.zsk.androtweet.usecases.GetUserUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class LoginViewModel constructor(private val getUser: GetUserUseCase)
    : BindableViewModel<LoginState>(AndroTweetApp.getInstance()) {

    override val resultChannel = getUser.resultChannel

    @get:Bindable
    var user by bindable(User())

    override fun onInit() {
        getUser.viewModelScope = viewModelScope
        getUser()
    }

    override fun onInitState() {
        Log.e("LoginViewModel", "INIT")
    }

    override fun onFailureState(error: ErrorModel) {
        Log.e("LoginViewModel", error.message)
    }

    override fun onSuccessState(data: Any?) {
        if (data is User)
            onState(LoginState.Authenticated(data))
    }

    override fun onState(state: LoginState) {
        super.onState(state)
        when (state) {
            is LoginState.Authenticated -> onAuthenticated(state.data)
        }
    }

    fun login(user: User) {
        Log.d("LoginViewModel", user.name ?: " login : NULL")
    }

    fun logout() {
        Log.d("LoginViewModel", "logout")
    }

    private fun onAuthenticated(data: User) {
        user = data
    }

}
