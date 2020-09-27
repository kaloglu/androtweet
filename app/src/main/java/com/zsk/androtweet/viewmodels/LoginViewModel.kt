package com.zsk.androtweet.viewmodels

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.kaloglu.library.databinding4vm.BindableViewModel
import com.kaloglu.library.databinding4vm.bindable
import com.kaloglu.library.viewmodel.mvi.Event
import com.kaloglu.library.viewmodel.mvi.State
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

    override val idleState: State.Idle = LoginState.Idle
    override val idleEvent: Event.Idle = LoginEvent.Idle

    @get:Bindable
    var user by bindable(UserFromDao())

    @get:Bindable
    var title by bindable("")

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.onUndispatchedIO {
            repository.getUser()
                    .collect {
                        user = it ?: UserFromDao()
                        when {
                            it != null -> postState(LoginState.Authenticated(user))
                            else -> postState(LoginState.UnAuthenticated)
                        }
                    }
        }
    }

    fun logout() = viewModelScope.onIO {
        repository.clean()
    }

    fun login(user: UserFromDao) = viewModelScope.onIO {
        repository.insert(user)
    }

}
