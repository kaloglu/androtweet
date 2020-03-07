package com.zsk.androtweet.viewmodels

import androidx.lifecycle.ViewModel
import com.kaloglu.library.ui.viewmodel.BaseViewModel
import com.kaloglu.library.ui.viewmodel.RepositoryViewModelFactory
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.repositories.UserRepository
import com.zsk.androtweet.usecases.GetUserUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class LoginViewModelFactory constructor(private val getUser: GetUserUseCase)
    : RepositoryViewModelFactory<AndroTweetApp>(AndroTweetApp.getInstance(), UserRepository.getInstance()) {

    @FlowPreview
    override fun <VM : ViewModel?> create(modelClass: Class<VM>) = when {
        BaseViewModel::class.java.isAssignableFrom(modelClass) -> {
            modelClass
                    .getConstructor(GetUserUseCase::class.java)
                    .newInstance(getUser)
                    .apply {
                        this as BaseViewModel<*>
                        this.onInit()
                    }
        }
        else ->
            super.create(modelClass)
    }

}