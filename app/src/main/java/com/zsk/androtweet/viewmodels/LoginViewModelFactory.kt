package com.zsk.androtweet.viewmodels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import com.kaloglu.library.ui.viewmodel.BaseViewModelFactory
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.usecases.AddUserUseCaseLegacy
import com.zsk.androtweet.usecases.ClearUserUseCaseLegacy
import com.zsk.androtweet.usecases.GetUserUseCaseAsLiveData

class LoginViewModelFactory constructor(
        lifecycle: Lifecycle,
        private val getUser: GetUserUseCaseAsLiveData = GetUserUseCaseAsLiveData(),
        private val addUser: AddUserUseCaseLegacy = AddUserUseCaseLegacy(),
        private val clearUser: ClearUserUseCaseLegacy = ClearUserUseCaseLegacy()
) : BaseViewModelFactory<AndroTweetApp>(AndroTweetApp.instance, lifecycle) {

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