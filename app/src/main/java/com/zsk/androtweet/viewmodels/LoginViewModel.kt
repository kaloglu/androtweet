package com.zsk.androtweet.viewmodels

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.kaloglu.library.ui.utils.Resource
import com.kaloglu.library.ui.viewmodel.databinding.BindableViewModel
import com.kaloglu.library.ui.viewmodel.databinding.bindable
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.models.User
import com.zsk.androtweet.states.LoginState
import com.zsk.androtweet.usecases.AddUserUseCase
import com.zsk.androtweet.usecases.GetUserFlowUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlin.coroutines.CoroutineContext

@FlowPreview
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class LoginViewModel constructor(private val getUser: GetUserFlowUseCase, private val addUser: AddUserUseCase)
    : BindableViewModel<User,LoginState>(AndroTweetApp.getInstance()), CoroutineScope {

    val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    val _state2 = MutableLiveData<LoginState>()

    val state2: LiveData<LoginState> = _state2

    @get:Bindable
    var user by bindable(User()) { old, new ->
        if (!old.equals(new))
            addUser(new)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun onAttachViewModel() {
        super.onAttachViewModel()

        _state2.observeForever {
            onState(it)
        }

        _state2.postValue(LoginState.Init)
        postState(LoginState.Init)

    }

    override fun onInitState() {
        Log.e("LoginViewModel", "INIT")
    }

    override fun onDataFailure() {
        TODO("Not yet implemented")
    }

    override fun onDataSuccess() {
            onState(LoginState.Authenticated(data))
    }

    override fun onState(state: LoginState) {
        super.onState(state)
        when (state) {
            is LoginState.Init -> getUser(viewModelScope, resultChannel)
            is LoginState.Authenticated -> onAuthenticated(state.data)
        }
    }

    fun login(user: User) {
        Log.d("LoginViewModel", user.name ?: " login : NULL")
        postState(LoginState.Authenticated(user))
        _state2.postValue(LoginState.Authenticated(user))
    }

    fun logout() {
        Log.d("LoginViewModel", "logout")
    }

    private fun onAuthenticated(data: User) {
        user = data
    }

    override val eventChannel: ConflatedBroadcastChannel<LoginState>
        get() = TODO("Not yet implemented")
    override val resultChannel: ConflatedBroadcastChannel<Resource<User>>
        get() = TODO("Not yet implemented")

    override fun Resource.Loading.onDataLoading() {
        TODO("Not yet implemented")
    }

}