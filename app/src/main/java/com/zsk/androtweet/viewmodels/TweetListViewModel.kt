package com.zsk.androtweet.viewmodels

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.kaloglu.library.databinding4vm.BindableViewModel
import com.kaloglu.library.databinding4vm.bindable
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.models.TweetWithUser
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
    var list by bindable(listOf<TweetWithUser>())
        internal set

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

    @Suppress("UNCHECKED_CAST")
    private fun <T> Resource<T>.handleResource() =
            when (status) {
                Status.ERROR -> postState(TweetListState.Error(message!!))
                Status.LOADING -> postState(TweetListState.Loading)
                Status.SUCCESS -> setList(data!! as List<TweetWithUser>, TweetListState.Success)
                Status.EMPTY -> setList(listOf(), TweetListState.Empty)
            }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    fun setList(list: List<TweetWithUser>, state: TweetListState? = null) {
        this.list = list.toMutableList()
        state?.let {
            postState(state)
        }
    }

    fun selectAllItem() {
        val setSelected = !(list.find { !it.isSelected }?.isSelected ?: true)
        val tempList = list
        tempList.map { it.isSelected = setSelected }
        setList(tempList)
    }
}


