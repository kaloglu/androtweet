package com.zsk.androtweet.viewmodels

import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.kaloglu.library.databinding4vm.BindableViewModel
import com.kaloglu.library.viewmodel.BaseViewModel
import com.zsk.androtweet.repositories.TweetListRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
open class TweetListViewModelFactory(
        override val lifecycle: Lifecycle,
        vararg _viewModels: BindableViewModel<*, *>
) : ViewModelFactory(lifecycle) {

    @CallSuper
    override fun onAttach(viewModel: BaseViewModel<*, *>) = viewModel.attachViewModel(*viewModels)
    private var viewModels = _viewModels

    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel?> create(modelClass: Class<VM>) = when {
        TweetListViewModel::class.java.isAssignableFrom(modelClass) -> createTweetListViewModel()
        else -> super.create(modelClass)
    } as VM

    private fun createTweetListViewModel() =
            TweetListViewModel(TweetListRepository.getInstance())
                    .apply {
                        onCreateViewModel()
                    }

}
