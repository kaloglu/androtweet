package com.zsk.androtweet.viewmodels

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.kaloglu.library.databinding4vm.BindableViewModel
import com.kaloglu.library.databinding4vm.bindable
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.models.UserFromDao
import com.zsk.androtweet.mvi.LoginEvent
import com.zsk.androtweet.mvi.LoginState
import com.zsk.androtweet.repositories.UserRepository
import com.zsk.androtweet.utils.extensions.RoomExtensions.onIO
import com.zsk.androtweet.utils.extensions.RoomExtensions.onUndispatchedIO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class LoginViewModel(
        private val repository: UserRepository = UserRepository.getInstance()
) : BindableViewModel<LoginEvent, LoginState>(AndroTweetApp.instance) {

    @get:Bindable
    var user by bindable(UserFromDao())

    @get:Bindable
    var title by bindable("")

    init {
        getUser()
    }

    override suspend fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.LogIn -> addUser(event.user)
            is LoginEvent.LogOut -> logoutUser()
            is LoginEvent.LoggedOut -> postState(LoginState.UnAuthenticated)
        }
    }

    private fun getUser() {
        viewModelScope.onUndispatchedIO {
            repository.getUser()
                    .collect {
                        user = it ?: UserFromDao()
                        when {
                            it != null -> postState(LoginState.Authenticated(user))
                            else -> postEvent(LoginEvent.LoggedOut)
                        }
                    }
        }
    }

    private suspend fun logoutUser() {
        viewModelScope.onIO {
            repository.clean()
        }
    }

    private suspend fun addUser(user: UserFromDao) = viewModelScope.onIO {
        repository.insert(user)
    }

}
