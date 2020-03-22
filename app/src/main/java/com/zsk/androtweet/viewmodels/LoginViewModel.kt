package com.zsk.androtweet.viewmodels

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewModelScope
import com.kaloglu.library.ui.utils.Resource
import com.kaloglu.library.ui.viewmodel.databinding.BindableViewModel
import com.kaloglu.library.ui.viewmodel.databinding.bindable
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.models.User
import com.zsk.androtweet.states.LoginState
import com.zsk.androtweet.usecases.AddUserUseCase
import com.zsk.androtweet.usecases.GetUserFlowUseCase
import kotlinx.coroutines.launch


class LoginViewModel constructor(private val getUser: GetUserFlowUseCase, private val addUser: AddUserUseCase)
    : BindableViewModel<User, LoginState>(AndroTweetApp.getInstance()) {

    @get:Bindable
    var user by bindable(User())

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun onAttachViewModel() {
        super.onAttachViewModel()
        getUser()
        postState(LoginState.Init)
    }

    override fun onDataLoading(loading: Resource.Loading<User>) {
        TODO("Not yet implemented")
    }

    override fun onInitState() {
        Log.e("LoginViewModel", "INIT")
    }

    override fun onDataFailure(failure: Resource.Failure<User>) {
        TODO("Not yet implemented")
    }

    override fun onDataSuccess(success: Resource.Success<User>) {
        onState(LoginState.Authenticated(success.data))
    }

    override fun onUiState(state: LoginState) {
        super.onUiState(state)
        when (state) {
            is LoginState.Authenticated -> onAuthenticated(state.data)
        }
    }

    fun login(user: User) {
        Log.d("LoginViewModel", user.name ?: " login : NULL")
        viewModelScope.launch {
            addUser.invoke(user)
        }
    }

    fun logout() {
        Log.d("LoginViewModel", "logout")
    }

    private fun onAuthenticated(data: User) {
        user = data
    }
}