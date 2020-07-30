package com.zsk.androtweet.viewmodels

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
import com.zsk.androtweet.utils.extensions.RoomExtensions.onUndispatchedIO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class LoginViewModel(
        private val contextProviders: ContextProviders = ContextProviders.instance,
        private val repository: UserRepository = UserRepository.getInstance()
) : BindableViewModel<LoginEvent, LoginState>(AndroTweetApp.instance) {

    override val eventFlow = MutableStateFlow<LoginEvent>(LoginEvent.Init)
    override val stateFlow = MutableStateFlow<LoginState>(LoginState.Init)

    @get:Bindable
    var user by bindable(User())

    @get:Bindable
    var title by bindable("")

    override fun onInit() {
        super.onInit()

        getUser()
    }

    override suspend fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.LogIn -> addUser(event.user)
            is LoginEvent.LogOut -> logoutUser()
            is LoginEvent.LoggedOut -> logout()
        }
    }

    private fun getUser() {
        viewModelScope.onUndispatchedIO(contextProviders) {
            repository.getUser()
                    .collect {
                        when {
                            it != null -> login(it)
                            else -> logout()
                        }
                    }
        }
    }

    private suspend fun logoutUser() {
        viewModelScope.launch(contextProviders.IO) {
            repository.clean()
        }
    }

    private suspend fun addUser(user: User) = viewModelScope.launch(contextProviders.IO) {
        repository.insert(user)
    }

    private fun logout() {
        postState(LoginState.UnAuthenticated)
    }

    private fun login(user: User) {
        postState(LoginState.Authenticated(user))
    }

}
