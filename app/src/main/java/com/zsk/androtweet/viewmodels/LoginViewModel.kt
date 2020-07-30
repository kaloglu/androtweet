package com.zsk.androtweet.viewmodels

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.kaloglu.library.databinding4vm.BindableViewModel
import com.kaloglu.library.databinding4vm.bindable
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.models.User
import com.zsk.androtweet.mvi.LoginEvent
import com.zsk.androtweet.mvi.LoginState
import com.zsk.androtweet.repositories.UserRepository
import com.zsk.androtweet.utils.ContextProviders
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
class LoginViewModel(
        private val contextProviders: ContextProviders = ContextProviders.instance,
        private val repository: UserRepository = UserRepository.getInstance()
) : BindableViewModel<LoginEvent, LoginState>(AndroTweetApp.instance) {

    override val eventFlow = MutableStateFlow<LoginEvent>(LoginEvent.LoggedOut)
    override val stateFlow = MutableStateFlow<LoginState>(LoginState.Init)

    private val dummyUser = User()

    @get:Bindable
    var user by bindable(User())

    @get:Bindable
    var title by bindable("")

    override fun onInit() {
        Log.i("LoginViewModel", "Init")
        super.onInit()
        viewModelScope.launch {
            repository.getUser()

            repository.userFlow
                    .onEach {
                        if (it != null)
                            postEvent(LoginEvent.LoggedIn(it))
                        else
                            postEvent(LoginEvent.LoggedOut)
                    }
                    .launchIn(this)

        }

    }

    override suspend fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.LogIn -> login(event.user)
            is LoginEvent.LogOut -> logout()
            is LoginEvent.LoggedIn -> postState(LoginState.Authenticated(event.user))
            is LoginEvent.LoggedOut -> postState(LoginState.UnAuthenticated)
        }
    }

    private suspend fun login(user: User) = repository.insert(user)

    private fun logout() {
        viewModelScope.launch {
            repository.clean()
        }
    }


}

