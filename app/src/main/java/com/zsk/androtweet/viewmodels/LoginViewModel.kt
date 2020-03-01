package com.zsk.androtweet.viewmodels

import androidx.databinding.Bindable
import com.kaloglu.library.ui.viewmodel.databinding.BindableViewModel
import com.kaloglu.library.ui.viewmodel.databinding.bindable
import com.zsk.androtweet.models.User
import com.zsk.androtweet.repositories.UserRepository
import com.zsk.androtweet.states.LoginState
import com.zsk.androtweet.usecases.GetUserUseCases

class LoginViewModel(val repository: UserRepository = UserRepository()) : BindableViewModel<LoginState>() {

    @get:Bindable
    var user by bindable(User())

    init {
        postState(LoginState.Init())
    }

    override fun onState(state: LoginState) {
        when (state) {
            null -> return
            is LoginState.Init -> onInit()
            is LoginState.Authenticated -> onAuthenticated(state.data)
        }
    }

    private fun onAuthenticated(data: User) {
        user = data
    }

    private fun onInit() {
        GetUserUseCases(repository)().observeForever {
            if (it != null) postState(LoginState.Authenticated(it))
        }
    }

}
