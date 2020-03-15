package com.zsk.androtweet.viewmodels

import androidx.lifecycle.ViewModel
import com.kaloglu.library.ui.viewmodel.BaseViewModel
import com.kaloglu.library.ui.viewmodel.RepositoryViewModelFactory
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.repositories.UserRepository
import com.zsk.androtweet.usecases.AddUserUseCase
import com.zsk.androtweet.usecases.GetUserFlowUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class LoginViewModelFactory constructor(
        private val getUser: GetUserFlowUseCase = GetUserFlowUseCase(),
        private val addUser: AddUserUseCase = AddUserUseCase()
) : RepositoryViewModelFactory<AndroTweetApp>(AndroTweetApp.getInstance(), UserRepository.getInstance()) {

    @FlowPreview
    override fun <VM : ViewModel?> create(modelClass: Class<VM>) = when {
        BaseViewModel::class.java.isAssignableFrom(modelClass) -> {
            modelClass
                    .getConstructor(
                            GetUserFlowUseCase::class.java,
                            AddUserUseCase::class.java
                    )
                    .newInstance(getUser, addUser)
                    .apply {
                        this as BaseViewModel<*>
                        this.onInit()
                    }
        }
        else ->
            super.create(modelClass)
    }

}