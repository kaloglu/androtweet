package com.zsk.androtweet.viewmodels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import com.kaloglu.library.ui.viewmodel.BaseViewModelFactory
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.usecases.AddUserUseCase
import com.zsk.androtweet.usecases.GetUserFlowUseCase

class LoginViewModelFactory constructor(
        lifecycle: Lifecycle,
        private val getUser: GetUserFlowUseCase = GetUserFlowUseCase(),
        private val addUser: AddUserUseCase = AddUserUseCase()
) : BaseViewModelFactory<AndroTweetApp>(AndroTweetApp.getInstance(), lifecycle) {


    override fun <VM : ViewModel?> create(modelClass: Class<VM>) = when {
        LoginViewModel::class.java.isAssignableFrom(modelClass) -> {
            registerLifecycle(lifecycle)
            LoginViewModel(getUser, addUser) as VM
        }
        else -> super.create(modelClass)
    }

    private fun registerLifecycle(lifecycle: Lifecycle) {
        getUser.registerLifecycle(lifecycle)
        addUser.registerLifecycle(lifecycle)
    }
}