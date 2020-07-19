package com.zsk.androtweet.viewmodels

import androidx.databinding.Bindable
import com.kaloglu.library.databinding4vm.BindableViewModel
import com.kaloglu.library.databinding4vm.bindable
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.models.SelectableTweet
import com.zsk.androtweet.mvi.TweetListEvent
import com.zsk.androtweet.mvi.TweetListState
import com.zsk.androtweet.repositories.TweetListRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class TweetListViewModel(
        private val repository: TweetListRepository = TweetListRepository.getInstance()
) : BindableViewModel<TweetListEvent, TweetListState>(AndroTweetApp.instance) {

    val pagingManager: TweetListRepository = repository

    @get:Bindable
    var list by bindable(emptyList<SelectableTweet>()) { o, n ->
        n.size
    }

    @get:Bindable
    var hasSelected: Boolean by bindable(false)

    init {
        onInit()
    }

    override fun onInit() {

    }

    override fun onEvent(event: TweetListEvent) {
        super.onEvent(event)
        when (event) {
            is TweetListEvent.GetTweetList -> {
                repository.initialList(event.userId)
                list = emptyList()
            }
            is TweetListEvent.UpdateList -> list = event.tweetList
            is TweetListEvent.ToggleSelectItem -> checkHasSelected()
            is TweetListEvent.ToggleSelectAllItem -> toggleSelectAllItem()
        }
    }

    fun deleteSelectedTweets() {
        repository.destroyTweets(list.filter { it.isSelected })
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

