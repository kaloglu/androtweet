package com.zsk.androtweet.viewmodels

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.kaloglu.library.ui.viewmodel.databinding.BindableViewModel
import com.kaloglu.library.ui.viewmodel.databinding.bindable
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.models.Tweet
import com.zsk.androtweet.mvi.TweetListEvent
import com.zsk.androtweet.mvi.TweetListState
import com.zsk.androtweet.usecases.GetTweetList
import com.zsk.androtweet.utils.twitter.Resource
import com.zsk.androtweet.utils.twitter.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class TweetListViewModel(private val getList: GetTweetList)
    : BindableViewModel<TweetListEvent, TweetListState>(AndroTweetApp.instance) {

    @get:Bindable
    var tweetList by bindable(mutableListOf<Tweet>())

    init {
        onInit()
    }

    override fun onInit() {
        Log.i("TweetsViewModel", "onInit")
        getList.coroutineScope = viewModelScope
        getList.onEach {
            it.handleResource()
        }
    }

    override fun onEvent(event: TweetListEvent) {
        super.onEvent(event)
        when (event) {
            is TweetListEvent.GetTweetList -> getList.execute(event.userId)
        }
    }

    @Suppress("RedundantSuspendModifier", "UNCHECKED_CAST")
    private suspend fun <T> Resource<T>.handleResource() = when (status) {
        Status.EMPTY -> postState(TweetListState.Empty)
        Status.ERROR -> postState(TweetListState.Error(message!!))
        Status.LOADING -> postState(TweetListState.Loading)
        Status.SUCCESS -> onShowTweetList(data!! as List<Tweet>)
    }

    private fun onShowTweetList(list: List<Tweet>) {
        tweetList.addAll(list)
        postState(TweetListState.Success)
    }

}

