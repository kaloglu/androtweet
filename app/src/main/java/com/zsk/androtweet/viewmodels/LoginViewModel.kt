package com.zsk.androtweet.viewmodels

import androidx.databinding.Bindable
import androidx.lifecycle.Transformations
import com.kaloglu.library.ui.viewmodel.databinding.BindableViewModel
import com.kaloglu.library.ui.viewmodel.databinding.bindable
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.models.User
import com.zsk.androtweet.repositories.UserRepository
import com.zsk.androtweet.states.LoginState
import com.zsk.androtweet.usecases.CleanUserUseCases
import com.zsk.androtweet.usecases.GetUserUseCases
import com.zsk.androtweet.usecases.InsertUserUseCases

class LoginViewModel constructor(application: AndroTweetApp, val repository: UserRepository) : BindableViewModel<LoginState>(application) {

    @get:Bindable
    var user by bindable(User())

    init {
        postState(LoginState.Init())
    }

    override fun onState(state: LoginState) {
        when (state) {
            is LoginState.Init -> onInit()
            is LoginState.Authenticated -> onAuthenticated(state.data)
        }
    }

    private fun onAuthenticated(data: User) {
        user = data
    }

    private fun onInit() {
        GetUserUseCases(repository, this::postState)
    }

    fun login(user: User) {
        InsertUserUseCases(repository)(user)
    }

    fun logout() {
        CleanUserUseCases(repository)
    }

}
