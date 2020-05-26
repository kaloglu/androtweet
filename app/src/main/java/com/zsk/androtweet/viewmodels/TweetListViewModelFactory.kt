package com.zsk.androtweet.viewmodels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import com.kaloglu.library.viewmodel.BaseViewModelFactory
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.usecases.GetTweetList
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class TweetListViewModelFactory constructor(lifecycle: Lifecycle, private val get: GetTweetList = GetTweetList())
    : BaseViewModelFactory<AndroTweetApp>(AndroTweetApp.instance, lifecycle) {

    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel?> create(modelClass: Class<VM>) = when {
        TweetListViewModel::class.java.isAssignableFrom(modelClass) -> {
            registerLifecycle(lifecycle)
            TweetListViewModel(get) as VM
        }
        else -> super.create(modelClass)
    }

    private fun registerLifecycle(lifecycle: Lifecycle) {}
}