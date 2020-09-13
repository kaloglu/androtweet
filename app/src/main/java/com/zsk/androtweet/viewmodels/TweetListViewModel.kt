package com.zsk.androtweet.viewmodels

import androidx.databinding.Bindable
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.viewModelScope
import com.kaloglu.library.databinding4vm.BindableViewModel
import com.kaloglu.library.databinding4vm.bindable
import com.kaloglu.library.viewmodel.BaseViewModel
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.models.TweetFromDao
import com.zsk.androtweet.mvi.LoginState
import com.zsk.androtweet.mvi.TweetListEvent
import com.zsk.androtweet.mvi.TweetListState
import com.zsk.androtweet.repositories.TweetListRepository
import com.zsk.androtweet.utils.extensions.RoomExtensions.onIO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@ExperimentalCoroutinesApi
class TweetListViewModel(
        private val repository: TweetListRepository = TweetListRepository.getInstance()
) : BindableViewModel<TweetListEvent, TweetListState>(AndroTweetApp.instance), LifecycleObserver {

    @get:Bindable
    var tweetList by bindable(listOf<TweetFromDao>())

    @get:Bindable
    var selectedList by bindable(listOf<TweetFromDao>())

    @get:Bindable
    var failedList by bindable(listOf<TweetFromDao>())

    @get:Bindable
    var deletedList by bindable(listOf<TweetFromDao>())

    override suspend fun onEvent(event: TweetListEvent) {
        when (event) {
            is TweetListEvent.Init -> postState(TweetListState.Init)
            is TweetListEvent.GetTweetList -> getTweetList()
            is TweetListEvent.RefreshTweetList -> getTweetListFromRemote()
//            is TweetListEvent.ToggleSelectItem -> toggleSelectItem(event.item)
//            is TweetListEvent.ToggleSelectAllItem -> toggleSelectAllItem()
        }
    }

    private fun toggleSelectItem(item: TweetFromDao, force: Boolean) {
        val newSelection = selectedList.toMutableList()
        when {
            item.isSelected || force -> {
                newSelection.add(item)
            }
            else -> {
                newSelection.remove(item)
            }
        }
        selectedList = newSelection

    }

    private suspend fun getTweetList() {
        viewModelScope.onIO {
            repository.getTweets()
                    .collectLatest {
                        if (it != null)
                            tweetList = it
                    }
        }
    }

    private suspend fun getTweetListFromRemote() {
        if (repository.getTweetsRemote())
            postState(TweetListState.UpdateUI)
    }

    override fun <VM : BaseViewModel<*, *>> attachViewModel(vararg viewModels: VM) {
        viewModels.forEach { viewModel ->
            when (viewModel) {
                is LoginViewModel -> onAttachLoginViewModel(viewModel)
            }
        }
    }

    private fun onAttachLoginViewModel(viewModel: LoginViewModel) {
        viewModelScope.onIO {
            viewModel.stateFlow
                    .collect { loginState ->
                        when (loginState) {
                            is LoginState.UnAuthenticated -> postState(TweetListState.NeedLogin)
                            is LoginState.Authenticated -> {
                                postEvent(TweetListEvent.GetTweetList)
                            }
                        }
                    }
        }
    }

    fun deleteSelectedTweets() {
        viewModelScope.onIO {
//            repository.delete(list.filter { it.isSelected })
        }
    }

}
