package com.zsk.androtweet.viewmodels

import androidx.databinding.Bindable
import androidx.paging.PagedList
import com.kaloglu.library.databinding4vm.BindableViewModel
import com.kaloglu.library.databinding4vm.bindable
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.models.SelectableTweet
import com.zsk.androtweet.mvi.TweetListEvent
import com.zsk.androtweet.mvi.TweetListState
import com.zsk.androtweet.repositories.TweetListRepository
import com.zsk.androtweet.utils.twitter.Resource
import com.zsk.androtweet.utils.twitter.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class TweetListViewModel(
        private val repository: TweetListRepository = TweetListRepository.getInstance()
) : BindableViewModel<TweetListEvent, TweetListState>(AndroTweetApp.instance) {

    @get:Bindable
    var list by bindable(initUserTimeline()) { o, n ->
        if (n.isNotEmpty())
            n.last()
    }

    @get:Bindable
    var hasSelected by bindable(false)

    init {
        onInit()
    }

    override fun onInit() = Unit

    override fun onEvent(event: TweetListEvent) {
        super.onEvent(event)
        when (event) {
            is TweetListEvent.ToggleSelectItem -> checkHasSelected()
            is TweetListEvent.ToggleSelectAllItem -> toggleSelectAllItem()
        }
    }

    fun deleteSelectedTweets() {
        repository.destroyTweets(list.filter { it.isSelected })
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> Resource<T>.handleResource() =
            when (status) {
                Status.ERROR -> postState(TweetListState.Error(message!!))
                Status.LOADING -> postState(TweetListState.Loading)
                Status.SUCCESS -> setList(data!! as PagedList<SelectableTweet>, TweetListState.Success)
                Status.EMPTY -> {
                    postState(TweetListState.Empty)
                }
            }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    fun setList(list: PagedList<SelectableTweet>, state: TweetListState? = null) {
        this.list = list
        state?.let {
            postState(state)
        }
    }

    private fun toggleSelectAllItem() {
        val setSelected = !hasSelected
        val tempList = list
        tempList.map { it.isSelected = setSelected }
        setList(tempList)
        checkHasSelected()
    }

    fun activeUserId(id: Long) {
        repository.setUserId(id)
        list = initUserTimeline()
    }

    private fun checkHasSelected(default: Boolean = false) {
        hasSelected = list.filter { it.result.isEmpty() }.find { it.isSelected }?.isSelected
                ?: default
    }

    private fun initUserTimeline() = repository.initUserTimeline()


}

