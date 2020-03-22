package com.zsk.androtweet.viewmodels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import com.kaloglu.library.ui.viewmodel.BaseViewModelFactory
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.usecases.AddUserUseCase
import com.zsk.androtweet.usecases.ClearUserUseCase
import com.zsk.androtweet.usecases.GetUserLiveDataUseCase

class LoginViewModelFactory constructor(
        lifecycle: Lifecycle,
        private val getUser: GetUserLiveDataUseCase = GetUserLiveDataUseCase(),
        private val addUser: AddUserUseCase = AddUserUseCase(),
        private val clearUser: ClearUserUseCase = ClearUserUseCase()
) : BaseViewModelFactory<AndroTweetApp>(AndroTweetApp.getInstance(), lifecycle) {

    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel?> create(modelClass: Class<VM>) = when {
        LoginViewModel::class.java.isAssignableFrom(modelClass) -> {
            registerLifecycle(lifecycle)
            LoginViewModel(getUser, addUser, clearUser) as VM
        }
        else -> super.create(modelClass)
    }

    private fun registerLifecycle(lifecycle: Lifecycle) {
        getUser.registerLifecycle(lifecycle)
        addUser.registerLifecycle(lifecycle)
        clearUser.registerLifecycle(lifecycle)
    }
}