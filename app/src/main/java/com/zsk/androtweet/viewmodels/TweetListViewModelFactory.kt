package com.zsk.androtweet.viewmodels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import com.kaloglu.library.viewmodel.BaseViewModelFactory
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.repositories.TweetListRepository
import com.zsk.androtweet.repositories.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class TweetListViewModelFactory constructor(lifecycle: Lifecycle)
    : BaseViewModelFactory<AndroTweetApp>(AndroTweetApp.instance, lifecycle) {

    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel?> create(modelClass: Class<VM>) = when {
        TweetListViewModel::class.java.isAssignableFrom(modelClass) -> {
            val repository = TweetListRepository.getInstance()
            val viewModel = TweetListViewModel(repository = repository)
            lifecycle.addObserver(repository)
            lifecycle.addObserver(viewModel)
            viewModel as VM
        }
        else -> super.create(modelClass)
    }

}