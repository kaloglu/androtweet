package com.zsk.androtweet.viewmodels

import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import com.kaloglu.library.databinding4vm.BindableViewModel
import com.kaloglu.library.viewmodel.BaseViewModel
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.repositories.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.kaloglu.library.viewmodel.ViewModelFactory as VMF

@ExperimentalCoroutinesApi
class ViewModelFactoryWithAttachViewModel(
        override val lifecycle: Lifecycle,
        vararg _viewModels: BindableViewModel<*, *>
) : ViewModelFactory(lifecycle) {

    private var viewModels = _viewModels

    @CallSuper
    override fun onAttach(viewModel: BaseViewModel<*, *>) = viewModel.attachViewModel(*viewModels)

}

@ExperimentalCoroutinesApi
open class ViewModelFactory(
        override val lifecycle: Lifecycle
) : VMF<AndroTweetApp>(AndroTweetApp.instance, lifecycle) {

    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel?> create(modelClass: Class<VM>) = when {
        LoginViewModel::class.java.isAssignableFrom(modelClass) -> createLoginViewModel()
        else -> super.create(modelClass)
    } as VM

    private fun createLoginViewModel() =
            LoginViewModel(UserRepository.getInstance())
                    .apply {
                        onCreateViewModel()
                    }
}
