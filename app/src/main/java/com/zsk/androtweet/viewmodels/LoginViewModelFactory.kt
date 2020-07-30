package com.zsk.androtweet.viewmodels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import com.kaloglu.library.viewmodel.BaseViewModelFactory
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.repositories.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class LoginViewModelFactory constructor(lifecycle: Lifecycle)
    : BaseViewModelFactory<AndroTweetApp>(AndroTweetApp.instance, lifecycle) {

    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel?> create(modelClass: Class<VM>) = when {
        LoginViewModel::class.java.isAssignableFrom(modelClass) -> {
            val repository = UserRepository.getInstance()
            val viewModel = LoginViewModel(repository = repository)
            lifecycle.addObserver(viewModel)
            viewModel as VM
        }
        else -> super.create(modelClass)
    }

}