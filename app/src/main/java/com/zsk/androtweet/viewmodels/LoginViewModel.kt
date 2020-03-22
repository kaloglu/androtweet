package com.zsk.androtweet.viewmodels

import androidx.databinding.Bindable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.kaloglu.library.ui.models.ErrorModel
import com.kaloglu.library.ui.utils.Resource
import com.kaloglu.library.ui.viewmodel.databinding.BindableViewModel
import com.kaloglu.library.ui.viewmodel.databinding.bindable
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.models.User
import com.zsk.androtweet.states.LoginState
import com.zsk.androtweet.usecases.AddUserUseCase
import com.zsk.androtweet.usecases.ClearUserUseCase
import com.zsk.androtweet.usecases.GetUserLiveDataUseCase


class LoginViewModel(
        private val getUser: GetUserLiveDataUseCase,
        private val addUser: AddUserUseCase,
        private val clearUser: ClearUserUseCase
) : BindableViewModel<User, LoginState>(AndroTweetApp.getInstance()) {

    @get:Bindable
    var user by bindable(User())

    init {
        onAttachViewModel()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun onAttachViewModel() {

        super.onAttachViewModel()

        postState(LoginState.Init)
    }

    override fun onDataSuccess(success: Resource.Success<User>) = postState(LoginState.Authenticated(success.data))

    override fun onDataFailure(failure: Resource.Failure<User>) = postState(LoginState.UnAuthenticated)

    override fun onInitState() {
        stateMediatorLiveData
                .addSource(getUser()) {
                    handleResult(
                            when (it) {
                                null -> Resource.Failure(ErrorModel("401", "Login Needed!"))
                                else -> Resource.Success(it)
                            }
                    )
                }
    }

    override fun onUiState(state: LoginState) {
        super.onUiState(state)
        when (state) {
            is LoginState.Authenticated -> onAuthenticated(state.data)
            is LoginState.UnAuthenticated -> onUnAuthenticated()
        }
    }

    fun login(user: User) = addUser(user)

    fun logout() = clearUser()

    private fun onUnAuthenticated() {
        user = User()
    }

    private fun onAuthenticated(data: User) {
        user = data
    }

}