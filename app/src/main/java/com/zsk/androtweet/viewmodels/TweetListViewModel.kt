package com.zsk.androtweet.viewmodels

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.viewModelScope
import com.kaloglu.library.databinding4vm.BindableViewModel
import com.kaloglu.library.databinding4vm.bindable
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.models.SelectableTweet
import com.zsk.androtweet.mvi.TweetListEvent
import com.zsk.androtweet.mvi.TweetListState
import com.zsk.androtweet.repositories.TweetListRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class TweetListViewModel(
        private val repository: TweetListRepository = TweetListRepository.getInstance()
) : BindableViewModel<TweetListEvent, TweetListState>(AndroTweetApp.instance), LifecycleObserver {
    override val eventFlow = MutableStateFlow<TweetListEvent>(TweetListEvent.Init)
    override val stateFlow = MutableStateFlow<TweetListState>(TweetListState.Init)

    @get:Bindable
    var list by bindable(emptyList<SelectableTweet>()) { o, n ->
        n.size
    }

    @get:Bindable
    var hasSelected: Boolean by bindable(false)

    override fun onInit() {
        Log.i("LoginViewModel", "Init")

        viewModelScope.launch {
            repository.tweetFlow
                    .collect { (selectableTweetList, error) ->
                        when {
                            selectableTweetList != null -> list = selectableTweetList
                            error != null -> postState(TweetListState.Failure(error))
                        }

                    }

        }

        super.onInit()
    }

    override suspend fun onEvent(event: TweetListEvent) {
        when (event) {
            is TweetListEvent.GetTweetList -> {
                list = emptyList()
                repository.initialList(event.userId)
            }
            is TweetListEvent.UpdateList -> list = event.tweetList
            is TweetListEvent.ToggleSelectItem -> checkHasSelected()
            is TweetListEvent.ToggleSelectAllItem -> toggleSelectAllItem()
        }
    }

    fun deleteSelectedTweets() {
//        repository.destroyTweets(list.filter { it.isSelected })
    }

    private fun toggleSelectAllItem() {
        list = list
//        map { it.isSelected = !hasSelected }
        postState(TweetListState.SelectedItem(!hasSelected))
    }

    private fun checkHasSelected(default: Boolean = false) {
        hasSelected = list.filter { it.result.isEmpty() }.find { it.isSelected }?.isSelected
                ?: default
    }

}
